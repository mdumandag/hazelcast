package com.hazelcast.internal.config;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NearCachePreloaderConfig;
import com.hazelcast.spi.eviction.EvictionPolicyComparator;

import java.util.EnumSet;

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
import static com.hazelcast.config.NearCacheConfig.LocalUpdatePolicy.INVALIDATE;
import static com.hazelcast.instance.BuildInfoProvider.getBuildInfo;
import static com.hazelcast.internal.util.StringUtil.isNullOrEmpty;
import static java.lang.String.format;

public class CommonConfigValidator {

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

    public static void checkMapEvictionConfig(EvictionConfig evictionConfig) {
        EvictionPolicyComparator comparator = evictionConfig.getComparator();
        String comparatorClassName = evictionConfig.getComparatorClassName();
        EvictionPolicy evictionPolicy = evictionConfig.getEvictionPolicy();

        checkComparatorDefinedOnlyOnce(comparatorClassName, comparator);
        checkEvictionPolicyConfiguredOnlyOnce(evictionPolicy, comparatorClassName,
                comparator, MapConfig.DEFAULT_EVICTION_POLICY);

        checkMapMaxSizePolicyConfig(evictionConfig.getMaxSizePolicy());
    }

    static void checkMapMaxSizePolicyConfig(MaxSizePolicy maxSizePolicy) {
        if (!MAP_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES.contains(maxSizePolicy)
                && !MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {

            EnumSet<MaxSizePolicy> allMaxSizePolicies = EnumSet.copyOf(MAP_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES);
            allMaxSizePolicies.addAll(MAP_SUPPORTED_NATIVE_MAX_SIZE_POLICIES);

            String msg = format("IMap eviction config doesn't support max size policy `%s`. "
                    + "Please select a valid one: %s.", maxSizePolicy, allMaxSizePolicies);

            throw new InvalidConfigurationException(msg);
        }
    }

    /**
     * Checks if a {@link EvictionConfig} is valid in its context.
     *
     * @param evictionConfig the {@link EvictionConfig}
     */
    public static void checkCacheEvictionConfig(EvictionConfig evictionConfig) {
        checkEvictionConfig(evictionConfig, COMMONLY_SUPPORTED_EVICTION_POLICIES);
    }

    /**
     * Checks if a {@link EvictionConfig} is valid in its context.
     *
     * @param evictionConfig the {@link EvictionConfig}
     */
    public static void checkEvictionConfig(EvictionConfig evictionConfig,
                                           EnumSet<EvictionPolicy> supportedEvictionPolicies) {
        if (evictionConfig == null) {
            throw new InvalidConfigurationException("Eviction config cannot be null!");
        }

        EvictionPolicy evictionPolicy = evictionConfig.getEvictionPolicy();
        String comparatorClassName = evictionConfig.getComparatorClassName();
        EvictionPolicyComparator comparator = evictionConfig.getComparator();

        checkEvictionConfig(evictionPolicy, comparatorClassName,
                comparator, supportedEvictionPolicies);
    }

    /**
     * Checks if parameters for an {@link EvictionConfig} are valid in their context.
     *
     * @param evictionPolicy      the {@link EvictionPolicy} for the {@link EvictionConfig}
     * @param comparatorClassName the comparator class name for the {@link EvictionConfig}
     * @param comparator          the comparator implementation for the {@link EvictionConfig}
     */
    public static void checkEvictionConfig(EvictionPolicy evictionPolicy,
                                           String comparatorClassName,
                                           Object comparator,
                                           EnumSet<EvictionPolicy> supportedEvictionPolicies) {
        checkComparatorDefinedOnlyOnce(comparatorClassName, comparator);

        if (!supportedEvictionPolicies.contains(evictionPolicy)) {
            if (isNullOrEmpty(comparatorClassName) && comparator == null) {
                String msg = format("Eviction policy `%s` is not supported. Either you can provide a custom one or "
                        + "you can use a supported one: %s.", evictionPolicy, supportedEvictionPolicies);

                throw new InvalidConfigurationException(msg);
            }
        } else {
            checkEvictionPolicyConfiguredOnlyOnce(evictionPolicy, comparatorClassName,
                    comparator, EvictionConfig.DEFAULT_EVICTION_POLICY);
        }
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
     */
    public static void checkCacheConfig(InMemoryFormat inMemoryFormat,
                                        EvictionConfig evictionConfig,
                                        EnumSet<EvictionPolicy> supportedEvictionPolicies) {
        try {
            checkNotNativeWhenOpenSource(inMemoryFormat);
            checkEvictionConfig(evictionConfig, supportedEvictionPolicies);
            checkCacheMaxSizePolicy(evictionConfig.getMaxSizePolicy(), inMemoryFormat);
        } catch (InvalidConfigurationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // package private for testing.
    static void checkCacheMaxSizePolicy(MaxSizePolicy maxSizePolicy,
                                        InMemoryFormat inMemoryFormat) {
        if (inMemoryFormat == NATIVE) {
            if (!CACHE_SUPPORTED_NATIVE_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
                throw new IllegalArgumentException("Maximum size policy " + maxSizePolicy
                        + " cannot be used with NATIVE in memory format backed Cache."
                        + " Supported maximum size policies are: " + CACHE_SUPPORTED_NATIVE_MAX_SIZE_POLICIES);
            }
        } else {
            if (!CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES.contains(maxSizePolicy)) {
                String msg = format("Cache eviction config doesn't support max size policy `%s`. "
                        + "Please select a valid one: %s.", maxSizePolicy, CACHE_SUPPORTED_ON_HEAP_MAX_SIZE_POLICIES);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static void checkCacheConfig(CacheSimpleConfig cacheConfig) {
        checkCacheConfig(cacheConfig.getInMemoryFormat(), cacheConfig.getEvictionConfig(), COMMONLY_SUPPORTED_EVICTION_POLICIES);
    }
}
