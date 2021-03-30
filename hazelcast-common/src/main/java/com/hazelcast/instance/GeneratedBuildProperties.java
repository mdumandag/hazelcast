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

package com.hazelcast.instance;

/**
 * This class is generated in a build-time from a template stored at
 * src/main/template/com/hazelcast/instance/GeneratedBuildProperties.
 *
 * Do not edit by hand as the changes will be overwritten in the next build.
 *
 * We used to have the version info as property file, but this caused issues
 * in on environments with a complicated classloading model. Having the info
 * as a Java class provide a better control when you have multiple version of
 * Hazelcast deployed.
 *
 * WARNING: DO NOT CHANGE FIELD NAMES IN THE TEMPLATE.
 * The fields are read via reflection at {@link com.hazelcast.instance.BuildInfoProvider}
 *
 */
public final class GeneratedBuildProperties {
    public static final String VERSION = "5.0-SNAPSHOT";
    public static final String BUILD = "20210330";
    public static final String REVISION = "d69ea2e";
    public static final String COMMIT_ID = "d69ea2e435857fe044ca8d2c1b0ceb514cc2eb09";
    public static final String DISTRIBUTION = "Hazelcast";
    public static final String SERIALIZATION_VERSION = "1";

    private GeneratedBuildProperties() {
    }
}
