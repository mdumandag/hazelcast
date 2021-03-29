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

package com.hazelcast.cp.internal;


import static com.hazelcast.cp.CPGroup.DEFAULT_GROUP_NAME;
import static com.hazelcast.internal.util.Preconditions.checkTrue;

public class RaftNameUtil {

    public static String getObjectNameForProxy(String name) {
        int i = name.indexOf("@");
        if (i == -1) {
            return name;
        }

        checkTrue(i < (name.length() - 1), "Object name cannot be empty string");
        checkTrue(name.indexOf("@", i + 1) == -1,
                "Custom CP group name must be specified at most once");
        String objectName = name.substring(0, i).trim();
        checkTrue(objectName.length() > 0, "Object name cannot be empty string");
        return objectName;
    }

    public static String withoutDefaultGroupName(String name) {
        name = name.trim();
        int i = name.indexOf("@");
        if (i == -1) {
            return name;
        }

        checkTrue(name.indexOf("@", i + 1) == -1, "Custom group name must be specified at most once");
        String groupName = name.substring(i + 1).trim();
        if (groupName.equalsIgnoreCase(DEFAULT_GROUP_NAME)) {
            return name.substring(0, i);
        }

        return name;
    }

}
