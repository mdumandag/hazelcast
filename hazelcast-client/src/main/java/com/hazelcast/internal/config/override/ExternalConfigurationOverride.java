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

package com.hazelcast.internal.config.override;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.impl.YamlClientDomConfigProcessor;
import com.hazelcast.config.InvalidConfigurationException;

public class ExternalConfigurationOverride extends ExternalConfigurationOverrideBase{
    public ClientConfig overwriteClientConfig(ClientConfig config) {
        return overwrite(config, (provider, rootNode, target) -> {
                    try {
                        new YamlClientDomConfigProcessor(true, target, false)
                                .buildConfig(new ConfigOverrideElementAdapter(rootNode));
                    } catch (Exception e) {
                        throw new InvalidConfigurationException("failed to overwrite configuration coming from " + provider, e);
                    }
                },
                new EnvConfigProvider(EnvVariablesConfigParser.client()),
                new SystemPropertiesConfigProvider(SystemPropertiesConfigParser.client()));
    }
}
