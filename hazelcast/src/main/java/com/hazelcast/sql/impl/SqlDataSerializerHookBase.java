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

import com.hazelcast.internal.serialization.impl.FactoryIdHelper;

import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.SQL_DS_FACTORY;
import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.SQL_DS_FACTORY_ID;

/**
 * Serialization hook for SQL classes.
 */
public class SqlDataSerializerHookBase {

    public static final int F_ID = FactoryIdHelper.getFactoryId(SQL_DS_FACTORY, SQL_DS_FACTORY_ID);

    public static final int QUERY_DATA_TYPE = 0;

    public static final int QUERY_ID = 1;

    public static final int ROW_HEAP = 2;
    public static final int ROW_JOIN = 3;
    public static final int ROW_EMPTY = 4;
    public static final int ROW_BATCH_LIST = 5;
    public static final int ROW_BATCH_EMPTY = 6;

    public static final int OPERATION_EXECUTE = 7;
    public static final int OPERATION_EXECUTE_FRAGMENT = 8;
    public static final int OPERATION_BATCH = 9;
    public static final int OPERATION_FLOW_CONTROL = 10;
    public static final int OPERATION_CANCEL = 11;
    public static final int OPERATION_CHECK = 12;
    public static final int OPERATION_CHECK_RESPONSE = 13;

    public static final int NODE_ROOT = 14;
    public static final int NODE_SEND = 15;
    public static final int NODE_RECEIVE = 16;
    public static final int NODE_PROJECT = 17;
    public static final int NODE_FILTER = 18;
    public static final int NODE_MAP_SCAN = 19;

    public static final int EXPRESSION_COLUMN = 20;
    public static final int EXPRESSION_IS_NULL = 21;

    public static final int TARGET_DESCRIPTOR_GENERIC = 22;

    public static final int QUERY_PATH = 23;

    public static final int EXPRESSION_CONSTANT = 24;
    public static final int EXPRESSION_PARAMETER = 25;
    public static final int EXPRESSION_CAST = 26;
    public static final int EXPRESSION_DIVIDE = 27;
    public static final int EXPRESSION_MINUS = 28;
    public static final int EXPRESSION_MULTIPLY = 29;
    public static final int EXPRESSION_PLUS = 30;
    public static final int EXPRESSION_UNARY_MINUS = 31;
    public static final int EXPRESSION_AND = 32;
    public static final int EXPRESSION_OR = 33;
    public static final int EXPRESSION_NOT = 34;
    public static final int EXPRESSION_COMPARISON = 35;
    public static final int EXPRESSION_IS_TRUE = 36;
    public static final int EXPRESSION_IS_NOT_TRUE = 37;
    public static final int EXPRESSION_IS_FALSE = 38;
    public static final int EXPRESSION_IS_NOT_FALSE = 39;
    public static final int EXPRESSION_IS_NOT_NULL = 40;

    public static final int EXPRESSION_ABS = 41;
    public static final int EXPRESSION_SIGN = 42;
    public static final int EXPRESSION_RAND = 43;
    public static final int EXPRESSION_DOUBLE = 44;
    public static final int EXPRESSION_FLOOR_CEIL = 45;
    public static final int EXPRESSION_ROUND_TRUNCATE = 46;

    public static final int NODE_EMPTY = 47;

    public static final int INDEX_FILTER_VALUE = 48;
    public static final int INDEX_FILTER_EQUALS = 49;
    public static final int INDEX_FILTER_RANGE = 50;
    public static final int INDEX_FILTER_IN = 51;

    public static final int NODE_MAP_INDEX_SCAN = 52;

    public static final int EXPRESSION_ASCII = 53;
    public static final int EXPRESSION_CHAR_LENGTH = 54;
    public static final int EXPRESSION_INITCAP = 55;
    public static final int EXPRESSION_LOWER = 56;
    public static final int EXPRESSION_UPPER = 57;
    public static final int EXPRESSION_CONCAT = 58;
    public static final int EXPRESSION_LIKE = 59;
    public static final int EXPRESSION_SUBSTRING = 60;
    public static final int EXPRESSION_TRIM = 61;

    public static final int NODE_RECEIVE_MERGE_SORT = 62;
    public static final int NODE_FETCH = 63;

    public static final int EXPRESSION_REMAINDER = 64;

    public static final int LAZY_TARGET = 65;

    public static final int EXPRESSION_DOUBLE_DOUBLE = 66;

    public static final int INTERVAL_YEAR_MONTH = 67;
    public static final int INTERVAL_DAY_SECOND = 68;

    public static final int EXPRESSION_REPLACE = 69;
    public static final int LEN = EXPRESSION_REPLACE + 1;

}
