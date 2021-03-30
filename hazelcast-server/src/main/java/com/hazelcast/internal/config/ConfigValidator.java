/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.internal.config;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheConfig;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.CollectionConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.EndpointConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.HotRestartConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.MultiMapConfig;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.ReplicatedMapConfig;
import com.hazelcast.config.RingbufferConfig;
import com.hazelcast.config.ScheduledExecutorConfig;
import com.hazelcast.config.ServerSocketEndpointConfig;
import com.hazelcast.config.WanBatchPublisherConfig;
import com.hazelcast.config.WanReplicationConfig;
import com.hazelcast.config.cp.CPSubsystemConfig;
import com.hazelcast.instance.EndpointQualifier;
import com.hazelcast.instance.ProtocolType;
import com.hazelcast.internal.util.MutableInteger;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.eviction.EvictionPolicyComparator;
import com.hazelcast.spi.merge.MergingValue;
import com.hazelcast.spi.merge.SplitBrainMergePolicyProvider;
import com.hazelcast.spi.merge.SplitBrainMergeTypes;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.spi.properties.HazelcastProperty;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.hazelcast.config.EvictionPolicy.LFU;
import static com.hazelcast.config.EvictionPolicy.LRU;
import static com.hazelcast.config.EvictionPolicy.NONE;
import static com.hazelcast.config.EvictionPolicy.RANDOM;
import static com.hazelcast.config.InMemoryFormat.NATIVE;
import static com.hazelcast.config.MaxSizePolicy.ENTRY_COUNT;
import static com.hazelcast.config.MaxSizePolicy.FREE_HEAP_PERCENTAGE;
import static com.hazelcast.config.MaxSizePolicy.FREE_HEAP_SIZE;
import static com.hazelcast.config.MaxSizePolicy.FREE_NATIVE_MEMORY_PERCENTAGE;
import static com.hazelcast.config.MaxSizePolicy.FREE_NATIVE_MEMORY_SIZE;
import static com.hazelcast.config.MaxSizePolicy.PER_NODE;
import static com.hazelcast.config.MaxSizePolicy.PER_PARTITION;
import static com.hazelcast.config.MaxSizePolicy.USED_HEAP_PERCENTAGE;
import static com.hazelcast.config.MaxSizePolicy.USED_HEAP_SIZE;
import static com.hazelcast.config.MaxSizePolicy.USED_NATIVE_MEMORY_PERCENTAGE;
import static com.hazelcast.config.MaxSizePolicy.USED_NATIVE_MEMORY_SIZE;
import static com.hazelcast.instance.BuildInfoProvider.getBuildInfo;
import static com.hazelcast.instance.ProtocolType.MEMBER;
import static com.hazelcast.instance.ProtocolType.WAN;
import static com.hazelcast.internal.config.MergePolicyValidator.checkMapMergePolicy;
import static com.hazelcast.internal.config.MergePolicyValidator.checkMergeTypeProviderHasRequiredTypes;
import static com.hazelcast.internal.util.Preconditions.checkTrue;
import static com.hazelcast.internal.util.StringUtil.isNullOrEmpty;
import static com.hazelcast.spi.properties.ClusterProperty.HOT_RESTART_FREE_NATIVE_MEMORY_PERCENTAGE;
import static java.lang.String.format;

/**
 * Validates a Hazelcast configuration in a specific
 * context like OS vs. EE or client vs. member nodes.
 */
@SuppressWarnings({"checkstyle:classfanoutcomplexity", "checkstyle:methodcount"})
public final class ConfigValidator extends CommonConfigValidator {

    public static final EnumSet<EvictionPolicy> COMMONLY_SUPPORTED_EVICTION_POLICIES = EnumSet.of(LRU, LFU);

    private static final EnumSet<EvictionPolicy> MAP_SUPPORTED_EVICTION_POLICIES
            = EnumSet.of(LRU, LFU, RANDOM, NONE);

