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

package com.hazelcast.sql.impl;

import com.hazelcast.sql.HazelcastSqlException;

import java.util.UUID;

public class QueryUtilsBase {
    public static HazelcastSqlException toPublicException(Throwable e, UUID localMemberId) {
        if (e instanceof HazelcastSqlException) {
            return (HazelcastSqlException) e;
        }

        if (e instanceof QueryException) {
            QueryException e0 = (QueryException) e;

            UUID originatingMemberId = e0.getOriginatingMemberId();

            if (originatingMemberId == null) {
                originatingMemberId = localMemberId;
            }

            return new HazelcastSqlException(originatingMemberId, e0.getCode(), e0.getMessage(), e);
        } else {
            return new HazelcastSqlException(localMemberId, SqlErrorCode.GENERIC, e.getMessage(), e);
        }
    }
}
