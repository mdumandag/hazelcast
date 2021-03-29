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

package com.hazelcast.internal.metrics.impl;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientMetricsConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.MetricsJmxConfig;
import com.hazelcast.internal.metrics.ProbeLevel;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.properties.HazelcastProperties;

import static com.hazelcast.internal.metrics.ProbeLevel.DEBUG;
import static com.hazelcast.internal.metrics.ProbeLevel.INFO;

public class MetricConfigHelper extends MetricsConfigHelperBase {
    private MetricConfigHelper() {
    }

    /**
     * Overrides the {@link ClientMetricsConfig} in the provided {@link ClientConfig}
     * with the metrics system properties if present.
     * See the {@link ClientMetricsConfig} javadoc for the links between metrics
     * configuration fields and the system properties.
     *
     * @param config The configuration
     */
    public static void overrideClientMetricsConfig(ClientConfig config, ILogger logger) {
        ClientMetricsConfig metricsConfig = config.getMetricsConfig();
        MetricsJmxConfig jmxConfig = metricsConfig.getJmxConfig();

        // MetricsConfig.enabled
        tryOverride(ClientProperty.METRICS_ENABLED, config::getProperty,
                prop -> metricsConfig.setEnabled(Boolean.parseBoolean(prop)),
                () -> Boolean.toString(metricsConfig.isEnabled()), "ClientMetricsConfig.enabled", logger);

        // MetricsJmxConfig.enabled
        tryOverride(ClientProperty.METRICS_JMX_ENABLED, config::getProperty,
                prop -> jmxConfig.setEnabled(Boolean.parseBoolean(prop)),
                () -> Boolean.toString(jmxConfig.isEnabled()), "MetricsJmxConfig.enabled", logger);

        // MetricsConfig.collectionFrequencySeconds
        tryOverride(ClientProperty.METRICS_COLLECTION_FREQUENCY, config::getProperty,
                prop -> metricsConfig.setCollectionFrequencySeconds(Integer.parseInt(prop)),
                () -> Integer.toString(metricsConfig.getCollectionFrequencySeconds()),
                "ClientMetricsConfig.collectionFrequencySeconds", logger);
    }

    public static ProbeLevel clientMetricsLevel(HazelcastProperties properties, ILogger logger) {
        boolean debugMetrics = properties.getBoolean(ClientProperty.METRICS_DEBUG);
        ProbeLevel probeLevel = debugMetrics ? DEBUG : INFO;

        if (probeLevel == INFO) {
            logger.fine("Collecting debug metrics and sending to diagnostics is disabled");
        } else {
            logger.info("Collecting debug metrics and sending to diagnostics is enabled");
        }

        return probeLevel;
    }

}