    private static final EnumSet<MaxSizePolicy> MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES
            = EnumSet.of(PER_NODE, PER_PARTITION, USED_NATIVE_MEMORY_PERCENTAGE,
            FREE_NATIVE_MEMORY_PERCENTAGE, USED_NATIVE_MEMORY_SIZE, FREE_NATIVE_MEMORY_SIZE);

    private static final EnumSet<MaxSizePolicy> MAP_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES
            = EnumSet.of(PER_NODE, PER_PARTITION, USED_HEAP_SIZE, USED_HEAP_PERCENTAGE,
            FREE_HEAP_SIZE, FREE_HEAP_PERCENTAGE);

    private static final EnumSet<MaxSizePolicy> CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES
            = EnumSet.of(ENTRY_COUNT);

    private static final EnumSet<MaxSizePolicy> CACHE_SUPPORTED_NATIVE_MAX_SIZE_POLICIES
            = EnumSet.of(USED_NATIVE_MEMORY_PERCENTAGE,
            FREE_NATIVE_MEMORY_PERCENTAGE, USED_NATIVE_MEMORY_SIZE, FREE_NATIVE_MEMORY_SIZE);

    private ConfigValidator() {
    }

    /**
     * Validates the given {@link MapConfig}.
     *
     * @param mapConfig the {@link MapConfig}
     */
    public static void checkMapConfig(MapConfig mapConfig,
                                      NativeMemoryConfig nativeMemoryConfig,
                                      SplitBrainMergePolicyProvider mergePolicyProvider,
                                      HazelcastProperties properties, ILogger logger) {

        checkNotNativeWhenOpenSource(mapConfig.getInMemoryFormat());
        checkNotBitmapIndexWhenNativeMemory(mapConfig.getInMemoryFormat(), mapConfig.getIndexConfigs());

        if (getBuildInfo().isEnterprise()) {
            checkMapNativeConfig(mapConfig, nativeMemoryConfig);
            warnForDeprecatedHotRestartProp(mapConfig, properties, logger);
        }

        checkMapEvictionConfig(mapConfig.getEvictionConfig());
        checkMapMaxSizePolicyPerInMemoryFormat(mapConfig);
        checkMapMergePolicy(mapConfig,
                mapConfig.getMergePolicyConfig().getPolicy(), mergePolicyProvider);
    }

    static void checkMapMaxSizePolicyPerInMemoryFormat(MapConfig mapConfig) {
        MaxSizePolicy maxSizePolicy = mapConfig.getEvictionConfig().getMaxSizePolicy();
        InMemoryFormat inMemoryFormat = mapConfig.getInMemoryFormat();

        if (inMemoryFormat == NATIVE) {
            if (!MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
                throwNotMatchingMaxSizePolicy(inMemoryFormat, maxSizePolicy, MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES);
            }
        } else {
            if (!MAP_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
                throwNotMatchingMaxSizePolicy(inMemoryFormat, maxSizePolicy, MAP_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES);
            }
        }
    }

    private static void throwNotMatchingMaxSizePolicy(InMemoryFormat inMemoryFormat,
                                                      MaxSizePolicy maxSizePolicy,
                                                      EnumSet<MaxSizePolicy> policies) {
        String msg = "%s is not a valid max size policy to use with"
                + " in memory format %s. Please select an appropriate one from list: %s";
        throw new InvalidConfigurationException(format(msg, maxSizePolicy, inMemoryFormat, policies));
    }

    public static void checkMapEvictionConfig(MaxSizePolicy maxSizePolicy,
                                              EvictionPolicy evictionPolicy,
                                              String comparatorClassName,
                                              Object comparator) {
        checkEvictionConfig(evictionPolicy, comparatorClassName,
                comparator, MAP_SUPPORTED_EVICTION_POLICIES);
        checkMapMaxSizePolicyConfig(maxSizePolicy);
    }

    /**
     * Checks preconditions to create a map proxy.
     *
     * @param mapConfig          the mapConfig
     * @param nativeMemoryConfig the nativeMemoryConfig
     */
    private static void checkMapNativeConfig(MapConfig mapConfig, NativeMemoryConfig nativeMemoryConfig) {
        if (NATIVE != mapConfig.getInMemoryFormat()) {
            return;
        }
        checkTrue(nativeMemoryConfig.isEnabled(),
                format("Enable native memory config to use NATIVE"
                        + " in-memory-format for the map [%s]", mapConfig.getName()));
        checkMapNativeMaxSizePolicy(mapConfig);
    }

