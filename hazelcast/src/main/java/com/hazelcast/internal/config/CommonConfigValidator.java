package com.hazelcast.internal.config;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NearCachePreloaderConfig;

import java.util.EnumSet;

import static com.hazelcast.config.InMemoryFormat.NATIVE;
import static com.hazelcast.config.NearCacheConfig.LocalUpdatePolicy.INVALIDATE;
import static com.hazelcast.instance.BuildInfoProvider.getBuildInfo;
import static com.hazelcast.internal.util.StringUtil.isNullOrEmpty;
import static java.lang.String.format;

public class CommonConfigValidator {

    static final EnumSet<MaxSizePolicy> NEAR_CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES
            = EnumSet.of(MaxSizePolicy.ENTRY_COUNT);

    /**
     * Checks preconditions to create a map proxy with Near Cache.
     *
     * @param mapName            name of the map that Near Cache will be created for
     * @param nearCacheConfig    the {@link NearCacheConfig} to be checked
     * @param nativeMemoryConfig the {@link NativeMemoryConfig} of the Hazelcast instance
     * @param isClient           {@code true} if the config is for a Hazelcast client, {@code false} otherwise
     */
    public static void checkNearCacheConfig(String mapName, NearCacheConfig nearCacheConfig,
                                            NativeMemoryConfig nativeMemoryConfig, boolean isClient) {
        checkNotNativeWhenOpenSource(nearCacheConfig.getInMemoryFormat());
        checkLocalUpdatePolicy(mapName, nearCacheConfig.getLocalUpdatePolicy());
        EvictionConfig evictionConfig = nearCacheConfig.getEvictionConfig();
        checkNearCacheEvictionConfig(evictionConfig.getEvictionPolicy(),
                evictionConfig.getComparatorClassName(), evictionConfig.getComparator());
        checkOnHeapNearCacheMaxSizePolicy(nearCacheConfig);
        checkNearCacheNativeMemoryConfig(nearCacheConfig.getInMemoryFormat(),
                nativeMemoryConfig, getBuildInfo().isEnterprise());

        if (isClient && nearCacheConfig.isCacheLocalEntries()) {
            throw new InvalidConfigurationException("The Near Cache option `cache-local-entries` is not supported in "
                    + "client configurations.");
        }
        checkPreloaderConfig(nearCacheConfig, isClient);
    }

    /**
     * Throws {@link InvalidConfigurationException} if the given {@link NearCacheConfig}
     * has an invalid {@link NearCachePreloaderConfig}.
     *
     * @param nearCacheConfig supplied NearCacheConfig
     * @param isClient        {@code true} if the config is for a Hazelcast client, {@code false} otherwise
     */
    private static void checkPreloaderConfig(NearCacheConfig nearCacheConfig, boolean isClient) {
        if (!isClient && nearCacheConfig.getPreloaderConfig().isEnabled()) {
            throw new InvalidConfigurationException("The Near Cache pre-loader is just available on Hazelcast clients!");
        }
    }

    /**
     * Throws {@link InvalidConfigurationException} if the given {@link InMemoryFormat}
     * is {@link InMemoryFormat#NATIVE} and Hazelcast is OS.
     *
     * @param inMemoryFormat supplied inMemoryFormat
     */
    protected static void checkNotNativeWhenOpenSource(InMemoryFormat inMemoryFormat) {
        if (inMemoryFormat == NATIVE && !getBuildInfo().isEnterprise()) {
            throw new InvalidConfigurationException("NATIVE storage format is supported in Hazelcast Enterprise only."
                    + " Make sure you have Hazelcast Enterprise JARs on your classpath!");
        }
    }


    /**
     * Checks IMap's supported Near Cache local update policy configuration.
     *
     * @param mapName           name of the map that Near Cache will be created for
     * @param localUpdatePolicy local update policy
     */
    private static void checkLocalUpdatePolicy(String mapName, NearCacheConfig.LocalUpdatePolicy localUpdatePolicy) {
        if (localUpdatePolicy != INVALIDATE) {
            throw new InvalidConfigurationException(format("Wrong `local-update-policy`"
                    + " option is selected for `%s` map Near Cache."
                    + " Only `%s` option is supported but found `%s`", mapName, INVALIDATE, localUpdatePolicy));
        }
    }

    public static void checkNearCacheEvictionConfig(EvictionPolicy evictionPolicy,
                                                    String comparatorClassName,
                                                    Object comparator) {
        checkComparatorDefinedOnlyOnce(comparatorClassName, comparator);
        checkEvictionPolicyConfiguredOnlyOnce(evictionPolicy, comparatorClassName,
                comparator, EvictionConfig.DEFAULT_EVICTION_POLICY);
    }

    protected static void checkComparatorDefinedOnlyOnce(String comparatorClassName, Object comparator) {
        if (comparatorClassName != null && comparator != null) {
            throw new InvalidConfigurationException("Only one of the `comparator class name` and `comparator`"
                    + " can be configured in the eviction configuration!");
        }
    }

    protected static void checkEvictionPolicyConfiguredOnlyOnce(EvictionPolicy evictionPolicy,
                                                                String comparatorClassName,
                                                                Object comparator, EvictionPolicy defaultEvictionPolicy) {
        if (evictionPolicy != defaultEvictionPolicy) {
            if (!isNullOrEmpty(comparatorClassName)) {
                throw new InvalidConfigurationException(
                        "Only one of the `eviction policy` and `comparator class name` can be configured!");
            }
            if (comparator != null) {
                throw new InvalidConfigurationException("Only one of the `eviction policy` and `comparator` can be configured!");
            }
        }
    }

    private static void checkOnHeapNearCacheMaxSizePolicy(NearCacheConfig nearCacheConfig) {
        InMemoryFormat inMemoryFormat = nearCacheConfig.getInMemoryFormat();
        if (inMemoryFormat == NATIVE) {
            return;
        }

        MaxSizePolicy maxSizePolicy = nearCacheConfig.getEvictionConfig().getMaxSizePolicy();
        if (!NEAR_CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
            throw new InvalidConfigurationException(format("Near Cache maximum size policy %s cannot be used with %s storage."
                            + " Supported maximum size policies are: %s",
                    maxSizePolicy, inMemoryFormat, NEAR_CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES));
        }
    }

    /**
     * Checks precondition to use {@link InMemoryFormat#NATIVE}.
     *
     * @param inMemoryFormat     the {@link InMemoryFormat} of the Near Cache
     * @param nativeMemoryConfig the {@link NativeMemoryConfig} of the Hazelcast instance
     * @param isEnterprise       {@code true} if the Hazelcast instance is EE, {@code false} otherwise
     */
    protected static void checkNearCacheNativeMemoryConfig(InMemoryFormat inMemoryFormat, NativeMemoryConfig nativeMemoryConfig,
                                                           boolean isEnterprise) {
        if (!isEnterprise) {
            return;
        }
        if (inMemoryFormat != NATIVE) {
            return;
        }
        if (nativeMemoryConfig != null && nativeMemoryConfig.isEnabled()) {
            return;
        }
        throw new InvalidConfigurationException("Enable native memory config to use NATIVE in-memory-format for Near Cache");
    }

}