    private static void checkMapNativeMaxSizePolicy(MapConfig mapConfig) {
        MaxSizePolicy maxSizePolicy = mapConfig.getEvictionConfig().getMaxSizePolicy();
        if (!MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
            throw new InvalidConfigurationException("Maximum size policy " + maxSizePolicy
                    + " cannot be used with NATIVE in memory format backed Map."
                    + " Supported maximum size policies are: " + MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES);
        }
    }

    private static void warnForDeprecatedHotRestartProp(MapConfig mapConfig,
                                                        HazelcastProperties properties, ILogger logger) {
        HotRestartConfig hotRestartConfig = mapConfig.getHotRestartConfig();
        if (hotRestartConfig == null || !hotRestartConfig.isEnabled()) {
            return;
        }
        HazelcastProperty prop = HOT_RESTART_FREE_NATIVE_MEMORY_PERCENTAGE;
        int hotRestartMinFreeNativeMemoryPercentage = properties.getInteger(prop);
        if (hotRestartMinFreeNativeMemoryPercentage != Integer.parseInt(prop.getDefaultValue())) {
            logger.warning(format("%s was deprecated in version 4.2. By starting from "
                    + "that version setting it has no effect.", prop.getName()));
        }
    }

    public static void warnForUsageOfDeprecatedSymmetricEncryption(Config config, ILogger logger) {
        String warn = "Symmetric encryption is deprecated and will be removed in a future version. Consider using TLS instead.";
        boolean usesAdvancedNetworkConfig = config.getAdvancedNetworkConfig().isEnabled();

        if (config.getNetworkConfig() != null
                && config.getNetworkConfig().getSymmetricEncryptionConfig() != null
                && config.getNetworkConfig().getSymmetricEncryptionConfig().isEnabled()
                && !usesAdvancedNetworkConfig) {
            logger.warning(warn);
        }

        if (config.getAdvancedNetworkConfig() != null
                && config.getAdvancedNetworkConfig().getEndpointConfigs() != null
                && usesAdvancedNetworkConfig) {
            for (EndpointConfig endpointConfig : config.getAdvancedNetworkConfig().getEndpointConfigs().values()) {
                if (endpointConfig.getSymmetricEncryptionConfig() != null
                        && endpointConfig.getSymmetricEncryptionConfig().isEnabled()) {
                    logger.warning(warn);

                    // Write error message once if more than one endpoint use symmetric encryption
                    break;
                }
            }
        }
    }

    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity",
            "checkstyle:booleanexpressioncomplexity"})
    public static void checkAdvancedNetworkConfig(Config config) {
        if (!config.getAdvancedNetworkConfig().isEnabled()) {
            return;
        }

        EnumMap<ProtocolType, MutableInteger> serverSocketsPerProtocolType = new EnumMap<>(ProtocolType.class);
        for (ProtocolType protocolType : ProtocolType.values()) {
            serverSocketsPerProtocolType.put(protocolType, new MutableInteger());
        }

        Map<EndpointQualifier, EndpointConfig> endpointConfigs = config.getAdvancedNetworkConfig().getEndpointConfigs();

        for (EndpointConfig endpointConfig : endpointConfigs.values()) {
            if (endpointConfig instanceof ServerSocketEndpointConfig) {
                serverSocketsPerProtocolType.get(endpointConfig.getProtocolType()).getAndInc();
            }
        }

        for (ProtocolType protocolType : ProtocolType.values()) {
            int serverSocketCount = serverSocketsPerProtocolType.get(protocolType).value;
            if (serverSocketCount > protocolType.getServerSocketCardinality()) {
                throw new InvalidConfigurationException(format("Protocol type %s allows definition "
                                + "of up to %d server sockets but %d were configured", protocolType,
                        protocolType.getServerSocketCardinality(), serverSocketCount));
            }
        }

        // ensure there is 1 MEMBER type server socket
        if (serverSocketsPerProtocolType.get(MEMBER).value != 1) {
            throw new InvalidConfigurationException("A member-server-socket-endpoint"
                    + " configuration is required for the cluster to form.");
        }

        // endpoint qualifiers referenced by WAN publishers must exist
        for (WanReplicationConfig wanReplicationConfig : config.getWanReplicationConfigs().values()) {
            for (WanBatchPublisherConfig wanPublisherConfig : wanReplicationConfig.getBatchPublisherConfigs()) {
                if (wanPublisherConfig.getEndpoint() != null) {
                    EndpointQualifier qualifier = EndpointQualifier.resolve(WAN, wanPublisherConfig.getEndpoint());
                    if (endpointConfigs.get(qualifier) == null) {
                        throw new InvalidConfigurationException(
                                format("WAN publisher config for cluster name '%s' requires an wan-endpoint "
                                                + "config with identifier '%s' but none was found",
                                        wanPublisherConfig.getClusterName(), wanPublisherConfig.getEndpoint()));
                    }
                }
            }
        }
    }


    /**
     * Validates the given {@link CacheSimpleConfig}.
     *
     * @param cacheSimpleConfig   the {@link CacheSimpleConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkCacheConfig(CacheSimpleConfig cacheSimpleConfig,
                                        SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkCacheConfig(cacheSimpleConfig.getInMemoryFormat(), cacheSimpleConfig.getEvictionConfig(),
                cacheSimpleConfig.getMergePolicyConfig().getPolicy(),
                SplitBrainMergeTypes.CacheMergeTypes.class, mergePolicyProvider, COMMONLY_SUPPORTED_EVICTION_POLICIES);
    }

    /**
     * Validates the given {@link CacheConfig}.
     *
     * @param cacheConfig the {@link CacheConfig}
     *                    to check @param mergePolicyProvider the {@link
     *                    SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkCacheConfig(CacheConfig cacheConfig,
                                        SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkCacheConfig(cacheConfig.getInMemoryFormat(), cacheConfig.getEvictionConfig(),
                cacheConfig.getMergePolicyConfig().getPolicy(), SplitBrainMergeTypes.CacheMergeTypes.class,
                mergePolicyProvider, COMMONLY_SUPPORTED_EVICTION_POLICIES);

    }

    /**
     * Validates the given parameters in the context of an {@link ICache} config.
     * According to JSR-107, {@code javax.cache.CacheManager#createCache(String, Configuration)}
     * should throw {@link IllegalArgumentException} in case of invalid configuration.
     * Any {@link InvalidConfigurationException}s thrown from common validation methods
     * are translated to {@link IllegalArgumentException} by this method.
     *
     * @param inMemoryFormat       the {@link InMemoryFormat} of the cache
     * @param evictionConfig       the {@link EvictionConfig} of the cache
     * @param mergePolicyClassname the configured merge policy of the cache
     * @param mergeTypes           the cache merge types
     * @param mergePolicyProvider  the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkCacheConfig(InMemoryFormat inMemoryFormat,
                                        EvictionConfig evictionConfig,
                                        String mergePolicyClassname,
                                        Class<? extends MergingValue> mergeTypes,
                                        SplitBrainMergePolicyProvider mergePolicyProvider,
                                        EnumSet<EvictionPolicy> supportedEvictionPolicies) {
        try {
            ConfigValidator.checkCacheConfig(inMemoryFormat, evictionConfig, supportedEvictionPolicies);
            checkCacheMaxSizePolicy(evictionConfig.getMaxSizePolicy(), inMemoryFormat);
            checkMergeTypeProviderHasRequiredTypes(mergeTypes, mergePolicyProvider, mergePolicyClassname);
        } catch (InvalidConfigurationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Validates the given {@link ReplicatedMapConfig}.
     *
     * @param replicatedMapConfig the {@link ReplicatedMapConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider}
     *                            to resolve merge policy classes
     */
    public static void checkReplicatedMapConfig(ReplicatedMapConfig replicatedMapConfig,
                                                SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.ReplicatedMapMergeTypes.class, mergePolicyProvider,
                replicatedMapConfig.getMergePolicyConfig().getPolicy());
    }

    /**
     * Validates the given {@link MultiMapConfig}.
     *
     * @param multiMapConfig      the {@link MultiMapConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkMultiMapConfig(MultiMapConfig multiMapConfig,
                                           SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.MultiMapMergeTypes.class, mergePolicyProvider,
                multiMapConfig.getMergePolicyConfig().getPolicy());
    }

    /**
     * Validates the given {@link QueueConfig}.
     *
     * @param queueConfig         the {@link QueueConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkQueueConfig(QueueConfig queueConfig,
                                        SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.QueueMergeTypes.class, mergePolicyProvider,
                queueConfig.getMergePolicyConfig().getPolicy());
    }

    /**
     * Validates the given {@link CollectionConfig}.
     *
     * @param collectionConfig    the {@link CollectionConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkCollectionConfig(CollectionConfig collectionConfig,
                                             SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.CollectionMergeTypes.class,
                mergePolicyProvider,
                collectionConfig.getMergePolicyConfig().getPolicy());
    }

    /**
     * Validates the given {@link RingbufferConfig}.
     *
     * @param ringbufferConfig    the {@link RingbufferConfig} to check
     * @param mergePolicyProvider the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkRingbufferConfig(RingbufferConfig ringbufferConfig,
                                             SplitBrainMergePolicyProvider mergePolicyProvider) {
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.RingbufferMergeTypes.class, mergePolicyProvider,
                ringbufferConfig.getMergePolicyConfig().getPolicy());
    }

    /**
     * Validates the given {@link ScheduledExecutorConfig}.
     *
     * @param scheduledExecutorConfig the {@link ScheduledExecutorConfig} to check
     * @param mergePolicyProvider     the {@link SplitBrainMergePolicyProvider} to resolve merge policy classes
     */
    public static void checkScheduledExecutorConfig(ScheduledExecutorConfig scheduledExecutorConfig,
                                                    SplitBrainMergePolicyProvider mergePolicyProvider) {
        String mergePolicyClassName = scheduledExecutorConfig.getMergePolicyConfig().getPolicy();
        checkMergeTypeProviderHasRequiredTypes(SplitBrainMergeTypes.ScheduledExecutorMergeTypes.class,
                mergePolicyProvider, mergePolicyClassName);
    }

    public static void checkCPSubsystemConfig(CPSubsystemConfig config) {
        checkTrue(config.getGroupSize() <= config.getCPMemberCount(),
                "The group size parameter cannot be bigger than the number of the CP member count");

        checkTrue(config.getSessionTimeToLiveSeconds() > config.getSessionHeartbeatIntervalSeconds(),
                "Session TTL must be greater than session heartbeat interval!");

        checkTrue(config.getMissingCPMemberAutoRemovalSeconds() == 0
                        || config.getSessionTimeToLiveSeconds() <= config.getMissingCPMemberAutoRemovalSeconds(),
                "Session TTL must be smaller than or equal to missing CP member auto-removal seconds!");

        checkTrue(!config.isPersistenceEnabled() || config.getCPMemberCount() > 0,
                "CP member count must be greater than 0 to use CP persistence feature!");
    }


    /**
     * Throws {@link InvalidConfigurationException} if the given {@link InMemoryFormat}
     * is {@link InMemoryFormat#NATIVE} and index configurations include {@link IndexType#BITMAP}.
     *
     * @param inMemoryFormat supplied inMemoryFormat
     * @param indexConfigs   {@link List} of {@link IndexConfig}
     */
    private static void checkNotBitmapIndexWhenNativeMemory(InMemoryFormat inMemoryFormat, List<IndexConfig> indexConfigs) {
        if (inMemoryFormat == NATIVE) {
            for (IndexConfig indexConfig : indexConfigs) {
                if (indexConfig.getType() == IndexType.BITMAP) {
                    throw new InvalidConfigurationException("BITMAP indexes are not supported by NATIVE storage");
                }
            }
        }
    }


}
