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

package com.hazelcast.client.protocol.compatibility;

import com.hazelcast.client.HazelcastClientUtil;
import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.ClientMessageReader;
import com.hazelcast.client.impl.protocol.codec.*;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hazelcast.client.impl.protocol.ClientMessage.IS_FINAL_FLAG;
import static com.hazelcast.client.protocol.compatibility.ReferenceObjects.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class MemberCompatibilityTest_2_1 {
    private List<ClientMessage> clientMessages = new ArrayList<>();

    @Before
    public void setUp() throws IOException {
        File file = new File(getClass().getResource("/2.1.protocol.compatibility.binary").getFile());
        InputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        inputStream.read(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        ClientMessageReader reader = new ClientMessageReader(0);
        while (reader.readFrom(buffer, true)) {
            clientMessages.add(reader.getClientMessage());
            reader.reset();
        }
    }

    @Test
    public void test_ClientAuthenticationCodec_decodeRequest() {
        int fileClientMessageIndex = 0;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerClientAuthenticationCodec.RequestParameters parameters = ServerClientAuthenticationCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.clusterName));
        assertTrue(isEqual(aString, parameters.username));
        assertTrue(isEqual(aString, parameters.password));
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aString, parameters.clientType));
        assertTrue(isEqual(aByte, parameters.serializationVersion));
        assertTrue(isEqual(aString, parameters.clientHazelcastVersion));
        assertTrue(isEqual(aString, parameters.clientName));
        assertTrue(isEqual(aListOfStrings, parameters.labels));
    }

    @Test
    public void test_ClientAuthenticationCodec_encodeResponse() {
        int fileClientMessageIndex = 1;
        ClientMessage encoded = ServerClientAuthenticationCodec.encodeResponse(aByte, anAddress, aUUID, aByte, aString, anInt, aUUID, aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAuthenticationCustomCodec_decodeRequest() {
        int fileClientMessageIndex = 2;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerClientAuthenticationCustomCodec.RequestParameters parameters = ServerClientAuthenticationCustomCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.clusterName));
        assertTrue(isEqual(aByteArray, parameters.credentials));
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aString, parameters.clientType));
        assertTrue(isEqual(aByte, parameters.serializationVersion));
        assertTrue(isEqual(aString, parameters.clientHazelcastVersion));
        assertTrue(isEqual(aString, parameters.clientName));
        assertTrue(isEqual(aListOfStrings, parameters.labels));
    }

    @Test
    public void test_ClientAuthenticationCustomCodec_encodeResponse() {
        int fileClientMessageIndex = 3;
        ClientMessage encoded = ServerClientAuthenticationCustomCodec.encodeResponse(aByte, anAddress, aUUID, aByte, aString, anInt, aUUID, aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddClusterViewListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 4;
    }

    @Test
    public void test_ClientAddClusterViewListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 5;
        ClientMessage encoded = ServerClientAddClusterViewListenerCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddClusterViewListenerCodec_encodeMembersViewEvent() {
        int fileClientMessageIndex = 6;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerClientAddClusterViewListenerCodec.encodeMembersViewEvent(anInt, aListOfMemberInfos);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddClusterViewListenerCodec_encodePartitionsViewEvent() {
        int fileClientMessageIndex = 7;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerClientAddClusterViewListenerCodec.encodePartitionsViewEvent(anInt, aListOfUUIDToListOfIntegers);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientCreateProxyCodec_decodeRequest() {
        int fileClientMessageIndex = 8;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerClientCreateProxyCodec.RequestParameters parameters = ServerClientCreateProxyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.serviceName));
    }

    @Test
    public void test_ClientCreateProxyCodec_encodeResponse() {
        int fileClientMessageIndex = 9;
        ClientMessage encoded = ServerClientCreateProxyCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientDestroyProxyCodec_decodeRequest() {
        int fileClientMessageIndex = 10;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerClientDestroyProxyCodec.RequestParameters parameters = ServerClientDestroyProxyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.serviceName));
    }

    @Test
    public void test_ClientDestroyProxyCodec_encodeResponse() {
        int fileClientMessageIndex = 11;
        ClientMessage encoded = ServerClientDestroyProxyCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddPartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 12;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aBoolean, ServerClientAddPartitionLostListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientAddPartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 13;
        ClientMessage encoded = ServerClientAddPartitionLostListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddPartitionLostListenerCodec_encodePartitionLostEvent() {
        int fileClientMessageIndex = 14;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerClientAddPartitionLostListenerCodec.encodePartitionLostEvent(anInt, anInt, aUUID);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientRemovePartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 15;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerClientRemovePartitionLostListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientRemovePartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 16;
        ClientMessage encoded = ServerClientRemovePartitionLostListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientGetDistributedObjectsCodec_decodeRequest() {
        int fileClientMessageIndex = 17;
    }

    @Test
    public void test_ClientGetDistributedObjectsCodec_encodeResponse() {
        int fileClientMessageIndex = 18;
        ClientMessage encoded = ServerClientGetDistributedObjectsCodec.encodeResponse(aListOfDistributedObjectInfo);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddDistributedObjectListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 19;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aBoolean, ServerClientAddDistributedObjectListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientAddDistributedObjectListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 20;
        ClientMessage encoded = ServerClientAddDistributedObjectListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientAddDistributedObjectListenerCodec_encodeDistributedObjectEvent() {
        int fileClientMessageIndex = 21;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerClientAddDistributedObjectListenerCodec.encodeDistributedObjectEvent(aString, aString, aString, aUUID);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientRemoveDistributedObjectListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 22;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerClientRemoveDistributedObjectListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientRemoveDistributedObjectListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 23;
        ClientMessage encoded = ServerClientRemoveDistributedObjectListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientPingCodec_decodeRequest() {
        int fileClientMessageIndex = 24;
    }

    @Test
    public void test_ClientPingCodec_encodeResponse() {
        int fileClientMessageIndex = 25;
        ClientMessage encoded = ServerClientPingCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientStatisticsCodec_decodeRequest() {
        int fileClientMessageIndex = 26;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerClientStatisticsCodec.RequestParameters parameters = ServerClientStatisticsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aLong, parameters.timestamp));
        assertTrue(isEqual(aString, parameters.clientAttributes));
        assertTrue(isEqual(aByteArray, parameters.metricsBlob));
    }

    @Test
    public void test_ClientStatisticsCodec_encodeResponse() {
        int fileClientMessageIndex = 27;
        ClientMessage encoded = ServerClientStatisticsCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientDeployClassesCodec_decodeRequest() {
        int fileClientMessageIndex = 28;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aListOfStringToByteArray, ServerClientDeployClassesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientDeployClassesCodec_encodeResponse() {
        int fileClientMessageIndex = 29;
        ClientMessage encoded = ServerClientDeployClassesCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientCreateProxiesCodec_decodeRequest() {
        int fileClientMessageIndex = 30;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aListOfStringToString, ServerClientCreateProxiesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ClientCreateProxiesCodec_encodeResponse() {
        int fileClientMessageIndex = 31;
        ClientMessage encoded = ServerClientCreateProxiesCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientLocalBackupListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 32;
    }

    @Test
    public void test_ClientLocalBackupListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 33;
        ClientMessage encoded = ServerClientLocalBackupListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientLocalBackupListenerCodec_encodeBackupEvent() {
        int fileClientMessageIndex = 34;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerClientLocalBackupListenerCodec.encodeBackupEvent(aLong);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ClientTriggerPartitionAssignmentCodec_decodeRequest() {
        int fileClientMessageIndex = 35;
    }

    @Test
    public void test_ClientTriggerPartitionAssignmentCodec_encodeResponse() {
        int fileClientMessageIndex = 36;
        ClientMessage encoded = ServerClientTriggerPartitionAssignmentCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutCodec_decodeRequest() {
        int fileClientMessageIndex = 37;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutCodec.RequestParameters parameters = ServerMapPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_MapPutCodec_encodeResponse() {
        int fileClientMessageIndex = 38;
        ClientMessage encoded = ServerMapPutCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapGetCodec_decodeRequest() {
        int fileClientMessageIndex = 39;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapGetCodec.RequestParameters parameters = ServerMapGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapGetCodec_encodeResponse() {
        int fileClientMessageIndex = 40;
        ClientMessage encoded = ServerMapGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 41;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemoveCodec.RequestParameters parameters = ServerMapRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 42;
        ClientMessage encoded = ServerMapRemoveCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapReplaceCodec_decodeRequest() {
        int fileClientMessageIndex = 43;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapReplaceCodec.RequestParameters parameters = ServerMapReplaceCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapReplaceCodec_encodeResponse() {
        int fileClientMessageIndex = 44;
        ClientMessage encoded = ServerMapReplaceCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapReplaceIfSameCodec_decodeRequest() {
        int fileClientMessageIndex = 45;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapReplaceIfSameCodec.RequestParameters parameters = ServerMapReplaceIfSameCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.testValue));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapReplaceIfSameCodec_encodeResponse() {
        int fileClientMessageIndex = 46;
        ClientMessage encoded = ServerMapReplaceIfSameCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapContainsKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 47;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapContainsKeyCodec.RequestParameters parameters = ServerMapContainsKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapContainsKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 48;
        ClientMessage encoded = ServerMapContainsKeyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapContainsValueCodec_decodeRequest() {
        int fileClientMessageIndex = 49;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapContainsValueCodec.RequestParameters parameters = ServerMapContainsValueCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_MapContainsValueCodec_encodeResponse() {
        int fileClientMessageIndex = 50;
        ClientMessage encoded = ServerMapContainsValueCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemoveIfSameCodec_decodeRequest() {
        int fileClientMessageIndex = 51;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemoveIfSameCodec.RequestParameters parameters = ServerMapRemoveIfSameCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapRemoveIfSameCodec_encodeResponse() {
        int fileClientMessageIndex = 52;
        ClientMessage encoded = ServerMapRemoveIfSameCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapDeleteCodec_decodeRequest() {
        int fileClientMessageIndex = 53;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapDeleteCodec.RequestParameters parameters = ServerMapDeleteCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapDeleteCodec_encodeResponse() {
        int fileClientMessageIndex = 54;
        ClientMessage encoded = ServerMapDeleteCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapFlushCodec_decodeRequest() {
        int fileClientMessageIndex = 55;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapFlushCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapFlushCodec_encodeResponse() {
        int fileClientMessageIndex = 56;
        ClientMessage encoded = ServerMapFlushCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapTryRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 57;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapTryRemoveCodec.RequestParameters parameters = ServerMapTryRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_MapTryRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 58;
        ClientMessage encoded = ServerMapTryRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapTryPutCodec_decodeRequest() {
        int fileClientMessageIndex = 59;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapTryPutCodec.RequestParameters parameters = ServerMapTryPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_MapTryPutCodec_encodeResponse() {
        int fileClientMessageIndex = 60;
        ClientMessage encoded = ServerMapTryPutCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutTransientCodec_decodeRequest() {
        int fileClientMessageIndex = 61;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutTransientCodec.RequestParameters parameters = ServerMapPutTransientCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_MapPutTransientCodec_encodeResponse() {
        int fileClientMessageIndex = 62;
        ClientMessage encoded = ServerMapPutTransientCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutIfAbsentCodec_decodeRequest() {
        int fileClientMessageIndex = 63;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutIfAbsentCodec.RequestParameters parameters = ServerMapPutIfAbsentCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_MapPutIfAbsentCodec_encodeResponse() {
        int fileClientMessageIndex = 64;
        ClientMessage encoded = ServerMapPutIfAbsentCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapSetCodec_decodeRequest() {
        int fileClientMessageIndex = 65;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapSetCodec.RequestParameters parameters = ServerMapSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_MapSetCodec_encodeResponse() {
        int fileClientMessageIndex = 66;
        ClientMessage encoded = ServerMapSetCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapLockCodec_decodeRequest() {
        int fileClientMessageIndex = 67;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapLockCodec.RequestParameters parameters = ServerMapLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MapLockCodec_encodeResponse() {
        int fileClientMessageIndex = 68;
        ClientMessage encoded = ServerMapLockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapTryLockCodec_decodeRequest() {
        int fileClientMessageIndex = 69;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapTryLockCodec.RequestParameters parameters = ServerMapTryLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.lease));
        assertTrue(isEqual(aLong, parameters.timeout));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MapTryLockCodec_encodeResponse() {
        int fileClientMessageIndex = 70;
        ClientMessage encoded = ServerMapTryLockCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapIsLockedCodec_decodeRequest() {
        int fileClientMessageIndex = 71;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapIsLockedCodec.RequestParameters parameters = ServerMapIsLockedCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_MapIsLockedCodec_encodeResponse() {
        int fileClientMessageIndex = 72;
        ClientMessage encoded = ServerMapIsLockedCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapUnlockCodec_decodeRequest() {
        int fileClientMessageIndex = 73;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapUnlockCodec.RequestParameters parameters = ServerMapUnlockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MapUnlockCodec_encodeResponse() {
        int fileClientMessageIndex = 74;
        ClientMessage encoded = ServerMapUnlockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddInterceptorCodec_decodeRequest() {
        int fileClientMessageIndex = 75;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddInterceptorCodec.RequestParameters parameters = ServerMapAddInterceptorCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.interceptor));
    }

    @Test
    public void test_MapAddInterceptorCodec_encodeResponse() {
        int fileClientMessageIndex = 76;
        ClientMessage encoded = ServerMapAddInterceptorCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemoveInterceptorCodec_decodeRequest() {
        int fileClientMessageIndex = 77;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemoveInterceptorCodec.RequestParameters parameters = ServerMapRemoveInterceptorCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.id));
    }

    @Test
    public void test_MapRemoveInterceptorCodec_encodeResponse() {
        int fileClientMessageIndex = 78;
        ClientMessage encoded = ServerMapRemoveInterceptorCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerToKeyWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 79;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddEntryListenerToKeyWithPredicateCodec.RequestParameters parameters = ServerMapAddEntryListenerToKeyWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(anInt, parameters.listenerFlags));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddEntryListenerToKeyWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 80;
        ClientMessage encoded = ServerMapAddEntryListenerToKeyWithPredicateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerToKeyWithPredicateCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 81;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddEntryListenerToKeyWithPredicateCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 82;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddEntryListenerWithPredicateCodec.RequestParameters parameters = ServerMapAddEntryListenerWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(anInt, parameters.listenerFlags));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddEntryListenerWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 83;
        ClientMessage encoded = ServerMapAddEntryListenerWithPredicateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerWithPredicateCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 84;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddEntryListenerWithPredicateCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerToKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 85;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddEntryListenerToKeyCodec.RequestParameters parameters = ServerMapAddEntryListenerToKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(anInt, parameters.listenerFlags));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddEntryListenerToKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 86;
        ClientMessage encoded = ServerMapAddEntryListenerToKeyCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerToKeyCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 87;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddEntryListenerToKeyCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 88;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddEntryListenerCodec.RequestParameters parameters = ServerMapAddEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(anInt, parameters.listenerFlags));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 89;
        ClientMessage encoded = ServerMapAddEntryListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddEntryListenerCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 90;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddEntryListenerCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemoveEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 91;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemoveEntryListenerCodec.RequestParameters parameters = ServerMapRemoveEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_MapRemoveEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 92;
        ClientMessage encoded = ServerMapRemoveEntryListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddPartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 93;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddPartitionLostListenerCodec.RequestParameters parameters = ServerMapAddPartitionLostListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddPartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 94;
        ClientMessage encoded = ServerMapAddPartitionLostListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddPartitionLostListenerCodec_encodeMapPartitionLostEvent() {
        int fileClientMessageIndex = 95;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddPartitionLostListenerCodec.encodeMapPartitionLostEvent(anInt, aUUID);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemovePartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 96;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemovePartitionLostListenerCodec.RequestParameters parameters = ServerMapRemovePartitionLostListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_MapRemovePartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 97;
        ClientMessage encoded = ServerMapRemovePartitionLostListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapGetEntryViewCodec_decodeRequest() {
        int fileClientMessageIndex = 98;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapGetEntryViewCodec.RequestParameters parameters = ServerMapGetEntryViewCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapGetEntryViewCodec_encodeResponse() {
        int fileClientMessageIndex = 99;
        ClientMessage encoded = ServerMapGetEntryViewCodec.encodeResponse(aSimpleEntryView, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEvictCodec_decodeRequest() {
        int fileClientMessageIndex = 100;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapEvictCodec.RequestParameters parameters = ServerMapEvictCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapEvictCodec_encodeResponse() {
        int fileClientMessageIndex = 101;
        ClientMessage encoded = ServerMapEvictCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEvictAllCodec_decodeRequest() {
        int fileClientMessageIndex = 102;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapEvictAllCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapEvictAllCodec_encodeResponse() {
        int fileClientMessageIndex = 103;
        ClientMessage encoded = ServerMapEvictAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapLoadAllCodec_decodeRequest() {
        int fileClientMessageIndex = 104;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapLoadAllCodec.RequestParameters parameters = ServerMapLoadAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.replaceExistingValues));
    }

    @Test
    public void test_MapLoadAllCodec_encodeResponse() {
        int fileClientMessageIndex = 105;
        ClientMessage encoded = ServerMapLoadAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapLoadGivenKeysCodec_decodeRequest() {
        int fileClientMessageIndex = 106;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapLoadGivenKeysCodec.RequestParameters parameters = ServerMapLoadGivenKeysCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
        assertTrue(isEqual(aBoolean, parameters.replaceExistingValues));
    }

    @Test
    public void test_MapLoadGivenKeysCodec_encodeResponse() {
        int fileClientMessageIndex = 107;
        ClientMessage encoded = ServerMapLoadGivenKeysCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapKeySetCodec_decodeRequest() {
        int fileClientMessageIndex = 108;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapKeySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapKeySetCodec_encodeResponse() {
        int fileClientMessageIndex = 109;
        ClientMessage encoded = ServerMapKeySetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapGetAllCodec_decodeRequest() {
        int fileClientMessageIndex = 110;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapGetAllCodec.RequestParameters parameters = ServerMapGetAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
    }

    @Test
    public void test_MapGetAllCodec_encodeResponse() {
        int fileClientMessageIndex = 111;
        ClientMessage encoded = ServerMapGetAllCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapValuesCodec_decodeRequest() {
        int fileClientMessageIndex = 112;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapValuesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapValuesCodec_encodeResponse() {
        int fileClientMessageIndex = 113;
        ClientMessage encoded = ServerMapValuesCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEntrySetCodec_decodeRequest() {
        int fileClientMessageIndex = 114;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapEntrySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapEntrySetCodec_encodeResponse() {
        int fileClientMessageIndex = 115;
        ClientMessage encoded = ServerMapEntrySetCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapKeySetWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 116;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapKeySetWithPredicateCodec.RequestParameters parameters = ServerMapKeySetWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapKeySetWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 117;
        ClientMessage encoded = ServerMapKeySetWithPredicateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapValuesWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 118;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapValuesWithPredicateCodec.RequestParameters parameters = ServerMapValuesWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapValuesWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 119;
        ClientMessage encoded = ServerMapValuesWithPredicateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEntriesWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 120;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapEntriesWithPredicateCodec.RequestParameters parameters = ServerMapEntriesWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapEntriesWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 121;
        ClientMessage encoded = ServerMapEntriesWithPredicateCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddIndexCodec_decodeRequest() {
        int fileClientMessageIndex = 122;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddIndexCodec.RequestParameters parameters = ServerMapAddIndexCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anIndexConfig, parameters.indexConfig));
    }

    @Test
    public void test_MapAddIndexCodec_encodeResponse() {
        int fileClientMessageIndex = 123;
        ClientMessage encoded = ServerMapAddIndexCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 124;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 125;
        ClientMessage encoded = ServerMapSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 126;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapIsEmptyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 127;
        ClientMessage encoded = ServerMapIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutAllCodec_decodeRequest() {
        int fileClientMessageIndex = 128;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutAllCodec.RequestParameters parameters = ServerMapPutAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfDataToData, parameters.entries));
        assertTrue(parameters.isTriggerMapLoaderExists);
        assertTrue(isEqual(aBoolean, parameters.triggerMapLoader));
    }

    @Test
    public void test_MapPutAllCodec_encodeResponse() {
        int fileClientMessageIndex = 129;
        ClientMessage encoded = ServerMapPutAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapClearCodec_decodeRequest() {
        int fileClientMessageIndex = 130;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapClearCodec_encodeResponse() {
        int fileClientMessageIndex = 131;
        ClientMessage encoded = ServerMapClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapExecuteOnKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 132;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapExecuteOnKeyCodec.RequestParameters parameters = ServerMapExecuteOnKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.entryProcessor));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapExecuteOnKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 133;
        ClientMessage encoded = ServerMapExecuteOnKeyCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapSubmitToKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 134;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapSubmitToKeyCodec.RequestParameters parameters = ServerMapSubmitToKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.entryProcessor));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MapSubmitToKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 135;
        ClientMessage encoded = ServerMapSubmitToKeyCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapExecuteOnAllKeysCodec_decodeRequest() {
        int fileClientMessageIndex = 136;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapExecuteOnAllKeysCodec.RequestParameters parameters = ServerMapExecuteOnAllKeysCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.entryProcessor));
    }

    @Test
    public void test_MapExecuteOnAllKeysCodec_encodeResponse() {
        int fileClientMessageIndex = 137;
        ClientMessage encoded = ServerMapExecuteOnAllKeysCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapExecuteWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 138;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapExecuteWithPredicateCodec.RequestParameters parameters = ServerMapExecuteWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.entryProcessor));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapExecuteWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 139;
        ClientMessage encoded = ServerMapExecuteWithPredicateCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapExecuteOnKeysCodec_decodeRequest() {
        int fileClientMessageIndex = 140;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapExecuteOnKeysCodec.RequestParameters parameters = ServerMapExecuteOnKeysCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.entryProcessor));
        assertTrue(isEqual(aListOfData, parameters.keys));
    }

    @Test
    public void test_MapExecuteOnKeysCodec_encodeResponse() {
        int fileClientMessageIndex = 141;
        ClientMessage encoded = ServerMapExecuteOnKeysCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapForceUnlockCodec_decodeRequest() {
        int fileClientMessageIndex = 142;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapForceUnlockCodec.RequestParameters parameters = ServerMapForceUnlockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MapForceUnlockCodec_encodeResponse() {
        int fileClientMessageIndex = 143;
        ClientMessage encoded = ServerMapForceUnlockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapKeySetWithPagingPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 144;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapKeySetWithPagingPredicateCodec.RequestParameters parameters = ServerMapKeySetWithPagingPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aPagingPredicateHolder, parameters.predicate));
    }

    @Test
    public void test_MapKeySetWithPagingPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 145;
        ClientMessage encoded = ServerMapKeySetWithPagingPredicateCodec.encodeResponse(aListOfData, anAnchorDataListHolder);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapValuesWithPagingPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 146;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapValuesWithPagingPredicateCodec.RequestParameters parameters = ServerMapValuesWithPagingPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aPagingPredicateHolder, parameters.predicate));
    }

    @Test
    public void test_MapValuesWithPagingPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 147;
        ClientMessage encoded = ServerMapValuesWithPagingPredicateCodec.encodeResponse(aListOfData, anAnchorDataListHolder);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEntriesWithPagingPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 148;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapEntriesWithPagingPredicateCodec.RequestParameters parameters = ServerMapEntriesWithPagingPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aPagingPredicateHolder, parameters.predicate));
    }

    @Test
    public void test_MapEntriesWithPagingPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 149;
        ClientMessage encoded = ServerMapEntriesWithPagingPredicateCodec.encodeResponse(aListOfDataToData, anAnchorDataListHolder);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapFetchKeysCodec_decodeRequest() {
        int fileClientMessageIndex = 150;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapFetchKeysCodec.RequestParameters parameters = ServerMapFetchKeysCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfIntegerToInteger, parameters.iterationPointers));
        assertTrue(isEqual(anInt, parameters.batch));
    }

    @Test
    public void test_MapFetchKeysCodec_encodeResponse() {
        int fileClientMessageIndex = 151;
        ClientMessage encoded = ServerMapFetchKeysCodec.encodeResponse(aListOfIntegerToInteger, aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapFetchEntriesCodec_decodeRequest() {
        int fileClientMessageIndex = 152;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapFetchEntriesCodec.RequestParameters parameters = ServerMapFetchEntriesCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfIntegerToInteger, parameters.iterationPointers));
        assertTrue(isEqual(anInt, parameters.batch));
    }

    @Test
    public void test_MapFetchEntriesCodec_encodeResponse() {
        int fileClientMessageIndex = 153;
        ClientMessage encoded = ServerMapFetchEntriesCodec.encodeResponse(aListOfIntegerToInteger, aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAggregateCodec_decodeRequest() {
        int fileClientMessageIndex = 154;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAggregateCodec.RequestParameters parameters = ServerMapAggregateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.aggregator));
    }

    @Test
    public void test_MapAggregateCodec_encodeResponse() {
        int fileClientMessageIndex = 155;
        ClientMessage encoded = ServerMapAggregateCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAggregateWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 156;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAggregateWithPredicateCodec.RequestParameters parameters = ServerMapAggregateWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.aggregator));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapAggregateWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 157;
        ClientMessage encoded = ServerMapAggregateWithPredicateCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapProjectCodec_decodeRequest() {
        int fileClientMessageIndex = 158;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapProjectCodec.RequestParameters parameters = ServerMapProjectCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.projection));
    }

    @Test
    public void test_MapProjectCodec_encodeResponse() {
        int fileClientMessageIndex = 159;
        ClientMessage encoded = ServerMapProjectCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapProjectWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 160;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapProjectWithPredicateCodec.RequestParameters parameters = ServerMapProjectWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.projection));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapProjectWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 161;
        ClientMessage encoded = ServerMapProjectWithPredicateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapFetchNearCacheInvalidationMetadataCodec_decodeRequest() {
        int fileClientMessageIndex = 162;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapFetchNearCacheInvalidationMetadataCodec.RequestParameters parameters = ServerMapFetchNearCacheInvalidationMetadataCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aListOfStrings, parameters.names));
        assertTrue(isEqual(aUUID, parameters.uuid));
    }

    @Test
    public void test_MapFetchNearCacheInvalidationMetadataCodec_encodeResponse() {
        int fileClientMessageIndex = 163;
        ClientMessage encoded = ServerMapFetchNearCacheInvalidationMetadataCodec.encodeResponse(aListOfStringToListOfIntegerToLong, aListOfIntegerToUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapRemoveAllCodec_decodeRequest() {
        int fileClientMessageIndex = 164;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapRemoveAllCodec.RequestParameters parameters = ServerMapRemoveAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapRemoveAllCodec_encodeResponse() {
        int fileClientMessageIndex = 165;
        ClientMessage encoded = ServerMapRemoveAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddNearCacheInvalidationListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 166;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapAddNearCacheInvalidationListenerCodec.RequestParameters parameters = ServerMapAddNearCacheInvalidationListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.listenerFlags));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MapAddNearCacheInvalidationListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 167;
        ClientMessage encoded = ServerMapAddNearCacheInvalidationListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddNearCacheInvalidationListenerCodec_encodeIMapInvalidationEvent() {
        int fileClientMessageIndex = 168;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddNearCacheInvalidationListenerCodec.encodeIMapInvalidationEvent(aData, aUUID, aUUID, aLong);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapAddNearCacheInvalidationListenerCodec_encodeIMapBatchInvalidationEvent() {
        int fileClientMessageIndex = 169;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMapAddNearCacheInvalidationListenerCodec.encodeIMapBatchInvalidationEvent(aListOfData, aListOfUUIDs, aListOfUUIDs, aListOfLongs);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapFetchWithQueryCodec_decodeRequest() {
        int fileClientMessageIndex = 170;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapFetchWithQueryCodec.RequestParameters parameters = ServerMapFetchWithQueryCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfIntegerToInteger, parameters.iterationPointers));
        assertTrue(isEqual(anInt, parameters.batch));
        assertTrue(isEqual(aData, parameters.projection));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_MapFetchWithQueryCodec_encodeResponse() {
        int fileClientMessageIndex = 171;
        ClientMessage encoded = ServerMapFetchWithQueryCodec.encodeResponse(aListOfData, aListOfIntegerToInteger);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEventJournalSubscribeCodec_decodeRequest() {
        int fileClientMessageIndex = 172;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMapEventJournalSubscribeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MapEventJournalSubscribeCodec_encodeResponse() {
        int fileClientMessageIndex = 173;
        ClientMessage encoded = ServerMapEventJournalSubscribeCodec.encodeResponse(aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapEventJournalReadCodec_decodeRequest() {
        int fileClientMessageIndex = 174;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapEventJournalReadCodec.RequestParameters parameters = ServerMapEventJournalReadCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.startSequence));
        assertTrue(isEqual(anInt, parameters.minSize));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aData, parameters.projection));
    }

    @Test
    public void test_MapEventJournalReadCodec_encodeResponse() {
        int fileClientMessageIndex = 175;
        ClientMessage encoded = ServerMapEventJournalReadCodec.encodeResponse(anInt, aListOfData, aLongArray, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapSetTtlCodec_decodeRequest() {
        int fileClientMessageIndex = 176;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapSetTtlCodec.RequestParameters parameters = ServerMapSetTtlCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_MapSetTtlCodec_encodeResponse() {
        int fileClientMessageIndex = 177;
        ClientMessage encoded = ServerMapSetTtlCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutWithMaxIdleCodec_decodeRequest() {
        int fileClientMessageIndex = 178;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutWithMaxIdleCodec.RequestParameters parameters = ServerMapPutWithMaxIdleCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.maxIdle));
    }

    @Test
    public void test_MapPutWithMaxIdleCodec_encodeResponse() {
        int fileClientMessageIndex = 179;
        ClientMessage encoded = ServerMapPutWithMaxIdleCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutTransientWithMaxIdleCodec_decodeRequest() {
        int fileClientMessageIndex = 180;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutTransientWithMaxIdleCodec.RequestParameters parameters = ServerMapPutTransientWithMaxIdleCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.maxIdle));
    }

    @Test
    public void test_MapPutTransientWithMaxIdleCodec_encodeResponse() {
        int fileClientMessageIndex = 181;
        ClientMessage encoded = ServerMapPutTransientWithMaxIdleCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapPutIfAbsentWithMaxIdleCodec_decodeRequest() {
        int fileClientMessageIndex = 182;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapPutIfAbsentWithMaxIdleCodec.RequestParameters parameters = ServerMapPutIfAbsentWithMaxIdleCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.maxIdle));
    }

    @Test
    public void test_MapPutIfAbsentWithMaxIdleCodec_encodeResponse() {
        int fileClientMessageIndex = 183;
        ClientMessage encoded = ServerMapPutIfAbsentWithMaxIdleCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MapSetWithMaxIdleCodec_decodeRequest() {
        int fileClientMessageIndex = 184;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMapSetWithMaxIdleCodec.RequestParameters parameters = ServerMapSetWithMaxIdleCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.maxIdle));
    }

    @Test
    public void test_MapSetWithMaxIdleCodec_encodeResponse() {
        int fileClientMessageIndex = 185;
        ClientMessage encoded = ServerMapSetWithMaxIdleCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapPutCodec_decodeRequest() {
        int fileClientMessageIndex = 186;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapPutCodec.RequestParameters parameters = ServerMultiMapPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapPutCodec_encodeResponse() {
        int fileClientMessageIndex = 187;
        ClientMessage encoded = ServerMultiMapPutCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapGetCodec_decodeRequest() {
        int fileClientMessageIndex = 188;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapGetCodec.RequestParameters parameters = ServerMultiMapGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapGetCodec_encodeResponse() {
        int fileClientMessageIndex = 189;
        ClientMessage encoded = ServerMultiMapGetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 190;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapRemoveCodec.RequestParameters parameters = ServerMultiMapRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 191;
        ClientMessage encoded = ServerMultiMapRemoveCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapKeySetCodec_decodeRequest() {
        int fileClientMessageIndex = 192;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMultiMapKeySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MultiMapKeySetCodec_encodeResponse() {
        int fileClientMessageIndex = 193;
        ClientMessage encoded = ServerMultiMapKeySetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapValuesCodec_decodeRequest() {
        int fileClientMessageIndex = 194;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMultiMapValuesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MultiMapValuesCodec_encodeResponse() {
        int fileClientMessageIndex = 195;
        ClientMessage encoded = ServerMultiMapValuesCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapEntrySetCodec_decodeRequest() {
        int fileClientMessageIndex = 196;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMultiMapEntrySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MultiMapEntrySetCodec_encodeResponse() {
        int fileClientMessageIndex = 197;
        ClientMessage encoded = ServerMultiMapEntrySetCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapContainsKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 198;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapContainsKeyCodec.RequestParameters parameters = ServerMultiMapContainsKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapContainsKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 199;
        ClientMessage encoded = ServerMultiMapContainsKeyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapContainsValueCodec_decodeRequest() {
        int fileClientMessageIndex = 200;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapContainsValueCodec.RequestParameters parameters = ServerMultiMapContainsValueCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_MultiMapContainsValueCodec_encodeResponse() {
        int fileClientMessageIndex = 201;
        ClientMessage encoded = ServerMultiMapContainsValueCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapContainsEntryCodec_decodeRequest() {
        int fileClientMessageIndex = 202;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapContainsEntryCodec.RequestParameters parameters = ServerMultiMapContainsEntryCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapContainsEntryCodec_encodeResponse() {
        int fileClientMessageIndex = 203;
        ClientMessage encoded = ServerMultiMapContainsEntryCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 204;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMultiMapSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MultiMapSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 205;
        ClientMessage encoded = ServerMultiMapSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapClearCodec_decodeRequest() {
        int fileClientMessageIndex = 206;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMultiMapClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MultiMapClearCodec_encodeResponse() {
        int fileClientMessageIndex = 207;
        ClientMessage encoded = ServerMultiMapClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapValueCountCodec_decodeRequest() {
        int fileClientMessageIndex = 208;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapValueCountCodec.RequestParameters parameters = ServerMultiMapValueCountCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapValueCountCodec_encodeResponse() {
        int fileClientMessageIndex = 209;
        ClientMessage encoded = ServerMultiMapValueCountCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapAddEntryListenerToKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 210;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapAddEntryListenerToKeyCodec.RequestParameters parameters = ServerMultiMapAddEntryListenerToKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MultiMapAddEntryListenerToKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 211;
        ClientMessage encoded = ServerMultiMapAddEntryListenerToKeyCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapAddEntryListenerToKeyCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 212;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMultiMapAddEntryListenerToKeyCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapAddEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 213;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapAddEntryListenerCodec.RequestParameters parameters = ServerMultiMapAddEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_MultiMapAddEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 214;
        ClientMessage encoded = ServerMultiMapAddEntryListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapAddEntryListenerCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 215;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerMultiMapAddEntryListenerCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapRemoveEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 216;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapRemoveEntryListenerCodec.RequestParameters parameters = ServerMultiMapRemoveEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_MultiMapRemoveEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 217;
        ClientMessage encoded = ServerMultiMapRemoveEntryListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapLockCodec_decodeRequest() {
        int fileClientMessageIndex = 218;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapLockCodec.RequestParameters parameters = ServerMultiMapLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.ttl));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MultiMapLockCodec_encodeResponse() {
        int fileClientMessageIndex = 219;
        ClientMessage encoded = ServerMultiMapLockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapTryLockCodec_decodeRequest() {
        int fileClientMessageIndex = 220;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapTryLockCodec.RequestParameters parameters = ServerMultiMapTryLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.lease));
        assertTrue(isEqual(aLong, parameters.timeout));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MultiMapTryLockCodec_encodeResponse() {
        int fileClientMessageIndex = 221;
        ClientMessage encoded = ServerMultiMapTryLockCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapIsLockedCodec_decodeRequest() {
        int fileClientMessageIndex = 222;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapIsLockedCodec.RequestParameters parameters = ServerMultiMapIsLockedCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_MultiMapIsLockedCodec_encodeResponse() {
        int fileClientMessageIndex = 223;
        ClientMessage encoded = ServerMultiMapIsLockedCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapUnlockCodec_decodeRequest() {
        int fileClientMessageIndex = 224;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapUnlockCodec.RequestParameters parameters = ServerMultiMapUnlockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MultiMapUnlockCodec_encodeResponse() {
        int fileClientMessageIndex = 225;
        ClientMessage encoded = ServerMultiMapUnlockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapForceUnlockCodec_decodeRequest() {
        int fileClientMessageIndex = 226;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapForceUnlockCodec.RequestParameters parameters = ServerMultiMapForceUnlockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.referenceId));
    }

    @Test
    public void test_MultiMapForceUnlockCodec_encodeResponse() {
        int fileClientMessageIndex = 227;
        ClientMessage encoded = ServerMultiMapForceUnlockCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapRemoveEntryCodec_decodeRequest() {
        int fileClientMessageIndex = 228;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapRemoveEntryCodec.RequestParameters parameters = ServerMultiMapRemoveEntryCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapRemoveEntryCodec_encodeResponse() {
        int fileClientMessageIndex = 229;
        ClientMessage encoded = ServerMultiMapRemoveEntryCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapDeleteCodec_decodeRequest() {
        int fileClientMessageIndex = 230;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapDeleteCodec.RequestParameters parameters = ServerMultiMapDeleteCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_MultiMapDeleteCodec_encodeResponse() {
        int fileClientMessageIndex = 231;
        ClientMessage encoded = ServerMultiMapDeleteCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MultiMapPutAllCodec_decodeRequest() {
        int fileClientMessageIndex = 232;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMultiMapPutAllCodec.RequestParameters parameters = ServerMultiMapPutAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfDataToListOfData, parameters.entries));
    }

    @Test
    public void test_MultiMapPutAllCodec_encodeResponse() {
        int fileClientMessageIndex = 233;
        ClientMessage encoded = ServerMultiMapPutAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueOfferCodec_decodeRequest() {
        int fileClientMessageIndex = 234;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueOfferCodec.RequestParameters parameters = ServerQueueOfferCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.timeoutMillis));
    }

    @Test
    public void test_QueueOfferCodec_encodeResponse() {
        int fileClientMessageIndex = 235;
        ClientMessage encoded = ServerQueueOfferCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueuePutCodec_decodeRequest() {
        int fileClientMessageIndex = 236;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueuePutCodec.RequestParameters parameters = ServerQueuePutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_QueuePutCodec_encodeResponse() {
        int fileClientMessageIndex = 237;
        ClientMessage encoded = ServerQueuePutCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 238;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 239;
        ClientMessage encoded = ServerQueueSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 240;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueRemoveCodec.RequestParameters parameters = ServerQueueRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_QueueRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 241;
        ClientMessage encoded = ServerQueueRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueuePollCodec_decodeRequest() {
        int fileClientMessageIndex = 242;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueuePollCodec.RequestParameters parameters = ServerQueuePollCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.timeoutMillis));
    }

    @Test
    public void test_QueuePollCodec_encodeResponse() {
        int fileClientMessageIndex = 243;
        ClientMessage encoded = ServerQueuePollCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueTakeCodec_decodeRequest() {
        int fileClientMessageIndex = 244;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueTakeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueTakeCodec_encodeResponse() {
        int fileClientMessageIndex = 245;
        ClientMessage encoded = ServerQueueTakeCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueuePeekCodec_decodeRequest() {
        int fileClientMessageIndex = 246;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueuePeekCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueuePeekCodec_encodeResponse() {
        int fileClientMessageIndex = 247;
        ClientMessage encoded = ServerQueuePeekCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueIteratorCodec_decodeRequest() {
        int fileClientMessageIndex = 248;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueIteratorCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueIteratorCodec_encodeResponse() {
        int fileClientMessageIndex = 249;
        ClientMessage encoded = ServerQueueIteratorCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueDrainToCodec_decodeRequest() {
        int fileClientMessageIndex = 250;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueDrainToCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueDrainToCodec_encodeResponse() {
        int fileClientMessageIndex = 251;
        ClientMessage encoded = ServerQueueDrainToCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueDrainToMaxSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 252;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueDrainToMaxSizeCodec.RequestParameters parameters = ServerQueueDrainToMaxSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.maxSize));
    }

    @Test
    public void test_QueueDrainToMaxSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 253;
        ClientMessage encoded = ServerQueueDrainToMaxSizeCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueContainsCodec_decodeRequest() {
        int fileClientMessageIndex = 254;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueContainsCodec.RequestParameters parameters = ServerQueueContainsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_QueueContainsCodec_encodeResponse() {
        int fileClientMessageIndex = 255;
        ClientMessage encoded = ServerQueueContainsCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueContainsAllCodec_decodeRequest() {
        int fileClientMessageIndex = 256;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueContainsAllCodec.RequestParameters parameters = ServerQueueContainsAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.dataList));
    }

    @Test
    public void test_QueueContainsAllCodec_encodeResponse() {
        int fileClientMessageIndex = 257;
        ClientMessage encoded = ServerQueueContainsAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueCompareAndRemoveAllCodec_decodeRequest() {
        int fileClientMessageIndex = 258;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueCompareAndRemoveAllCodec.RequestParameters parameters = ServerQueueCompareAndRemoveAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.dataList));
    }

    @Test
    public void test_QueueCompareAndRemoveAllCodec_encodeResponse() {
        int fileClientMessageIndex = 259;
        ClientMessage encoded = ServerQueueCompareAndRemoveAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueCompareAndRetainAllCodec_decodeRequest() {
        int fileClientMessageIndex = 260;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueCompareAndRetainAllCodec.RequestParameters parameters = ServerQueueCompareAndRetainAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.dataList));
    }

    @Test
    public void test_QueueCompareAndRetainAllCodec_encodeResponse() {
        int fileClientMessageIndex = 261;
        ClientMessage encoded = ServerQueueCompareAndRetainAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueClearCodec_decodeRequest() {
        int fileClientMessageIndex = 262;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueClearCodec_encodeResponse() {
        int fileClientMessageIndex = 263;
        ClientMessage encoded = ServerQueueClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueAddAllCodec_decodeRequest() {
        int fileClientMessageIndex = 264;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueAddAllCodec.RequestParameters parameters = ServerQueueAddAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.dataList));
    }

    @Test
    public void test_QueueAddAllCodec_encodeResponse() {
        int fileClientMessageIndex = 265;
        ClientMessage encoded = ServerQueueAddAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueAddListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 266;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueAddListenerCodec.RequestParameters parameters = ServerQueueAddListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_QueueAddListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 267;
        ClientMessage encoded = ServerQueueAddListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueAddListenerCodec_encodeItemEvent() {
        int fileClientMessageIndex = 268;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerQueueAddListenerCodec.encodeItemEvent(aData, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueRemoveListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 269;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerQueueRemoveListenerCodec.RequestParameters parameters = ServerQueueRemoveListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_QueueRemoveListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 270;
        ClientMessage encoded = ServerQueueRemoveListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueRemainingCapacityCodec_decodeRequest() {
        int fileClientMessageIndex = 271;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueRemainingCapacityCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueRemainingCapacityCodec_encodeResponse() {
        int fileClientMessageIndex = 272;
        ClientMessage encoded = ServerQueueRemainingCapacityCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_QueueIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 273;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerQueueIsEmptyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_QueueIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 274;
        ClientMessage encoded = ServerQueueIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TopicPublishCodec_decodeRequest() {
        int fileClientMessageIndex = 275;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTopicPublishCodec.RequestParameters parameters = ServerTopicPublishCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.message));
    }

    @Test
    public void test_TopicPublishCodec_encodeResponse() {
        int fileClientMessageIndex = 276;
        ClientMessage encoded = ServerTopicPublishCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TopicAddMessageListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 277;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTopicAddMessageListenerCodec.RequestParameters parameters = ServerTopicAddMessageListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_TopicAddMessageListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 278;
        ClientMessage encoded = ServerTopicAddMessageListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TopicAddMessageListenerCodec_encodeTopicEvent() {
        int fileClientMessageIndex = 279;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerTopicAddMessageListenerCodec.encodeTopicEvent(aData, aLong, aUUID);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TopicRemoveMessageListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 280;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTopicRemoveMessageListenerCodec.RequestParameters parameters = ServerTopicRemoveMessageListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_TopicRemoveMessageListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 281;
        ClientMessage encoded = ServerTopicRemoveMessageListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TopicPublishAllCodec_decodeRequest() {
        int fileClientMessageIndex = 282;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTopicPublishAllCodec.RequestParameters parameters = ServerTopicPublishAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.messages));
    }

    @Test
    public void test_TopicPublishAllCodec_encodeResponse() {
        int fileClientMessageIndex = 283;
        ClientMessage encoded = ServerTopicPublishAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 284;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerListSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ListSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 285;
        ClientMessage encoded = ServerListSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListContainsCodec_decodeRequest() {
        int fileClientMessageIndex = 286;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListContainsCodec.RequestParameters parameters = ServerListContainsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListContainsCodec_encodeResponse() {
        int fileClientMessageIndex = 287;
        ClientMessage encoded = ServerListContainsCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListContainsAllCodec_decodeRequest() {
        int fileClientMessageIndex = 288;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListContainsAllCodec.RequestParameters parameters = ServerListContainsAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.values));
    }

    @Test
    public void test_ListContainsAllCodec_encodeResponse() {
        int fileClientMessageIndex = 289;
        ClientMessage encoded = ServerListContainsAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddCodec_decodeRequest() {
        int fileClientMessageIndex = 290;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListAddCodec.RequestParameters parameters = ServerListAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListAddCodec_encodeResponse() {
        int fileClientMessageIndex = 291;
        ClientMessage encoded = ServerListAddCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 292;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListRemoveCodec.RequestParameters parameters = ServerListRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 293;
        ClientMessage encoded = ServerListRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddAllCodec_decodeRequest() {
        int fileClientMessageIndex = 294;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListAddAllCodec.RequestParameters parameters = ServerListAddAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.valueList));
    }

    @Test
    public void test_ListAddAllCodec_encodeResponse() {
        int fileClientMessageIndex = 295;
        ClientMessage encoded = ServerListAddAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListCompareAndRemoveAllCodec_decodeRequest() {
        int fileClientMessageIndex = 296;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListCompareAndRemoveAllCodec.RequestParameters parameters = ServerListCompareAndRemoveAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.values));
    }

    @Test
    public void test_ListCompareAndRemoveAllCodec_encodeResponse() {
        int fileClientMessageIndex = 297;
        ClientMessage encoded = ServerListCompareAndRemoveAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListCompareAndRetainAllCodec_decodeRequest() {
        int fileClientMessageIndex = 298;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListCompareAndRetainAllCodec.RequestParameters parameters = ServerListCompareAndRetainAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.values));
    }

    @Test
    public void test_ListCompareAndRetainAllCodec_encodeResponse() {
        int fileClientMessageIndex = 299;
        ClientMessage encoded = ServerListCompareAndRetainAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListClearCodec_decodeRequest() {
        int fileClientMessageIndex = 300;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerListClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ListClearCodec_encodeResponse() {
        int fileClientMessageIndex = 301;
        ClientMessage encoded = ServerListClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListGetAllCodec_decodeRequest() {
        int fileClientMessageIndex = 302;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerListGetAllCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ListGetAllCodec_encodeResponse() {
        int fileClientMessageIndex = 303;
        ClientMessage encoded = ServerListGetAllCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 304;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListAddListenerCodec.RequestParameters parameters = ServerListAddListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ListAddListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 305;
        ClientMessage encoded = ServerListAddListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddListenerCodec_encodeItemEvent() {
        int fileClientMessageIndex = 306;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerListAddListenerCodec.encodeItemEvent(aData, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListRemoveListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 307;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListRemoveListenerCodec.RequestParameters parameters = ServerListRemoveListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_ListRemoveListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 308;
        ClientMessage encoded = ServerListRemoveListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 309;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerListIsEmptyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ListIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 310;
        ClientMessage encoded = ServerListIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddAllWithIndexCodec_decodeRequest() {
        int fileClientMessageIndex = 311;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListAddAllWithIndexCodec.RequestParameters parameters = ServerListAddAllWithIndexCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
        assertTrue(isEqual(aListOfData, parameters.valueList));
    }

    @Test
    public void test_ListAddAllWithIndexCodec_encodeResponse() {
        int fileClientMessageIndex = 312;
        ClientMessage encoded = ServerListAddAllWithIndexCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListGetCodec_decodeRequest() {
        int fileClientMessageIndex = 313;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListGetCodec.RequestParameters parameters = ServerListGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
    }

    @Test
    public void test_ListGetCodec_encodeResponse() {
        int fileClientMessageIndex = 314;
        ClientMessage encoded = ServerListGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListSetCodec_decodeRequest() {
        int fileClientMessageIndex = 315;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListSetCodec.RequestParameters parameters = ServerListSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListSetCodec_encodeResponse() {
        int fileClientMessageIndex = 316;
        ClientMessage encoded = ServerListSetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListAddWithIndexCodec_decodeRequest() {
        int fileClientMessageIndex = 317;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListAddWithIndexCodec.RequestParameters parameters = ServerListAddWithIndexCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListAddWithIndexCodec_encodeResponse() {
        int fileClientMessageIndex = 318;
        ClientMessage encoded = ServerListAddWithIndexCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListRemoveWithIndexCodec_decodeRequest() {
        int fileClientMessageIndex = 319;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListRemoveWithIndexCodec.RequestParameters parameters = ServerListRemoveWithIndexCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
    }

    @Test
    public void test_ListRemoveWithIndexCodec_encodeResponse() {
        int fileClientMessageIndex = 320;
        ClientMessage encoded = ServerListRemoveWithIndexCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListLastIndexOfCodec_decodeRequest() {
        int fileClientMessageIndex = 321;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListLastIndexOfCodec.RequestParameters parameters = ServerListLastIndexOfCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListLastIndexOfCodec_encodeResponse() {
        int fileClientMessageIndex = 322;
        ClientMessage encoded = ServerListLastIndexOfCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListIndexOfCodec_decodeRequest() {
        int fileClientMessageIndex = 323;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListIndexOfCodec.RequestParameters parameters = ServerListIndexOfCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ListIndexOfCodec_encodeResponse() {
        int fileClientMessageIndex = 324;
        ClientMessage encoded = ServerListIndexOfCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListSubCodec_decodeRequest() {
        int fileClientMessageIndex = 325;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListSubCodec.RequestParameters parameters = ServerListSubCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.from));
        assertTrue(isEqual(anInt, parameters.to));
    }

    @Test
    public void test_ListSubCodec_encodeResponse() {
        int fileClientMessageIndex = 326;
        ClientMessage encoded = ServerListSubCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListIteratorCodec_decodeRequest() {
        int fileClientMessageIndex = 327;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerListIteratorCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ListIteratorCodec_encodeResponse() {
        int fileClientMessageIndex = 328;
        ClientMessage encoded = ServerListIteratorCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ListListIteratorCodec_decodeRequest() {
        int fileClientMessageIndex = 329;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerListListIteratorCodec.RequestParameters parameters = ServerListListIteratorCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.index));
    }

    @Test
    public void test_ListListIteratorCodec_encodeResponse() {
        int fileClientMessageIndex = 330;
        ClientMessage encoded = ServerListListIteratorCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 331;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerSetSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SetSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 332;
        ClientMessage encoded = ServerSetSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetContainsCodec_decodeRequest() {
        int fileClientMessageIndex = 333;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetContainsCodec.RequestParameters parameters = ServerSetContainsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_SetContainsCodec_encodeResponse() {
        int fileClientMessageIndex = 334;
        ClientMessage encoded = ServerSetContainsCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetContainsAllCodec_decodeRequest() {
        int fileClientMessageIndex = 335;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetContainsAllCodec.RequestParameters parameters = ServerSetContainsAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.items));
    }

    @Test
    public void test_SetContainsAllCodec_encodeResponse() {
        int fileClientMessageIndex = 336;
        ClientMessage encoded = ServerSetContainsAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetAddCodec_decodeRequest() {
        int fileClientMessageIndex = 337;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetAddCodec.RequestParameters parameters = ServerSetAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_SetAddCodec_encodeResponse() {
        int fileClientMessageIndex = 338;
        ClientMessage encoded = ServerSetAddCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 339;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetRemoveCodec.RequestParameters parameters = ServerSetRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_SetRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 340;
        ClientMessage encoded = ServerSetRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetAddAllCodec_decodeRequest() {
        int fileClientMessageIndex = 341;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetAddAllCodec.RequestParameters parameters = ServerSetAddAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.valueList));
    }

    @Test
    public void test_SetAddAllCodec_encodeResponse() {
        int fileClientMessageIndex = 342;
        ClientMessage encoded = ServerSetAddAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetCompareAndRemoveAllCodec_decodeRequest() {
        int fileClientMessageIndex = 343;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetCompareAndRemoveAllCodec.RequestParameters parameters = ServerSetCompareAndRemoveAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.values));
    }

    @Test
    public void test_SetCompareAndRemoveAllCodec_encodeResponse() {
        int fileClientMessageIndex = 344;
        ClientMessage encoded = ServerSetCompareAndRemoveAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetCompareAndRetainAllCodec_decodeRequest() {
        int fileClientMessageIndex = 345;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetCompareAndRetainAllCodec.RequestParameters parameters = ServerSetCompareAndRetainAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.values));
    }

    @Test
    public void test_SetCompareAndRetainAllCodec_encodeResponse() {
        int fileClientMessageIndex = 346;
        ClientMessage encoded = ServerSetCompareAndRetainAllCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetClearCodec_decodeRequest() {
        int fileClientMessageIndex = 347;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerSetClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SetClearCodec_encodeResponse() {
        int fileClientMessageIndex = 348;
        ClientMessage encoded = ServerSetClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetGetAllCodec_decodeRequest() {
        int fileClientMessageIndex = 349;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerSetGetAllCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SetGetAllCodec_encodeResponse() {
        int fileClientMessageIndex = 350;
        ClientMessage encoded = ServerSetGetAllCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetAddListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 351;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetAddListenerCodec.RequestParameters parameters = ServerSetAddListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_SetAddListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 352;
        ClientMessage encoded = ServerSetAddListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetAddListenerCodec_encodeItemEvent() {
        int fileClientMessageIndex = 353;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerSetAddListenerCodec.encodeItemEvent(aData, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetRemoveListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 354;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSetRemoveListenerCodec.RequestParameters parameters = ServerSetRemoveListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_SetRemoveListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 355;
        ClientMessage encoded = ServerSetRemoveListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SetIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 356;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerSetIsEmptyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SetIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 357;
        ClientMessage encoded = ServerSetIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_FencedLockLockCodec_decodeRequest() {
        int fileClientMessageIndex = 358;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerFencedLockLockCodec.RequestParameters parameters = ServerFencedLockLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
    }

    @Test
    public void test_FencedLockLockCodec_encodeResponse() {
        int fileClientMessageIndex = 359;
        ClientMessage encoded = ServerFencedLockLockCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_FencedLockTryLockCodec_decodeRequest() {
        int fileClientMessageIndex = 360;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerFencedLockTryLockCodec.RequestParameters parameters = ServerFencedLockTryLockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(aLong, parameters.timeoutMs));
    }

    @Test
    public void test_FencedLockTryLockCodec_encodeResponse() {
        int fileClientMessageIndex = 361;
        ClientMessage encoded = ServerFencedLockTryLockCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_FencedLockUnlockCodec_decodeRequest() {
        int fileClientMessageIndex = 362;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerFencedLockUnlockCodec.RequestParameters parameters = ServerFencedLockUnlockCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
    }

    @Test
    public void test_FencedLockUnlockCodec_encodeResponse() {
        int fileClientMessageIndex = 363;
        ClientMessage encoded = ServerFencedLockUnlockCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_FencedLockGetLockOwnershipCodec_decodeRequest() {
        int fileClientMessageIndex = 364;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerFencedLockGetLockOwnershipCodec.RequestParameters parameters = ServerFencedLockGetLockOwnershipCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_FencedLockGetLockOwnershipCodec_encodeResponse() {
        int fileClientMessageIndex = 365;
        ClientMessage encoded = ServerFencedLockGetLockOwnershipCodec.encodeResponse(aLong, anInt, aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceShutdownCodec_decodeRequest() {
        int fileClientMessageIndex = 366;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerExecutorServiceShutdownCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ExecutorServiceShutdownCodec_encodeResponse() {
        int fileClientMessageIndex = 367;
        ClientMessage encoded = ServerExecutorServiceShutdownCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceIsShutdownCodec_decodeRequest() {
        int fileClientMessageIndex = 368;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerExecutorServiceIsShutdownCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ExecutorServiceIsShutdownCodec_encodeResponse() {
        int fileClientMessageIndex = 369;
        ClientMessage encoded = ServerExecutorServiceIsShutdownCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceCancelOnPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 370;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerExecutorServiceCancelOnPartitionCodec.RequestParameters parameters = ServerExecutorServiceCancelOnPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aBoolean, parameters.interrupt));
    }

    @Test
    public void test_ExecutorServiceCancelOnPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 371;
        ClientMessage encoded = ServerExecutorServiceCancelOnPartitionCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceCancelOnMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 372;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerExecutorServiceCancelOnMemberCodec.RequestParameters parameters = ServerExecutorServiceCancelOnMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aUUID, parameters.memberUUID));
        assertTrue(isEqual(aBoolean, parameters.interrupt));
    }

    @Test
    public void test_ExecutorServiceCancelOnMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 373;
        ClientMessage encoded = ServerExecutorServiceCancelOnMemberCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceSubmitToPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 374;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerExecutorServiceSubmitToPartitionCodec.RequestParameters parameters = ServerExecutorServiceSubmitToPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aData, parameters.callable));
    }

    @Test
    public void test_ExecutorServiceSubmitToPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 375;
        ClientMessage encoded = ServerExecutorServiceSubmitToPartitionCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ExecutorServiceSubmitToMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 376;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerExecutorServiceSubmitToMemberCodec.RequestParameters parameters = ServerExecutorServiceSubmitToMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aData, parameters.callable));
        assertTrue(isEqual(aUUID, parameters.memberUUID));
    }

    @Test
    public void test_ExecutorServiceSubmitToMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 377;
        ClientMessage encoded = ServerExecutorServiceSubmitToMemberCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongApplyCodec_decodeRequest() {
        int fileClientMessageIndex = 378;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongApplyCodec.RequestParameters parameters = ServerAtomicLongApplyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.function));
    }

    @Test
    public void test_AtomicLongApplyCodec_encodeResponse() {
        int fileClientMessageIndex = 379;
        ClientMessage encoded = ServerAtomicLongApplyCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongAlterCodec_decodeRequest() {
        int fileClientMessageIndex = 380;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongAlterCodec.RequestParameters parameters = ServerAtomicLongAlterCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.function));
        assertTrue(isEqual(anInt, parameters.returnValueType));
    }

    @Test
    public void test_AtomicLongAlterCodec_encodeResponse() {
        int fileClientMessageIndex = 381;
        ClientMessage encoded = ServerAtomicLongAlterCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongAddAndGetCodec_decodeRequest() {
        int fileClientMessageIndex = 382;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongAddAndGetCodec.RequestParameters parameters = ServerAtomicLongAddAndGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.delta));
    }

    @Test
    public void test_AtomicLongAddAndGetCodec_encodeResponse() {
        int fileClientMessageIndex = 383;
        ClientMessage encoded = ServerAtomicLongAddAndGetCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongCompareAndSetCodec_decodeRequest() {
        int fileClientMessageIndex = 384;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongCompareAndSetCodec.RequestParameters parameters = ServerAtomicLongCompareAndSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.expected));
        assertTrue(isEqual(aLong, parameters.updated));
    }

    @Test
    public void test_AtomicLongCompareAndSetCodec_encodeResponse() {
        int fileClientMessageIndex = 385;
        ClientMessage encoded = ServerAtomicLongCompareAndSetCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongGetCodec_decodeRequest() {
        int fileClientMessageIndex = 386;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongGetCodec.RequestParameters parameters = ServerAtomicLongGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_AtomicLongGetCodec_encodeResponse() {
        int fileClientMessageIndex = 387;
        ClientMessage encoded = ServerAtomicLongGetCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongGetAndAddCodec_decodeRequest() {
        int fileClientMessageIndex = 388;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongGetAndAddCodec.RequestParameters parameters = ServerAtomicLongGetAndAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.delta));
    }

    @Test
    public void test_AtomicLongGetAndAddCodec_encodeResponse() {
        int fileClientMessageIndex = 389;
        ClientMessage encoded = ServerAtomicLongGetAndAddCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicLongGetAndSetCodec_decodeRequest() {
        int fileClientMessageIndex = 390;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicLongGetAndSetCodec.RequestParameters parameters = ServerAtomicLongGetAndSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.newValue));
    }

    @Test
    public void test_AtomicLongGetAndSetCodec_encodeResponse() {
        int fileClientMessageIndex = 391;
        ClientMessage encoded = ServerAtomicLongGetAndSetCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicRefApplyCodec_decodeRequest() {
        int fileClientMessageIndex = 392;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicRefApplyCodec.RequestParameters parameters = ServerAtomicRefApplyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.function));
        assertTrue(isEqual(anInt, parameters.returnValueType));
        assertTrue(isEqual(aBoolean, parameters.alter));
    }

    @Test
    public void test_AtomicRefApplyCodec_encodeResponse() {
        int fileClientMessageIndex = 393;
        ClientMessage encoded = ServerAtomicRefApplyCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicRefCompareAndSetCodec_decodeRequest() {
        int fileClientMessageIndex = 394;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicRefCompareAndSetCodec.RequestParameters parameters = ServerAtomicRefCompareAndSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.oldValue));
        assertTrue(isEqual(aData, parameters.newValue));
    }

    @Test
    public void test_AtomicRefCompareAndSetCodec_encodeResponse() {
        int fileClientMessageIndex = 395;
        ClientMessage encoded = ServerAtomicRefCompareAndSetCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicRefContainsCodec_decodeRequest() {
        int fileClientMessageIndex = 396;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicRefContainsCodec.RequestParameters parameters = ServerAtomicRefContainsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_AtomicRefContainsCodec_encodeResponse() {
        int fileClientMessageIndex = 397;
        ClientMessage encoded = ServerAtomicRefContainsCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicRefGetCodec_decodeRequest() {
        int fileClientMessageIndex = 398;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicRefGetCodec.RequestParameters parameters = ServerAtomicRefGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_AtomicRefGetCodec_encodeResponse() {
        int fileClientMessageIndex = 399;
        ClientMessage encoded = ServerAtomicRefGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_AtomicRefSetCodec_decodeRequest() {
        int fileClientMessageIndex = 400;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerAtomicRefSetCodec.RequestParameters parameters = ServerAtomicRefSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.newValue));
        assertTrue(isEqual(aBoolean, parameters.returnOldValue));
    }

    @Test
    public void test_AtomicRefSetCodec_encodeResponse() {
        int fileClientMessageIndex = 401;
        ClientMessage encoded = ServerAtomicRefSetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CountDownLatchTrySetCountCodec_decodeRequest() {
        int fileClientMessageIndex = 402;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCountDownLatchTrySetCountCodec.RequestParameters parameters = ServerCountDownLatchTrySetCountCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.count));
    }

    @Test
    public void test_CountDownLatchTrySetCountCodec_encodeResponse() {
        int fileClientMessageIndex = 403;
        ClientMessage encoded = ServerCountDownLatchTrySetCountCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CountDownLatchAwaitCodec_decodeRequest() {
        int fileClientMessageIndex = 404;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCountDownLatchAwaitCodec.RequestParameters parameters = ServerCountDownLatchAwaitCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(aLong, parameters.timeoutMs));
    }

    @Test
    public void test_CountDownLatchAwaitCodec_encodeResponse() {
        int fileClientMessageIndex = 405;
        ClientMessage encoded = ServerCountDownLatchAwaitCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CountDownLatchCountDownCodec_decodeRequest() {
        int fileClientMessageIndex = 406;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCountDownLatchCountDownCodec.RequestParameters parameters = ServerCountDownLatchCountDownCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(anInt, parameters.expectedRound));
    }

    @Test
    public void test_CountDownLatchCountDownCodec_encodeResponse() {
        int fileClientMessageIndex = 407;
        ClientMessage encoded = ServerCountDownLatchCountDownCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CountDownLatchGetCountCodec_decodeRequest() {
        int fileClientMessageIndex = 408;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCountDownLatchGetCountCodec.RequestParameters parameters = ServerCountDownLatchGetCountCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_CountDownLatchGetCountCodec_encodeResponse() {
        int fileClientMessageIndex = 409;
        ClientMessage encoded = ServerCountDownLatchGetCountCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CountDownLatchGetRoundCodec_decodeRequest() {
        int fileClientMessageIndex = 410;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCountDownLatchGetRoundCodec.RequestParameters parameters = ServerCountDownLatchGetRoundCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_CountDownLatchGetRoundCodec_encodeResponse() {
        int fileClientMessageIndex = 411;
        ClientMessage encoded = ServerCountDownLatchGetRoundCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreInitCodec_decodeRequest() {
        int fileClientMessageIndex = 412;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreInitCodec.RequestParameters parameters = ServerSemaphoreInitCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.permits));
    }

    @Test
    public void test_SemaphoreInitCodec_encodeResponse() {
        int fileClientMessageIndex = 413;
        ClientMessage encoded = ServerSemaphoreInitCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreAcquireCodec_decodeRequest() {
        int fileClientMessageIndex = 414;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreAcquireCodec.RequestParameters parameters = ServerSemaphoreAcquireCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(anInt, parameters.permits));
        assertTrue(isEqual(aLong, parameters.timeoutMs));
    }

    @Test
    public void test_SemaphoreAcquireCodec_encodeResponse() {
        int fileClientMessageIndex = 415;
        ClientMessage encoded = ServerSemaphoreAcquireCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreReleaseCodec_decodeRequest() {
        int fileClientMessageIndex = 416;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreReleaseCodec.RequestParameters parameters = ServerSemaphoreReleaseCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(anInt, parameters.permits));
    }

    @Test
    public void test_SemaphoreReleaseCodec_encodeResponse() {
        int fileClientMessageIndex = 417;
        ClientMessage encoded = ServerSemaphoreReleaseCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreDrainCodec_decodeRequest() {
        int fileClientMessageIndex = 418;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreDrainCodec.RequestParameters parameters = ServerSemaphoreDrainCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
    }

    @Test
    public void test_SemaphoreDrainCodec_encodeResponse() {
        int fileClientMessageIndex = 419;
        ClientMessage encoded = ServerSemaphoreDrainCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreChangeCodec_decodeRequest() {
        int fileClientMessageIndex = 420;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreChangeCodec.RequestParameters parameters = ServerSemaphoreChangeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sessionId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aUUID, parameters.invocationUid));
        assertTrue(isEqual(anInt, parameters.permits));
    }

    @Test
    public void test_SemaphoreChangeCodec_encodeResponse() {
        int fileClientMessageIndex = 421;
        ClientMessage encoded = ServerSemaphoreChangeCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreAvailablePermitsCodec_decodeRequest() {
        int fileClientMessageIndex = 422;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSemaphoreAvailablePermitsCodec.RequestParameters parameters = ServerSemaphoreAvailablePermitsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.name));
    }

    @Test
    public void test_SemaphoreAvailablePermitsCodec_encodeResponse() {
        int fileClientMessageIndex = 423;
        ClientMessage encoded = ServerSemaphoreAvailablePermitsCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SemaphoreGetSemaphoreTypeCodec_decodeRequest() {
        int fileClientMessageIndex = 424;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerSemaphoreGetSemaphoreTypeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SemaphoreGetSemaphoreTypeCodec_encodeResponse() {
        int fileClientMessageIndex = 425;
        ClientMessage encoded = ServerSemaphoreGetSemaphoreTypeCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapPutCodec_decodeRequest() {
        int fileClientMessageIndex = 426;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapPutCodec.RequestParameters parameters = ServerReplicatedMapPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_ReplicatedMapPutCodec_encodeResponse() {
        int fileClientMessageIndex = 427;
        ClientMessage encoded = ServerReplicatedMapPutCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 428;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 429;
        ClientMessage encoded = ServerReplicatedMapSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 430;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapIsEmptyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 431;
        ClientMessage encoded = ServerReplicatedMapIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapContainsKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 432;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapContainsKeyCodec.RequestParameters parameters = ServerReplicatedMapContainsKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_ReplicatedMapContainsKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 433;
        ClientMessage encoded = ServerReplicatedMapContainsKeyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapContainsValueCodec_decodeRequest() {
        int fileClientMessageIndex = 434;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapContainsValueCodec.RequestParameters parameters = ServerReplicatedMapContainsValueCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_ReplicatedMapContainsValueCodec_encodeResponse() {
        int fileClientMessageIndex = 435;
        ClientMessage encoded = ServerReplicatedMapContainsValueCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapGetCodec_decodeRequest() {
        int fileClientMessageIndex = 436;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapGetCodec.RequestParameters parameters = ServerReplicatedMapGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_ReplicatedMapGetCodec_encodeResponse() {
        int fileClientMessageIndex = 437;
        ClientMessage encoded = ServerReplicatedMapGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 438;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapRemoveCodec.RequestParameters parameters = ServerReplicatedMapRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_ReplicatedMapRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 439;
        ClientMessage encoded = ServerReplicatedMapRemoveCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapPutAllCodec_decodeRequest() {
        int fileClientMessageIndex = 440;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapPutAllCodec.RequestParameters parameters = ServerReplicatedMapPutAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfDataToData, parameters.entries));
    }

    @Test
    public void test_ReplicatedMapPutAllCodec_encodeResponse() {
        int fileClientMessageIndex = 441;
        ClientMessage encoded = ServerReplicatedMapPutAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapClearCodec_decodeRequest() {
        int fileClientMessageIndex = 442;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapClearCodec_encodeResponse() {
        int fileClientMessageIndex = 443;
        ClientMessage encoded = ServerReplicatedMapClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 444;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec.RequestParameters parameters = ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 445;
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyWithPredicateCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 446;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 447;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapAddEntryListenerWithPredicateCodec.RequestParameters parameters = ServerReplicatedMapAddEntryListenerWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 448;
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerWithPredicateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerWithPredicateCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 449;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerWithPredicateCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 450;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapAddEntryListenerToKeyCodec.RequestParameters parameters = ServerReplicatedMapAddEntryListenerToKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 451;
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerToKeyCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerToKeyCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 452;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerToKeyCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 453;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapAddEntryListenerCodec.RequestParameters parameters = ServerReplicatedMapAddEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 454;
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddEntryListenerCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 455;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerReplicatedMapAddEntryListenerCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapRemoveEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 456;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapRemoveEntryListenerCodec.RequestParameters parameters = ServerReplicatedMapRemoveEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_ReplicatedMapRemoveEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 457;
        ClientMessage encoded = ServerReplicatedMapRemoveEntryListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapKeySetCodec_decodeRequest() {
        int fileClientMessageIndex = 458;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapKeySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapKeySetCodec_encodeResponse() {
        int fileClientMessageIndex = 459;
        ClientMessage encoded = ServerReplicatedMapKeySetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapValuesCodec_decodeRequest() {
        int fileClientMessageIndex = 460;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapValuesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapValuesCodec_encodeResponse() {
        int fileClientMessageIndex = 461;
        ClientMessage encoded = ServerReplicatedMapValuesCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapEntrySetCodec_decodeRequest() {
        int fileClientMessageIndex = 462;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerReplicatedMapEntrySetCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ReplicatedMapEntrySetCodec_encodeResponse() {
        int fileClientMessageIndex = 463;
        ClientMessage encoded = ServerReplicatedMapEntrySetCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddNearCacheEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 464;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerReplicatedMapAddNearCacheEntryListenerCodec.RequestParameters parameters = ServerReplicatedMapAddNearCacheEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.includeValue));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ReplicatedMapAddNearCacheEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 465;
        ClientMessage encoded = ServerReplicatedMapAddNearCacheEntryListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ReplicatedMapAddNearCacheEntryListenerCodec_encodeEntryEvent() {
        int fileClientMessageIndex = 466;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerReplicatedMapAddNearCacheEntryListenerCodec.encodeEntryEvent(aData, aData, aData, aData, anInt, aUUID, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapContainsKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 467;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapContainsKeyCodec.RequestParameters parameters = ServerTransactionalMapContainsKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMapContainsKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 468;
        ClientMessage encoded = ServerTransactionalMapContainsKeyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapGetCodec_decodeRequest() {
        int fileClientMessageIndex = 469;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapGetCodec.RequestParameters parameters = ServerTransactionalMapGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMapGetCodec_encodeResponse() {
        int fileClientMessageIndex = 470;
        ClientMessage encoded = ServerTransactionalMapGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapGetForUpdateCodec_decodeRequest() {
        int fileClientMessageIndex = 471;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapGetForUpdateCodec.RequestParameters parameters = ServerTransactionalMapGetForUpdateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMapGetForUpdateCodec_encodeResponse() {
        int fileClientMessageIndex = 472;
        ClientMessage encoded = ServerTransactionalMapGetForUpdateCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 473;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapSizeCodec.RequestParameters parameters = ServerTransactionalMapSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalMapSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 474;
        ClientMessage encoded = ServerTransactionalMapSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapIsEmptyCodec_decodeRequest() {
        int fileClientMessageIndex = 475;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapIsEmptyCodec.RequestParameters parameters = ServerTransactionalMapIsEmptyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalMapIsEmptyCodec_encodeResponse() {
        int fileClientMessageIndex = 476;
        ClientMessage encoded = ServerTransactionalMapIsEmptyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapPutCodec_decodeRequest() {
        int fileClientMessageIndex = 477;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapPutCodec.RequestParameters parameters = ServerTransactionalMapPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aLong, parameters.ttl));
    }

    @Test
    public void test_TransactionalMapPutCodec_encodeResponse() {
        int fileClientMessageIndex = 478;
        ClientMessage encoded = ServerTransactionalMapPutCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapSetCodec_decodeRequest() {
        int fileClientMessageIndex = 479;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapSetCodec.RequestParameters parameters = ServerTransactionalMapSetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMapSetCodec_encodeResponse() {
        int fileClientMessageIndex = 480;
        ClientMessage encoded = ServerTransactionalMapSetCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapPutIfAbsentCodec_decodeRequest() {
        int fileClientMessageIndex = 481;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapPutIfAbsentCodec.RequestParameters parameters = ServerTransactionalMapPutIfAbsentCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMapPutIfAbsentCodec_encodeResponse() {
        int fileClientMessageIndex = 482;
        ClientMessage encoded = ServerTransactionalMapPutIfAbsentCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapReplaceCodec_decodeRequest() {
        int fileClientMessageIndex = 483;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapReplaceCodec.RequestParameters parameters = ServerTransactionalMapReplaceCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMapReplaceCodec_encodeResponse() {
        int fileClientMessageIndex = 484;
        ClientMessage encoded = ServerTransactionalMapReplaceCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapReplaceIfSameCodec_decodeRequest() {
        int fileClientMessageIndex = 485;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapReplaceIfSameCodec.RequestParameters parameters = ServerTransactionalMapReplaceIfSameCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.oldValue));
        assertTrue(isEqual(aData, parameters.newValue));
    }

    @Test
    public void test_TransactionalMapReplaceIfSameCodec_encodeResponse() {
        int fileClientMessageIndex = 486;
        ClientMessage encoded = ServerTransactionalMapReplaceIfSameCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 487;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapRemoveCodec.RequestParameters parameters = ServerTransactionalMapRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMapRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 488;
        ClientMessage encoded = ServerTransactionalMapRemoveCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapDeleteCodec_decodeRequest() {
        int fileClientMessageIndex = 489;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapDeleteCodec.RequestParameters parameters = ServerTransactionalMapDeleteCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMapDeleteCodec_encodeResponse() {
        int fileClientMessageIndex = 490;
        ClientMessage encoded = ServerTransactionalMapDeleteCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapRemoveIfSameCodec_decodeRequest() {
        int fileClientMessageIndex = 491;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapRemoveIfSameCodec.RequestParameters parameters = ServerTransactionalMapRemoveIfSameCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMapRemoveIfSameCodec_encodeResponse() {
        int fileClientMessageIndex = 492;
        ClientMessage encoded = ServerTransactionalMapRemoveIfSameCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapKeySetCodec_decodeRequest() {
        int fileClientMessageIndex = 493;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapKeySetCodec.RequestParameters parameters = ServerTransactionalMapKeySetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalMapKeySetCodec_encodeResponse() {
        int fileClientMessageIndex = 494;
        ClientMessage encoded = ServerTransactionalMapKeySetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapKeySetWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 495;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapKeySetWithPredicateCodec.RequestParameters parameters = ServerTransactionalMapKeySetWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_TransactionalMapKeySetWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 496;
        ClientMessage encoded = ServerTransactionalMapKeySetWithPredicateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapValuesCodec_decodeRequest() {
        int fileClientMessageIndex = 497;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapValuesCodec.RequestParameters parameters = ServerTransactionalMapValuesCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalMapValuesCodec_encodeResponse() {
        int fileClientMessageIndex = 498;
        ClientMessage encoded = ServerTransactionalMapValuesCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapValuesWithPredicateCodec_decodeRequest() {
        int fileClientMessageIndex = 499;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapValuesWithPredicateCodec.RequestParameters parameters = ServerTransactionalMapValuesWithPredicateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.predicate));
    }

    @Test
    public void test_TransactionalMapValuesWithPredicateCodec_encodeResponse() {
        int fileClientMessageIndex = 500;
        ClientMessage encoded = ServerTransactionalMapValuesWithPredicateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMapContainsValueCodec_decodeRequest() {
        int fileClientMessageIndex = 501;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMapContainsValueCodec.RequestParameters parameters = ServerTransactionalMapContainsValueCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMapContainsValueCodec_encodeResponse() {
        int fileClientMessageIndex = 502;
        ClientMessage encoded = ServerTransactionalMapContainsValueCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapPutCodec_decodeRequest() {
        int fileClientMessageIndex = 503;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapPutCodec.RequestParameters parameters = ServerTransactionalMultiMapPutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMultiMapPutCodec_encodeResponse() {
        int fileClientMessageIndex = 504;
        ClientMessage encoded = ServerTransactionalMultiMapPutCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapGetCodec_decodeRequest() {
        int fileClientMessageIndex = 505;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapGetCodec.RequestParameters parameters = ServerTransactionalMultiMapGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMultiMapGetCodec_encodeResponse() {
        int fileClientMessageIndex = 506;
        ClientMessage encoded = ServerTransactionalMultiMapGetCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 507;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapRemoveCodec.RequestParameters parameters = ServerTransactionalMultiMapRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMultiMapRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 508;
        ClientMessage encoded = ServerTransactionalMultiMapRemoveCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapRemoveEntryCodec_decodeRequest() {
        int fileClientMessageIndex = 509;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapRemoveEntryCodec.RequestParameters parameters = ServerTransactionalMultiMapRemoveEntryCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_TransactionalMultiMapRemoveEntryCodec_encodeResponse() {
        int fileClientMessageIndex = 510;
        ClientMessage encoded = ServerTransactionalMultiMapRemoveEntryCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapValueCountCodec_decodeRequest() {
        int fileClientMessageIndex = 511;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapValueCountCodec.RequestParameters parameters = ServerTransactionalMultiMapValueCountCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_TransactionalMultiMapValueCountCodec_encodeResponse() {
        int fileClientMessageIndex = 512;
        ClientMessage encoded = ServerTransactionalMultiMapValueCountCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalMultiMapSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 513;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalMultiMapSizeCodec.RequestParameters parameters = ServerTransactionalMultiMapSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalMultiMapSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 514;
        ClientMessage encoded = ServerTransactionalMultiMapSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalSetAddCodec_decodeRequest() {
        int fileClientMessageIndex = 515;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalSetAddCodec.RequestParameters parameters = ServerTransactionalSetAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.item));
    }

    @Test
    public void test_TransactionalSetAddCodec_encodeResponse() {
        int fileClientMessageIndex = 516;
        ClientMessage encoded = ServerTransactionalSetAddCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalSetRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 517;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalSetRemoveCodec.RequestParameters parameters = ServerTransactionalSetRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.item));
    }

    @Test
    public void test_TransactionalSetRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 518;
        ClientMessage encoded = ServerTransactionalSetRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalSetSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 519;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalSetSizeCodec.RequestParameters parameters = ServerTransactionalSetSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalSetSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 520;
        ClientMessage encoded = ServerTransactionalSetSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalListAddCodec_decodeRequest() {
        int fileClientMessageIndex = 521;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalListAddCodec.RequestParameters parameters = ServerTransactionalListAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.item));
    }

    @Test
    public void test_TransactionalListAddCodec_encodeResponse() {
        int fileClientMessageIndex = 522;
        ClientMessage encoded = ServerTransactionalListAddCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalListRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 523;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalListRemoveCodec.RequestParameters parameters = ServerTransactionalListRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.item));
    }

    @Test
    public void test_TransactionalListRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 524;
        ClientMessage encoded = ServerTransactionalListRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalListSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 525;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalListSizeCodec.RequestParameters parameters = ServerTransactionalListSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalListSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 526;
        ClientMessage encoded = ServerTransactionalListSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalQueueOfferCodec_decodeRequest() {
        int fileClientMessageIndex = 527;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalQueueOfferCodec.RequestParameters parameters = ServerTransactionalQueueOfferCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aData, parameters.item));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_TransactionalQueueOfferCodec_encodeResponse() {
        int fileClientMessageIndex = 528;
        ClientMessage encoded = ServerTransactionalQueueOfferCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalQueueTakeCodec_decodeRequest() {
        int fileClientMessageIndex = 529;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalQueueTakeCodec.RequestParameters parameters = ServerTransactionalQueueTakeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalQueueTakeCodec_encodeResponse() {
        int fileClientMessageIndex = 530;
        ClientMessage encoded = ServerTransactionalQueueTakeCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalQueuePollCodec_decodeRequest() {
        int fileClientMessageIndex = 531;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalQueuePollCodec.RequestParameters parameters = ServerTransactionalQueuePollCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_TransactionalQueuePollCodec_encodeResponse() {
        int fileClientMessageIndex = 532;
        ClientMessage encoded = ServerTransactionalQueuePollCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalQueuePeekCodec_decodeRequest() {
        int fileClientMessageIndex = 533;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalQueuePeekCodec.RequestParameters parameters = ServerTransactionalQueuePeekCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_TransactionalQueuePeekCodec_encodeResponse() {
        int fileClientMessageIndex = 534;
        ClientMessage encoded = ServerTransactionalQueuePeekCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionalQueueSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 535;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionalQueueSizeCodec.RequestParameters parameters = ServerTransactionalQueueSizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.txnId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionalQueueSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 536;
        ClientMessage encoded = ServerTransactionalQueueSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 537;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheAddEntryListenerCodec.RequestParameters parameters = ServerCacheAddEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_CacheAddEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 538;
        ClientMessage encoded = ServerCacheAddEntryListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddEntryListenerCodec_encodeCacheEvent() {
        int fileClientMessageIndex = 539;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCacheAddEntryListenerCodec.encodeCacheEvent(anInt, aListOfCacheEventData, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheClearCodec_decodeRequest() {
        int fileClientMessageIndex = 540;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCacheClearCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CacheClearCodec_encodeResponse() {
        int fileClientMessageIndex = 541;
        ClientMessage encoded = ServerCacheClearCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemoveAllKeysCodec_decodeRequest() {
        int fileClientMessageIndex = 542;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemoveAllKeysCodec.RequestParameters parameters = ServerCacheRemoveAllKeysCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheRemoveAllKeysCodec_encodeResponse() {
        int fileClientMessageIndex = 543;
        ClientMessage encoded = ServerCacheRemoveAllKeysCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemoveAllCodec_decodeRequest() {
        int fileClientMessageIndex = 544;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemoveAllCodec.RequestParameters parameters = ServerCacheRemoveAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheRemoveAllCodec_encodeResponse() {
        int fileClientMessageIndex = 545;
        ClientMessage encoded = ServerCacheRemoveAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheContainsKeyCodec_decodeRequest() {
        int fileClientMessageIndex = 546;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheContainsKeyCodec.RequestParameters parameters = ServerCacheContainsKeyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
    }

    @Test
    public void test_CacheContainsKeyCodec_encodeResponse() {
        int fileClientMessageIndex = 547;
        ClientMessage encoded = ServerCacheContainsKeyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheCreateConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 548;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheCreateConfigCodec.RequestParameters parameters = ServerCacheCreateConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aCacheConfigHolder, parameters.cacheConfig));
        assertTrue(isEqual(aBoolean, parameters.createAlsoOnOthers));
    }

    @Test
    public void test_CacheCreateConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 549;
        ClientMessage encoded = ServerCacheCreateConfigCodec.encodeResponse(aCacheConfigHolder);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheDestroyCodec_decodeRequest() {
        int fileClientMessageIndex = 550;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCacheDestroyCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CacheDestroyCodec_encodeResponse() {
        int fileClientMessageIndex = 551;
        ClientMessage encoded = ServerCacheDestroyCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheEntryProcessorCodec_decodeRequest() {
        int fileClientMessageIndex = 552;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheEntryProcessorCodec.RequestParameters parameters = ServerCacheEntryProcessorCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.entryProcessor));
        assertTrue(isEqual(aListOfData, parameters.arguments));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheEntryProcessorCodec_encodeResponse() {
        int fileClientMessageIndex = 553;
        ClientMessage encoded = ServerCacheEntryProcessorCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheGetAllCodec_decodeRequest() {
        int fileClientMessageIndex = 554;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheGetAllCodec.RequestParameters parameters = ServerCacheGetAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
    }

    @Test
    public void test_CacheGetAllCodec_encodeResponse() {
        int fileClientMessageIndex = 555;
        ClientMessage encoded = ServerCacheGetAllCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheGetAndRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 556;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheGetAndRemoveCodec.RequestParameters parameters = ServerCacheGetAndRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheGetAndRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 557;
        ClientMessage encoded = ServerCacheGetAndRemoveCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheGetAndReplaceCodec_decodeRequest() {
        int fileClientMessageIndex = 558;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheGetAndReplaceCodec.RequestParameters parameters = ServerCacheGetAndReplaceCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheGetAndReplaceCodec_encodeResponse() {
        int fileClientMessageIndex = 559;
        ClientMessage encoded = ServerCacheGetAndReplaceCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheGetConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 560;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheGetConfigCodec.RequestParameters parameters = ServerCacheGetConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.simpleName));
    }

    @Test
    public void test_CacheGetConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 561;
        ClientMessage encoded = ServerCacheGetConfigCodec.encodeResponse(aCacheConfigHolder);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheGetCodec_decodeRequest() {
        int fileClientMessageIndex = 562;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheGetCodec.RequestParameters parameters = ServerCacheGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
    }

    @Test
    public void test_CacheGetCodec_encodeResponse() {
        int fileClientMessageIndex = 563;
        ClientMessage encoded = ServerCacheGetCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheIterateCodec_decodeRequest() {
        int fileClientMessageIndex = 564;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheIterateCodec.RequestParameters parameters = ServerCacheIterateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfIntegerToInteger, parameters.iterationPointers));
        assertTrue(isEqual(anInt, parameters.batch));
    }

    @Test
    public void test_CacheIterateCodec_encodeResponse() {
        int fileClientMessageIndex = 565;
        ClientMessage encoded = ServerCacheIterateCodec.encodeResponse(aListOfIntegerToInteger, aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheListenerRegistrationCodec_decodeRequest() {
        int fileClientMessageIndex = 566;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheListenerRegistrationCodec.RequestParameters parameters = ServerCacheListenerRegistrationCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.listenerConfig));
        assertTrue(isEqual(aBoolean, parameters.shouldRegister));
        assertTrue(isEqual(aUUID, parameters.uuid));
    }

    @Test
    public void test_CacheListenerRegistrationCodec_encodeResponse() {
        int fileClientMessageIndex = 567;
        ClientMessage encoded = ServerCacheListenerRegistrationCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheLoadAllCodec_decodeRequest() {
        int fileClientMessageIndex = 568;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheLoadAllCodec.RequestParameters parameters = ServerCacheLoadAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
        assertTrue(isEqual(aBoolean, parameters.replaceExistingValues));
    }

    @Test
    public void test_CacheLoadAllCodec_encodeResponse() {
        int fileClientMessageIndex = 569;
        ClientMessage encoded = ServerCacheLoadAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheManagementConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 570;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheManagementConfigCodec.RequestParameters parameters = ServerCacheManagementConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.isStat));
        assertTrue(isEqual(aBoolean, parameters.enabled));
        assertTrue(isEqual(aUUID, parameters.uuid));
    }

    @Test
    public void test_CacheManagementConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 571;
        ClientMessage encoded = ServerCacheManagementConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CachePutIfAbsentCodec_decodeRequest() {
        int fileClientMessageIndex = 572;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCachePutIfAbsentCodec.RequestParameters parameters = ServerCachePutIfAbsentCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CachePutIfAbsentCodec_encodeResponse() {
        int fileClientMessageIndex = 573;
        ClientMessage encoded = ServerCachePutIfAbsentCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CachePutCodec_decodeRequest() {
        int fileClientMessageIndex = 574;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCachePutCodec.RequestParameters parameters = ServerCachePutCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.value));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
        assertTrue(isEqual(aBoolean, parameters.get));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CachePutCodec_encodeResponse() {
        int fileClientMessageIndex = 575;
        ClientMessage encoded = ServerCachePutCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemoveEntryListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 576;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemoveEntryListenerCodec.RequestParameters parameters = ServerCacheRemoveEntryListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_CacheRemoveEntryListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 577;
        ClientMessage encoded = ServerCacheRemoveEntryListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemoveInvalidationListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 578;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemoveInvalidationListenerCodec.RequestParameters parameters = ServerCacheRemoveInvalidationListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_CacheRemoveInvalidationListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 579;
        ClientMessage encoded = ServerCacheRemoveInvalidationListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemoveCodec_decodeRequest() {
        int fileClientMessageIndex = 580;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemoveCodec.RequestParameters parameters = ServerCacheRemoveCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.currentValue));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheRemoveCodec_encodeResponse() {
        int fileClientMessageIndex = 581;
        ClientMessage encoded = ServerCacheRemoveCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheReplaceCodec_decodeRequest() {
        int fileClientMessageIndex = 582;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheReplaceCodec.RequestParameters parameters = ServerCacheReplaceCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.key));
        assertTrue(isEqual(aData, parameters.oldValue));
        assertTrue(isEqual(aData, parameters.newValue));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CacheReplaceCodec_encodeResponse() {
        int fileClientMessageIndex = 583;
        ClientMessage encoded = ServerCacheReplaceCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 584;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCacheSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CacheSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 585;
        ClientMessage encoded = ServerCacheSizeCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddPartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 586;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheAddPartitionLostListenerCodec.RequestParameters parameters = ServerCacheAddPartitionLostListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_CacheAddPartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 587;
        ClientMessage encoded = ServerCacheAddPartitionLostListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddPartitionLostListenerCodec_encodeCachePartitionLostEvent() {
        int fileClientMessageIndex = 588;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCacheAddPartitionLostListenerCodec.encodeCachePartitionLostEvent(anInt, aUUID);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheRemovePartitionLostListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 589;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheRemovePartitionLostListenerCodec.RequestParameters parameters = ServerCacheRemovePartitionLostListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aUUID, parameters.registrationId));
    }

    @Test
    public void test_CacheRemovePartitionLostListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 590;
        ClientMessage encoded = ServerCacheRemovePartitionLostListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CachePutAllCodec_decodeRequest() {
        int fileClientMessageIndex = 591;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCachePutAllCodec.RequestParameters parameters = ServerCachePutAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfDataToData, parameters.entries));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
        assertTrue(isEqual(anInt, parameters.completionId));
    }

    @Test
    public void test_CachePutAllCodec_encodeResponse() {
        int fileClientMessageIndex = 592;
        ClientMessage encoded = ServerCachePutAllCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheIterateEntriesCodec_decodeRequest() {
        int fileClientMessageIndex = 593;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheIterateEntriesCodec.RequestParameters parameters = ServerCacheIterateEntriesCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfIntegerToInteger, parameters.iterationPointers));
        assertTrue(isEqual(anInt, parameters.batch));
    }

    @Test
    public void test_CacheIterateEntriesCodec_encodeResponse() {
        int fileClientMessageIndex = 594;
        ClientMessage encoded = ServerCacheIterateEntriesCodec.encodeResponse(aListOfIntegerToInteger, aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddNearCacheInvalidationListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 595;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheAddNearCacheInvalidationListenerCodec.RequestParameters parameters = ServerCacheAddNearCacheInvalidationListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_CacheAddNearCacheInvalidationListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 596;
        ClientMessage encoded = ServerCacheAddNearCacheInvalidationListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddNearCacheInvalidationListenerCodec_encodeCacheInvalidationEvent() {
        int fileClientMessageIndex = 597;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCacheAddNearCacheInvalidationListenerCodec.encodeCacheInvalidationEvent(aString, aData, aUUID, aUUID, aLong);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheAddNearCacheInvalidationListenerCodec_encodeCacheBatchInvalidationEvent() {
        int fileClientMessageIndex = 598;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCacheAddNearCacheInvalidationListenerCodec.encodeCacheBatchInvalidationEvent(aString, aListOfData, aListOfUUIDs, aListOfUUIDs, aListOfLongs);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheFetchNearCacheInvalidationMetadataCodec_decodeRequest() {
        int fileClientMessageIndex = 599;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheFetchNearCacheInvalidationMetadataCodec.RequestParameters parameters = ServerCacheFetchNearCacheInvalidationMetadataCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aListOfStrings, parameters.names));
        assertTrue(isEqual(aUUID, parameters.uuid));
    }

    @Test
    public void test_CacheFetchNearCacheInvalidationMetadataCodec_encodeResponse() {
        int fileClientMessageIndex = 600;
        ClientMessage encoded = ServerCacheFetchNearCacheInvalidationMetadataCodec.encodeResponse(aListOfStringToListOfIntegerToLong, aListOfIntegerToUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheEventJournalSubscribeCodec_decodeRequest() {
        int fileClientMessageIndex = 601;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCacheEventJournalSubscribeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CacheEventJournalSubscribeCodec_encodeResponse() {
        int fileClientMessageIndex = 602;
        ClientMessage encoded = ServerCacheEventJournalSubscribeCodec.encodeResponse(aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheEventJournalReadCodec_decodeRequest() {
        int fileClientMessageIndex = 603;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheEventJournalReadCodec.RequestParameters parameters = ServerCacheEventJournalReadCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.startSequence));
        assertTrue(isEqual(anInt, parameters.minSize));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(aData, parameters.projection));
    }

    @Test
    public void test_CacheEventJournalReadCodec_encodeResponse() {
        int fileClientMessageIndex = 604;
        ClientMessage encoded = ServerCacheEventJournalReadCodec.encodeResponse(anInt, aListOfData, aLongArray, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CacheSetExpiryPolicyCodec_decodeRequest() {
        int fileClientMessageIndex = 605;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCacheSetExpiryPolicyCodec.RequestParameters parameters = ServerCacheSetExpiryPolicyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.keys));
        assertTrue(isEqual(aData, parameters.expiryPolicy));
    }

    @Test
    public void test_CacheSetExpiryPolicyCodec_encodeResponse() {
        int fileClientMessageIndex = 606;
        ClientMessage encoded = ServerCacheSetExpiryPolicyCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionClearRemoteCodec_decodeRequest() {
        int fileClientMessageIndex = 607;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(anXid, ServerXATransactionClearRemoteCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_XATransactionClearRemoteCodec_encodeResponse() {
        int fileClientMessageIndex = 608;
        ClientMessage encoded = ServerXATransactionClearRemoteCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionCollectTransactionsCodec_decodeRequest() {
        int fileClientMessageIndex = 609;
    }

    @Test
    public void test_XATransactionCollectTransactionsCodec_encodeResponse() {
        int fileClientMessageIndex = 610;
        ClientMessage encoded = ServerXATransactionCollectTransactionsCodec.encodeResponse(aListOfXids);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionFinalizeCodec_decodeRequest() {
        int fileClientMessageIndex = 611;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerXATransactionFinalizeCodec.RequestParameters parameters = ServerXATransactionFinalizeCodec.decodeRequest(fromFile);
        assertTrue(isEqual(anXid, parameters.xid));
        assertTrue(isEqual(aBoolean, parameters.isCommit));
    }

    @Test
    public void test_XATransactionFinalizeCodec_encodeResponse() {
        int fileClientMessageIndex = 612;
        ClientMessage encoded = ServerXATransactionFinalizeCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionCommitCodec_decodeRequest() {
        int fileClientMessageIndex = 613;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerXATransactionCommitCodec.RequestParameters parameters = ServerXATransactionCommitCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.transactionId));
        assertTrue(isEqual(aBoolean, parameters.onePhase));
    }

    @Test
    public void test_XATransactionCommitCodec_encodeResponse() {
        int fileClientMessageIndex = 614;
        ClientMessage encoded = ServerXATransactionCommitCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionCreateCodec_decodeRequest() {
        int fileClientMessageIndex = 615;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerXATransactionCreateCodec.RequestParameters parameters = ServerXATransactionCreateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(anXid, parameters.xid));
        assertTrue(isEqual(aLong, parameters.timeout));
    }

    @Test
    public void test_XATransactionCreateCodec_encodeResponse() {
        int fileClientMessageIndex = 616;
        ClientMessage encoded = ServerXATransactionCreateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionPrepareCodec_decodeRequest() {
        int fileClientMessageIndex = 617;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerXATransactionPrepareCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_XATransactionPrepareCodec_encodeResponse() {
        int fileClientMessageIndex = 618;
        ClientMessage encoded = ServerXATransactionPrepareCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_XATransactionRollbackCodec_decodeRequest() {
        int fileClientMessageIndex = 619;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerXATransactionRollbackCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_XATransactionRollbackCodec_encodeResponse() {
        int fileClientMessageIndex = 620;
        ClientMessage encoded = ServerXATransactionRollbackCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionCommitCodec_decodeRequest() {
        int fileClientMessageIndex = 621;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionCommitCodec.RequestParameters parameters = ServerTransactionCommitCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.transactionId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionCommitCodec_encodeResponse() {
        int fileClientMessageIndex = 622;
        ClientMessage encoded = ServerTransactionCommitCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionCreateCodec_decodeRequest() {
        int fileClientMessageIndex = 623;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionCreateCodec.RequestParameters parameters = ServerTransactionCreateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aLong, parameters.timeout));
        assertTrue(isEqual(anInt, parameters.durability));
        assertTrue(isEqual(anInt, parameters.transactionType));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionCreateCodec_encodeResponse() {
        int fileClientMessageIndex = 624;
        ClientMessage encoded = ServerTransactionCreateCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_TransactionRollbackCodec_decodeRequest() {
        int fileClientMessageIndex = 625;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerTransactionRollbackCodec.RequestParameters parameters = ServerTransactionRollbackCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.transactionId));
        assertTrue(isEqual(aLong, parameters.threadId));
    }

    @Test
    public void test_TransactionRollbackCodec_encodeResponse() {
        int fileClientMessageIndex = 626;
        ClientMessage encoded = ServerTransactionRollbackCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryPublisherCreateWithValueCodec_decodeRequest() {
        int fileClientMessageIndex = 627;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQueryPublisherCreateWithValueCodec.RequestParameters parameters = ServerContinuousQueryPublisherCreateWithValueCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(aString, parameters.cacheName));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(anInt, parameters.batchSize));
        assertTrue(isEqual(anInt, parameters.bufferSize));
        assertTrue(isEqual(aLong, parameters.delaySeconds));
        assertTrue(isEqual(aBoolean, parameters.populate));
        assertTrue(isEqual(aBoolean, parameters.coalesce));
    }

    @Test
    public void test_ContinuousQueryPublisherCreateWithValueCodec_encodeResponse() {
        int fileClientMessageIndex = 628;
        ClientMessage encoded = ServerContinuousQueryPublisherCreateWithValueCodec.encodeResponse(aListOfDataToData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryPublisherCreateCodec_decodeRequest() {
        int fileClientMessageIndex = 629;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQueryPublisherCreateCodec.RequestParameters parameters = ServerContinuousQueryPublisherCreateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(aString, parameters.cacheName));
        assertTrue(isEqual(aData, parameters.predicate));
        assertTrue(isEqual(anInt, parameters.batchSize));
        assertTrue(isEqual(anInt, parameters.bufferSize));
        assertTrue(isEqual(aLong, parameters.delaySeconds));
        assertTrue(isEqual(aBoolean, parameters.populate));
        assertTrue(isEqual(aBoolean, parameters.coalesce));
    }

    @Test
    public void test_ContinuousQueryPublisherCreateCodec_encodeResponse() {
        int fileClientMessageIndex = 630;
        ClientMessage encoded = ServerContinuousQueryPublisherCreateCodec.encodeResponse(aListOfData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryMadePublishableCodec_decodeRequest() {
        int fileClientMessageIndex = 631;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQueryMadePublishableCodec.RequestParameters parameters = ServerContinuousQueryMadePublishableCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(aString, parameters.cacheName));
    }

    @Test
    public void test_ContinuousQueryMadePublishableCodec_encodeResponse() {
        int fileClientMessageIndex = 632;
        ClientMessage encoded = ServerContinuousQueryMadePublishableCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryAddListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 633;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQueryAddListenerCodec.RequestParameters parameters = ServerContinuousQueryAddListenerCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.listenerName));
        assertTrue(isEqual(aBoolean, parameters.localOnly));
    }

    @Test
    public void test_ContinuousQueryAddListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 634;
        ClientMessage encoded = ServerContinuousQueryAddListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryAddListenerCodec_encodeQueryCacheSingleEvent() {
        int fileClientMessageIndex = 635;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerContinuousQueryAddListenerCodec.encodeQueryCacheSingleEvent(aQueryCacheEventData);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryAddListenerCodec_encodeQueryCacheBatchEvent() {
        int fileClientMessageIndex = 636;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerContinuousQueryAddListenerCodec.encodeQueryCacheBatchEvent(aListOfQueryCacheEventData, aString, anInt);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQuerySetReadCursorCodec_decodeRequest() {
        int fileClientMessageIndex = 637;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQuerySetReadCursorCodec.RequestParameters parameters = ServerContinuousQuerySetReadCursorCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(aString, parameters.cacheName));
        assertTrue(isEqual(aLong, parameters.sequence));
    }

    @Test
    public void test_ContinuousQuerySetReadCursorCodec_encodeResponse() {
        int fileClientMessageIndex = 638;
        ClientMessage encoded = ServerContinuousQuerySetReadCursorCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ContinuousQueryDestroyCacheCodec_decodeRequest() {
        int fileClientMessageIndex = 639;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerContinuousQueryDestroyCacheCodec.RequestParameters parameters = ServerContinuousQueryDestroyCacheCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(aString, parameters.cacheName));
    }

    @Test
    public void test_ContinuousQueryDestroyCacheCodec_encodeResponse() {
        int fileClientMessageIndex = 640;
        ClientMessage encoded = ServerContinuousQueryDestroyCacheCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferSizeCodec_decodeRequest() {
        int fileClientMessageIndex = 641;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerRingbufferSizeCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_RingbufferSizeCodec_encodeResponse() {
        int fileClientMessageIndex = 642;
        ClientMessage encoded = ServerRingbufferSizeCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferTailSequenceCodec_decodeRequest() {
        int fileClientMessageIndex = 643;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerRingbufferTailSequenceCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_RingbufferTailSequenceCodec_encodeResponse() {
        int fileClientMessageIndex = 644;
        ClientMessage encoded = ServerRingbufferTailSequenceCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferHeadSequenceCodec_decodeRequest() {
        int fileClientMessageIndex = 645;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerRingbufferHeadSequenceCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_RingbufferHeadSequenceCodec_encodeResponse() {
        int fileClientMessageIndex = 646;
        ClientMessage encoded = ServerRingbufferHeadSequenceCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferCapacityCodec_decodeRequest() {
        int fileClientMessageIndex = 647;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerRingbufferCapacityCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_RingbufferCapacityCodec_encodeResponse() {
        int fileClientMessageIndex = 648;
        ClientMessage encoded = ServerRingbufferCapacityCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferRemainingCapacityCodec_decodeRequest() {
        int fileClientMessageIndex = 649;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerRingbufferRemainingCapacityCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_RingbufferRemainingCapacityCodec_encodeResponse() {
        int fileClientMessageIndex = 650;
        ClientMessage encoded = ServerRingbufferRemainingCapacityCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferAddCodec_decodeRequest() {
        int fileClientMessageIndex = 651;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerRingbufferAddCodec.RequestParameters parameters = ServerRingbufferAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.overflowPolicy));
        assertTrue(isEqual(aData, parameters.value));
    }

    @Test
    public void test_RingbufferAddCodec_encodeResponse() {
        int fileClientMessageIndex = 652;
        ClientMessage encoded = ServerRingbufferAddCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferReadOneCodec_decodeRequest() {
        int fileClientMessageIndex = 653;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerRingbufferReadOneCodec.RequestParameters parameters = ServerRingbufferReadOneCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.sequence));
    }

    @Test
    public void test_RingbufferReadOneCodec_encodeResponse() {
        int fileClientMessageIndex = 654;
        ClientMessage encoded = ServerRingbufferReadOneCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferAddAllCodec_decodeRequest() {
        int fileClientMessageIndex = 655;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerRingbufferAddAllCodec.RequestParameters parameters = ServerRingbufferAddAllCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfData, parameters.valueList));
        assertTrue(isEqual(anInt, parameters.overflowPolicy));
    }

    @Test
    public void test_RingbufferAddAllCodec_encodeResponse() {
        int fileClientMessageIndex = 656;
        ClientMessage encoded = ServerRingbufferAddAllCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_RingbufferReadManyCodec_decodeRequest() {
        int fileClientMessageIndex = 657;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerRingbufferReadManyCodec.RequestParameters parameters = ServerRingbufferReadManyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.startSequence));
        assertTrue(isEqual(anInt, parameters.minCount));
        assertTrue(isEqual(anInt, parameters.maxCount));
        assertTrue(isEqual(aData, parameters.filter));
    }

    @Test
    public void test_RingbufferReadManyCodec_encodeResponse() {
        int fileClientMessageIndex = 658;
        ClientMessage encoded = ServerRingbufferReadManyCodec.encodeResponse(anInt, aListOfData, aLongArray, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorShutdownCodec_decodeRequest() {
        int fileClientMessageIndex = 659;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerDurableExecutorShutdownCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_DurableExecutorShutdownCodec_encodeResponse() {
        int fileClientMessageIndex = 660;
        ClientMessage encoded = ServerDurableExecutorShutdownCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorIsShutdownCodec_decodeRequest() {
        int fileClientMessageIndex = 661;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerDurableExecutorIsShutdownCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_DurableExecutorIsShutdownCodec_encodeResponse() {
        int fileClientMessageIndex = 662;
        ClientMessage encoded = ServerDurableExecutorIsShutdownCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorSubmitToPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 663;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDurableExecutorSubmitToPartitionCodec.RequestParameters parameters = ServerDurableExecutorSubmitToPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aData, parameters.callable));
    }

    @Test
    public void test_DurableExecutorSubmitToPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 664;
        ClientMessage encoded = ServerDurableExecutorSubmitToPartitionCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorRetrieveResultCodec_decodeRequest() {
        int fileClientMessageIndex = 665;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDurableExecutorRetrieveResultCodec.RequestParameters parameters = ServerDurableExecutorRetrieveResultCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.sequence));
    }

    @Test
    public void test_DurableExecutorRetrieveResultCodec_encodeResponse() {
        int fileClientMessageIndex = 666;
        ClientMessage encoded = ServerDurableExecutorRetrieveResultCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorDisposeResultCodec_decodeRequest() {
        int fileClientMessageIndex = 667;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDurableExecutorDisposeResultCodec.RequestParameters parameters = ServerDurableExecutorDisposeResultCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.sequence));
    }

    @Test
    public void test_DurableExecutorDisposeResultCodec_encodeResponse() {
        int fileClientMessageIndex = 668;
        ClientMessage encoded = ServerDurableExecutorDisposeResultCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DurableExecutorRetrieveAndDisposeResultCodec_decodeRequest() {
        int fileClientMessageIndex = 669;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDurableExecutorRetrieveAndDisposeResultCodec.RequestParameters parameters = ServerDurableExecutorRetrieveAndDisposeResultCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.sequence));
    }

    @Test
    public void test_DurableExecutorRetrieveAndDisposeResultCodec_encodeResponse() {
        int fileClientMessageIndex = 670;
        ClientMessage encoded = ServerDurableExecutorRetrieveAndDisposeResultCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CardinalityEstimatorAddCodec_decodeRequest() {
        int fileClientMessageIndex = 671;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCardinalityEstimatorAddCodec.RequestParameters parameters = ServerCardinalityEstimatorAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.hash));
    }

    @Test
    public void test_CardinalityEstimatorAddCodec_encodeResponse() {
        int fileClientMessageIndex = 672;
        ClientMessage encoded = ServerCardinalityEstimatorAddCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CardinalityEstimatorEstimateCodec_decodeRequest() {
        int fileClientMessageIndex = 673;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCardinalityEstimatorEstimateCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CardinalityEstimatorEstimateCodec_encodeResponse() {
        int fileClientMessageIndex = 674;
        ClientMessage encoded = ServerCardinalityEstimatorEstimateCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorShutdownCodec_decodeRequest() {
        int fileClientMessageIndex = 675;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorShutdownCodec.RequestParameters parameters = ServerScheduledExecutorShutdownCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorShutdownCodec_encodeResponse() {
        int fileClientMessageIndex = 676;
        ClientMessage encoded = ServerScheduledExecutorShutdownCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorSubmitToPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 677;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorSubmitToPartitionCodec.RequestParameters parameters = ServerScheduledExecutorSubmitToPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aByte, parameters.type));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aData, parameters.task));
        assertTrue(isEqual(aLong, parameters.initialDelayInMillis));
        assertTrue(isEqual(aLong, parameters.periodInMillis));
        assertTrue(parameters.isAutoDisposableExists);
        assertTrue(isEqual(aBoolean, parameters.autoDisposable));
    }

    @Test
    public void test_ScheduledExecutorSubmitToPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 678;
        ClientMessage encoded = ServerScheduledExecutorSubmitToPartitionCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorSubmitToMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 679;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorSubmitToMemberCodec.RequestParameters parameters = ServerScheduledExecutorSubmitToMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
        assertTrue(isEqual(aByte, parameters.type));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aData, parameters.task));
        assertTrue(isEqual(aLong, parameters.initialDelayInMillis));
        assertTrue(isEqual(aLong, parameters.periodInMillis));
        assertTrue(parameters.isAutoDisposableExists);
        assertTrue(isEqual(aBoolean, parameters.autoDisposable));
    }

    @Test
    public void test_ScheduledExecutorSubmitToMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 680;
        ClientMessage encoded = ServerScheduledExecutorSubmitToMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetAllScheduledFuturesCodec_decodeRequest() {
        int fileClientMessageIndex = 681;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerScheduledExecutorGetAllScheduledFuturesCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_ScheduledExecutorGetAllScheduledFuturesCodec_encodeResponse() {
        int fileClientMessageIndex = 682;
        ClientMessage encoded = ServerScheduledExecutorGetAllScheduledFuturesCodec.encodeResponse(aListOfScheduledTaskHandler);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetStatsFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 683;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetStatsFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorGetStatsFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorGetStatsFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 684;
        ClientMessage encoded = ServerScheduledExecutorGetStatsFromPartitionCodec.encodeResponse(aLong, aLong, aLong, aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetStatsFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 685;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetStatsFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorGetStatsFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorGetStatsFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 686;
        ClientMessage encoded = ServerScheduledExecutorGetStatsFromMemberCodec.encodeResponse(aLong, aLong, aLong, aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetDelayFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 687;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetDelayFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorGetDelayFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorGetDelayFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 688;
        ClientMessage encoded = ServerScheduledExecutorGetDelayFromPartitionCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetDelayFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 689;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetDelayFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorGetDelayFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorGetDelayFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 690;
        ClientMessage encoded = ServerScheduledExecutorGetDelayFromMemberCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorCancelFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 691;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorCancelFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorCancelFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aBoolean, parameters.mayInterruptIfRunning));
    }

    @Test
    public void test_ScheduledExecutorCancelFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 692;
        ClientMessage encoded = ServerScheduledExecutorCancelFromPartitionCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorCancelFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 693;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorCancelFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorCancelFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
        assertTrue(isEqual(aBoolean, parameters.mayInterruptIfRunning));
    }

    @Test
    public void test_ScheduledExecutorCancelFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 694;
        ClientMessage encoded = ServerScheduledExecutorCancelFromMemberCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorIsCancelledFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 695;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorIsCancelledFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorIsCancelledFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorIsCancelledFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 696;
        ClientMessage encoded = ServerScheduledExecutorIsCancelledFromPartitionCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorIsCancelledFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 697;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorIsCancelledFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorIsCancelledFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorIsCancelledFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 698;
        ClientMessage encoded = ServerScheduledExecutorIsCancelledFromMemberCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorIsDoneFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 699;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorIsDoneFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorIsDoneFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorIsDoneFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 700;
        ClientMessage encoded = ServerScheduledExecutorIsDoneFromPartitionCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorIsDoneFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 701;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorIsDoneFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorIsDoneFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorIsDoneFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 702;
        ClientMessage encoded = ServerScheduledExecutorIsDoneFromMemberCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetResultFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 703;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetResultFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorGetResultFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorGetResultFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 704;
        ClientMessage encoded = ServerScheduledExecutorGetResultFromPartitionCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorGetResultFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 705;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorGetResultFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorGetResultFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorGetResultFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 706;
        ClientMessage encoded = ServerScheduledExecutorGetResultFromMemberCodec.encodeResponse(aData);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorDisposeFromPartitionCodec_decodeRequest() {
        int fileClientMessageIndex = 707;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorDisposeFromPartitionCodec.RequestParameters parameters = ServerScheduledExecutorDisposeFromPartitionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
    }

    @Test
    public void test_ScheduledExecutorDisposeFromPartitionCodec_encodeResponse() {
        int fileClientMessageIndex = 708;
        ClientMessage encoded = ServerScheduledExecutorDisposeFromPartitionCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_ScheduledExecutorDisposeFromMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 709;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerScheduledExecutorDisposeFromMemberCodec.RequestParameters parameters = ServerScheduledExecutorDisposeFromMemberCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.schedulerName));
        assertTrue(isEqual(aString, parameters.taskName));
        assertTrue(isEqual(aUUID, parameters.memberUuid));
    }

    @Test
    public void test_ScheduledExecutorDisposeFromMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 710;
        ClientMessage encoded = ServerScheduledExecutorDisposeFromMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddMultiMapConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 711;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddMultiMapConfigCodec.RequestParameters parameters = ServerDynamicConfigAddMultiMapConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.collectionType));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(aBoolean, parameters.binary));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddMultiMapConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 712;
        ClientMessage encoded = ServerDynamicConfigAddMultiMapConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddRingbufferConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 713;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddRingbufferConfigCodec.RequestParameters parameters = ServerDynamicConfigAddRingbufferConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.capacity));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(anInt, parameters.timeToLiveSeconds));
        assertTrue(isEqual(aString, parameters.inMemoryFormat));
        assertTrue(isEqual(aRingbufferStoreConfigHolder, parameters.ringbufferStoreConfig));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddRingbufferConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 714;
        ClientMessage encoded = ServerDynamicConfigAddRingbufferConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddCardinalityEstimatorConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 715;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddCardinalityEstimatorConfigCodec.RequestParameters parameters = ServerDynamicConfigAddCardinalityEstimatorConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddCardinalityEstimatorConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 716;
        ClientMessage encoded = ServerDynamicConfigAddCardinalityEstimatorConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddListConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 717;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddListConfigCodec.RequestParameters parameters = ServerDynamicConfigAddListConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddListConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 718;
        ClientMessage encoded = ServerDynamicConfigAddListConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddSetConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 719;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddSetConfigCodec.RequestParameters parameters = ServerDynamicConfigAddSetConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddSetConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 720;
        ClientMessage encoded = ServerDynamicConfigAddSetConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddReplicatedMapConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 721;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddReplicatedMapConfigCodec.RequestParameters parameters = ServerDynamicConfigAddReplicatedMapConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.inMemoryFormat));
        assertTrue(isEqual(aBoolean, parameters.asyncFillup));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
    }

    @Test
    public void test_DynamicConfigAddReplicatedMapConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 722;
        ClientMessage encoded = ServerDynamicConfigAddReplicatedMapConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddTopicConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 723;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddTopicConfigCodec.RequestParameters parameters = ServerDynamicConfigAddTopicConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aBoolean, parameters.globalOrderingEnabled));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aBoolean, parameters.multiThreadingEnabled));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
    }

    @Test
    public void test_DynamicConfigAddTopicConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 724;
        ClientMessage encoded = ServerDynamicConfigAddTopicConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddExecutorConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 725;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddExecutorConfigCodec.RequestParameters parameters = ServerDynamicConfigAddExecutorConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.poolSize));
        assertTrue(isEqual(anInt, parameters.queueCapacity));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
    }

    @Test
    public void test_DynamicConfigAddExecutorConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 726;
        ClientMessage encoded = ServerDynamicConfigAddExecutorConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddDurableExecutorConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 727;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddDurableExecutorConfigCodec.RequestParameters parameters = ServerDynamicConfigAddDurableExecutorConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.poolSize));
        assertTrue(isEqual(anInt, parameters.durability));
        assertTrue(isEqual(anInt, parameters.capacity));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(parameters.isStatisticsEnabledExists);
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
    }

    @Test
    public void test_DynamicConfigAddDurableExecutorConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 728;
        ClientMessage encoded = ServerDynamicConfigAddDurableExecutorConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddScheduledExecutorConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 729;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddScheduledExecutorConfigCodec.RequestParameters parameters = ServerDynamicConfigAddScheduledExecutorConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.poolSize));
        assertTrue(isEqual(anInt, parameters.durability));
        assertTrue(isEqual(anInt, parameters.capacity));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
        assertTrue(parameters.isStatisticsEnabledExists);
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
    }

    @Test
    public void test_DynamicConfigAddScheduledExecutorConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 730;
        ClientMessage encoded = ServerDynamicConfigAddScheduledExecutorConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddQueueConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 731;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddQueueConfigCodec.RequestParameters parameters = ServerDynamicConfigAddQueueConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(anInt, parameters.emptyQueueTtl));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aQueueStoreConfigHolder, parameters.queueStoreConfig));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
        assertTrue(parameters.isPriorityComparatorClassNameExists);
        assertTrue(isEqual(aString, parameters.priorityComparatorClassName));
    }

    @Test
    public void test_DynamicConfigAddQueueConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 732;
        ClientMessage encoded = ServerDynamicConfigAddQueueConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddMapConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 733;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddMapConfigCodec.RequestParameters parameters = ServerDynamicConfigAddMapConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(anInt, parameters.timeToLiveSeconds));
        assertTrue(isEqual(anInt, parameters.maxIdleSeconds));
        assertTrue(isEqual(anEvictionConfigHolder, parameters.evictionConfig));
        assertTrue(isEqual(aBoolean, parameters.readBackupData));
        assertTrue(isEqual(aString, parameters.cacheDeserializedValues));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
        assertTrue(isEqual(aString, parameters.inMemoryFormat));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.partitionLostListenerConfigs));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aMapStoreConfigHolder, parameters.mapStoreConfig));
        assertTrue(isEqual(aNearCacheConfigHolder, parameters.nearCacheConfig));
        assertTrue(isEqual(aWanReplicationRef, parameters.wanReplicationRef));
        assertTrue(isEqual(aListOfIndexConfigs, parameters.indexConfigs));
        assertTrue(isEqual(aListOfAttributeConfigs, parameters.attributeConfigs));
        assertTrue(isEqual(aListOfQueryCacheConfigHolders, parameters.queryCacheConfigs));
        assertTrue(isEqual(aString, parameters.partitioningStrategyClassName));
        assertTrue(isEqual(aData, parameters.partitioningStrategyImplementation));
        assertTrue(isEqual(aHotRestartConfig, parameters.hotRestartConfig));
        assertTrue(isEqual(anEventJournalConfig, parameters.eventJournalConfig));
        assertTrue(isEqual(aMerkleTreeConfig, parameters.merkleTreeConfig));
        assertTrue(isEqual(anInt, parameters.metadataPolicy));
        assertFalse(parameters.isPerEntryStatsEnabledExists);
    }

    @Test
    public void test_DynamicConfigAddMapConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 734;
        ClientMessage encoded = ServerDynamicConfigAddMapConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddReliableTopicConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 735;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddReliableTopicConfigCodec.RequestParameters parameters = ServerDynamicConfigAddReliableTopicConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.listenerConfigs));
        assertTrue(isEqual(anInt, parameters.readBatchSize));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.topicOverloadPolicy));
        assertTrue(isEqual(aData, parameters.executor));
    }

    @Test
    public void test_DynamicConfigAddReliableTopicConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 736;
        ClientMessage encoded = ServerDynamicConfigAddReliableTopicConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddCacheConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 737;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddCacheConfigCodec.RequestParameters parameters = ServerDynamicConfigAddCacheConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.keyType));
        assertTrue(isEqual(aString, parameters.valueType));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aBoolean, parameters.managementEnabled));
        assertTrue(isEqual(aBoolean, parameters.readThrough));
        assertTrue(isEqual(aBoolean, parameters.writeThrough));
        assertTrue(isEqual(aString, parameters.cacheLoaderFactory));
        assertTrue(isEqual(aString, parameters.cacheWriterFactory));
        assertTrue(isEqual(aString, parameters.cacheLoader));
        assertTrue(isEqual(aString, parameters.cacheWriter));
        assertTrue(isEqual(anInt, parameters.backupCount));
        assertTrue(isEqual(anInt, parameters.asyncBackupCount));
        assertTrue(isEqual(aString, parameters.inMemoryFormat));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
        assertTrue(isEqual(aString, parameters.mergePolicy));
        assertTrue(isEqual(anInt, parameters.mergeBatchSize));
        assertTrue(isEqual(aBoolean, parameters.disablePerEntryInvalidationEvents));
        assertTrue(isEqual(aListOfListenerConfigHolders, parameters.partitionLostListenerConfigs));
        assertTrue(isEqual(aString, parameters.expiryPolicyFactoryClassName));
        assertTrue(isEqual(aTimedExpiryPolicyFactoryConfig, parameters.timedExpiryPolicyFactoryConfig));
        assertTrue(isEqual(aListOfCacheSimpleEntryListenerConfigs, parameters.cacheEntryListeners));
        assertTrue(isEqual(anEvictionConfigHolder, parameters.evictionConfig));
        assertTrue(isEqual(aWanReplicationRef, parameters.wanReplicationRef));
        assertTrue(isEqual(anEventJournalConfig, parameters.eventJournalConfig));
        assertTrue(isEqual(aHotRestartConfig, parameters.hotRestartConfig));
    }

    @Test
    public void test_DynamicConfigAddCacheConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 738;
        ClientMessage encoded = ServerDynamicConfigAddCacheConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddFlakeIdGeneratorConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 739;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddFlakeIdGeneratorConfigCodec.RequestParameters parameters = ServerDynamicConfigAddFlakeIdGeneratorConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.prefetchCount));
        assertTrue(isEqual(aLong, parameters.prefetchValidity));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aLong, parameters.nodeIdOffset));
        assertTrue(isEqual(aLong, parameters.epochStart));
        assertTrue(isEqual(anInt, parameters.bitsSequence));
        assertTrue(isEqual(anInt, parameters.bitsNodeId));
        assertTrue(isEqual(aLong, parameters.allowedFutureMillis));
    }

    @Test
    public void test_DynamicConfigAddFlakeIdGeneratorConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 740;
        ClientMessage encoded = ServerDynamicConfigAddFlakeIdGeneratorConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_DynamicConfigAddPNCounterConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 741;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerDynamicConfigAddPNCounterConfigCodec.RequestParameters parameters = ServerDynamicConfigAddPNCounterConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.replicaCount));
        assertTrue(isEqual(aBoolean, parameters.statisticsEnabled));
        assertTrue(isEqual(aString, parameters.splitBrainProtectionName));
    }

    @Test
    public void test_DynamicConfigAddPNCounterConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 742;
        ClientMessage encoded = ServerDynamicConfigAddPNCounterConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_FlakeIdGeneratorNewIdBatchCodec_decodeRequest() {
        int fileClientMessageIndex = 743;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerFlakeIdGeneratorNewIdBatchCodec.RequestParameters parameters = ServerFlakeIdGeneratorNewIdBatchCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(anInt, parameters.batchSize));
    }

    @Test
    public void test_FlakeIdGeneratorNewIdBatchCodec_encodeResponse() {
        int fileClientMessageIndex = 744;
        ClientMessage encoded = ServerFlakeIdGeneratorNewIdBatchCodec.encodeResponse(aLong, aLong, anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_PNCounterGetCodec_decodeRequest() {
        int fileClientMessageIndex = 745;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerPNCounterGetCodec.RequestParameters parameters = ServerPNCounterGetCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aListOfUuidToLong, parameters.replicaTimestamps));
        assertTrue(isEqual(aUUID, parameters.targetReplicaUUID));
    }

    @Test
    public void test_PNCounterGetCodec_encodeResponse() {
        int fileClientMessageIndex = 746;
        ClientMessage encoded = ServerPNCounterGetCodec.encodeResponse(aLong, aListOfUuidToLong, anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_PNCounterAddCodec_decodeRequest() {
        int fileClientMessageIndex = 747;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerPNCounterAddCodec.RequestParameters parameters = ServerPNCounterAddCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aLong, parameters.delta));
        assertTrue(isEqual(aBoolean, parameters.getBeforeUpdate));
        assertTrue(isEqual(aListOfUuidToLong, parameters.replicaTimestamps));
        assertTrue(isEqual(aUUID, parameters.targetReplicaUUID));
    }

    @Test
    public void test_PNCounterAddCodec_encodeResponse() {
        int fileClientMessageIndex = 748;
        ClientMessage encoded = ServerPNCounterAddCodec.encodeResponse(aLong, aListOfUuidToLong, anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_PNCounterGetConfiguredReplicaCountCodec_decodeRequest() {
        int fileClientMessageIndex = 749;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerPNCounterGetConfiguredReplicaCountCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_PNCounterGetConfiguredReplicaCountCodec_encodeResponse() {
        int fileClientMessageIndex = 750;
        ClientMessage encoded = ServerPNCounterGetConfiguredReplicaCountCodec.encodeResponse(anInt);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPGroupCreateCPGroupCodec_decodeRequest() {
        int fileClientMessageIndex = 751;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerCPGroupCreateCPGroupCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPGroupCreateCPGroupCodec_encodeResponse() {
        int fileClientMessageIndex = 752;
        ClientMessage encoded = ServerCPGroupCreateCPGroupCodec.encodeResponse(aRaftGroupId);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPGroupDestroyCPObjectCodec_decodeRequest() {
        int fileClientMessageIndex = 753;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCPGroupDestroyCPObjectCodec.RequestParameters parameters = ServerCPGroupDestroyCPObjectCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.serviceName));
        assertTrue(isEqual(aString, parameters.objectName));
    }

    @Test
    public void test_CPGroupDestroyCPObjectCodec_encodeResponse() {
        int fileClientMessageIndex = 754;
        ClientMessage encoded = ServerCPGroupDestroyCPObjectCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSessionCreateSessionCodec_decodeRequest() {
        int fileClientMessageIndex = 755;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCPSessionCreateSessionCodec.RequestParameters parameters = ServerCPSessionCreateSessionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aString, parameters.endpointName));
    }

    @Test
    public void test_CPSessionCreateSessionCodec_encodeResponse() {
        int fileClientMessageIndex = 756;
        ClientMessage encoded = ServerCPSessionCreateSessionCodec.encodeResponse(aLong, aLong, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSessionCloseSessionCodec_decodeRequest() {
        int fileClientMessageIndex = 757;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCPSessionCloseSessionCodec.RequestParameters parameters = ServerCPSessionCloseSessionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aLong, parameters.sessionId));
    }

    @Test
    public void test_CPSessionCloseSessionCodec_encodeResponse() {
        int fileClientMessageIndex = 758;
        ClientMessage encoded = ServerCPSessionCloseSessionCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSessionHeartbeatSessionCodec_decodeRequest() {
        int fileClientMessageIndex = 759;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerCPSessionHeartbeatSessionCodec.RequestParameters parameters = ServerCPSessionHeartbeatSessionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aRaftGroupId, parameters.groupId));
        assertTrue(isEqual(aLong, parameters.sessionId));
    }

    @Test
    public void test_CPSessionHeartbeatSessionCodec_encodeResponse() {
        int fileClientMessageIndex = 760;
        ClientMessage encoded = ServerCPSessionHeartbeatSessionCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSessionGenerateThreadIdCodec_decodeRequest() {
        int fileClientMessageIndex = 761;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aRaftGroupId, ServerCPSessionGenerateThreadIdCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPSessionGenerateThreadIdCodec_encodeResponse() {
        int fileClientMessageIndex = 762;
        ClientMessage encoded = ServerCPSessionGenerateThreadIdCodec.encodeResponse(aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCReadMetricsCodec_decodeRequest() {
        int fileClientMessageIndex = 763;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCReadMetricsCodec.RequestParameters parameters = ServerMCReadMetricsCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aUUID, parameters.uuid));
        assertTrue(isEqual(aLong, parameters.fromSequence));
    }

    @Test
    public void test_MCReadMetricsCodec_encodeResponse() {
        int fileClientMessageIndex = 764;
        ClientMessage encoded = ServerMCReadMetricsCodec.encodeResponse(aListOfLongToByteArray, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCChangeClusterStateCodec_decodeRequest() {
        int fileClientMessageIndex = 765;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(anInt, ServerMCChangeClusterStateCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MCChangeClusterStateCodec_encodeResponse() {
        int fileClientMessageIndex = 766;
        ClientMessage encoded = ServerMCChangeClusterStateCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetMapConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 767;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMCGetMapConfigCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MCGetMapConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 768;
        ClientMessage encoded = ServerMCGetMapConfigCodec.encodeResponse(anInt, anInt, anInt, anInt, anInt, anInt, anInt, aBoolean, anInt, aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCUpdateMapConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 769;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCUpdateMapConfigCodec.RequestParameters parameters = ServerMCUpdateMapConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.mapName));
        assertTrue(isEqual(anInt, parameters.timeToLiveSeconds));
        assertTrue(isEqual(anInt, parameters.maxIdleSeconds));
        assertTrue(isEqual(anInt, parameters.evictionPolicy));
        assertTrue(isEqual(aBoolean, parameters.readBackupData));
        assertTrue(isEqual(anInt, parameters.maxSize));
        assertTrue(isEqual(anInt, parameters.maxSizePolicy));
    }

    @Test
    public void test_MCUpdateMapConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 770;
        ClientMessage encoded = ServerMCUpdateMapConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetMemberConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 771;
    }

    @Test
    public void test_MCGetMemberConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 772;
        ClientMessage encoded = ServerMCGetMemberConfigCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCRunGcCodec_decodeRequest() {
        int fileClientMessageIndex = 773;
    }

    @Test
    public void test_MCRunGcCodec_encodeResponse() {
        int fileClientMessageIndex = 774;
        ClientMessage encoded = ServerMCRunGcCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetThreadDumpCodec_decodeRequest() {
        int fileClientMessageIndex = 775;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aBoolean, ServerMCGetThreadDumpCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MCGetThreadDumpCodec_encodeResponse() {
        int fileClientMessageIndex = 776;
        ClientMessage encoded = ServerMCGetThreadDumpCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCShutdownMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 777;
    }

    @Test
    public void test_MCShutdownMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 778;
        ClientMessage encoded = ServerMCShutdownMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCPromoteLiteMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 779;
    }

    @Test
    public void test_MCPromoteLiteMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 780;
        ClientMessage encoded = ServerMCPromoteLiteMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetSystemPropertiesCodec_decodeRequest() {
        int fileClientMessageIndex = 781;
    }

    @Test
    public void test_MCGetSystemPropertiesCodec_encodeResponse() {
        int fileClientMessageIndex = 782;
        ClientMessage encoded = ServerMCGetSystemPropertiesCodec.encodeResponse(aListOfStringToString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetTimedMemberStateCodec_decodeRequest() {
        int fileClientMessageIndex = 783;
    }

    @Test
    public void test_MCGetTimedMemberStateCodec_encodeResponse() {
        int fileClientMessageIndex = 784;
        ClientMessage encoded = ServerMCGetTimedMemberStateCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCMatchMCConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 785;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aString, ServerMCMatchMCConfigCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MCMatchMCConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 786;
        ClientMessage encoded = ServerMCMatchMCConfigCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCApplyMCConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 787;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCApplyMCConfigCodec.RequestParameters parameters = ServerMCApplyMCConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.eTag));
        assertTrue(isEqual(anInt, parameters.clientBwListMode));
        assertTrue(isEqual(aListOfClientBwListEntries, parameters.clientBwListEntries));
    }

    @Test
    public void test_MCApplyMCConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 788;
        ClientMessage encoded = ServerMCApplyMCConfigCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetClusterMetadataCodec_decodeRequest() {
        int fileClientMessageIndex = 789;
    }

    @Test
    public void test_MCGetClusterMetadataCodec_encodeResponse() {
        int fileClientMessageIndex = 790;
        ClientMessage encoded = ServerMCGetClusterMetadataCodec.encodeResponse(aByte, aString, aString, aLong);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCShutdownClusterCodec_decodeRequest() {
        int fileClientMessageIndex = 791;
    }

    @Test
    public void test_MCShutdownClusterCodec_encodeResponse() {
        int fileClientMessageIndex = 792;
        ClientMessage encoded = ServerMCShutdownClusterCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCChangeClusterVersionCodec_decodeRequest() {
        int fileClientMessageIndex = 793;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCChangeClusterVersionCodec.RequestParameters parameters = ServerMCChangeClusterVersionCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aByte, parameters.majorVersion));
        assertTrue(isEqual(aByte, parameters.minorVersion));
    }

    @Test
    public void test_MCChangeClusterVersionCodec_encodeResponse() {
        int fileClientMessageIndex = 794;
        ClientMessage encoded = ServerMCChangeClusterVersionCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCRunScriptCodec_decodeRequest() {
        int fileClientMessageIndex = 795;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCRunScriptCodec.RequestParameters parameters = ServerMCRunScriptCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.engine));
        assertTrue(isEqual(aString, parameters.script));
    }

    @Test
    public void test_MCRunScriptCodec_encodeResponse() {
        int fileClientMessageIndex = 796;
        ClientMessage encoded = ServerMCRunScriptCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCRunConsoleCommandCodec_decodeRequest() {
        int fileClientMessageIndex = 797;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCRunConsoleCommandCodec.RequestParameters parameters = ServerMCRunConsoleCommandCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.namespace));
        assertTrue(isEqual(aString, parameters.command));
    }

    @Test
    public void test_MCRunConsoleCommandCodec_encodeResponse() {
        int fileClientMessageIndex = 798;
        ClientMessage encoded = ServerMCRunConsoleCommandCodec.encodeResponse(aString);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCChangeWanReplicationStateCodec_decodeRequest() {
        int fileClientMessageIndex = 799;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCChangeWanReplicationStateCodec.RequestParameters parameters = ServerMCChangeWanReplicationStateCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.wanReplicationName));
        assertTrue(isEqual(aString, parameters.wanPublisherId));
        assertTrue(isEqual(aByte, parameters.newState));
    }

    @Test
    public void test_MCChangeWanReplicationStateCodec_encodeResponse() {
        int fileClientMessageIndex = 800;
        ClientMessage encoded = ServerMCChangeWanReplicationStateCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCClearWanQueuesCodec_decodeRequest() {
        int fileClientMessageIndex = 801;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCClearWanQueuesCodec.RequestParameters parameters = ServerMCClearWanQueuesCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.wanReplicationName));
        assertTrue(isEqual(aString, parameters.wanPublisherId));
    }

    @Test
    public void test_MCClearWanQueuesCodec_encodeResponse() {
        int fileClientMessageIndex = 802;
        ClientMessage encoded = ServerMCClearWanQueuesCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCAddWanBatchPublisherConfigCodec_decodeRequest() {
        int fileClientMessageIndex = 803;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCAddWanBatchPublisherConfigCodec.RequestParameters parameters = ServerMCAddWanBatchPublisherConfigCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.name));
        assertTrue(isEqual(aString, parameters.targetCluster));
        assertTrue(isEqual(aString, parameters.publisherId));
        assertTrue(isEqual(aString, parameters.endpoints));
        assertTrue(isEqual(anInt, parameters.queueCapacity));
        assertTrue(isEqual(anInt, parameters.batchSize));
        assertTrue(isEqual(anInt, parameters.batchMaxDelayMillis));
        assertTrue(isEqual(anInt, parameters.responseTimeoutMillis));
        assertTrue(isEqual(anInt, parameters.ackType));
        assertTrue(isEqual(anInt, parameters.queueFullBehavior));
    }

    @Test
    public void test_MCAddWanBatchPublisherConfigCodec_encodeResponse() {
        int fileClientMessageIndex = 804;
        ClientMessage encoded = ServerMCAddWanBatchPublisherConfigCodec.encodeResponse(aListOfStrings, aListOfStrings);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCWanSyncMapCodec_decodeRequest() {
        int fileClientMessageIndex = 805;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCWanSyncMapCodec.RequestParameters parameters = ServerMCWanSyncMapCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.wanReplicationName));
        assertTrue(isEqual(aString, parameters.wanPublisherId));
        assertTrue(isEqual(anInt, parameters.wanSyncType));
        assertTrue(isEqual(aString, parameters.mapName));
    }

    @Test
    public void test_MCWanSyncMapCodec_encodeResponse() {
        int fileClientMessageIndex = 806;
        ClientMessage encoded = ServerMCWanSyncMapCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCCheckWanConsistencyCodec_decodeRequest() {
        int fileClientMessageIndex = 807;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerMCCheckWanConsistencyCodec.RequestParameters parameters = ServerMCCheckWanConsistencyCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.wanReplicationName));
        assertTrue(isEqual(aString, parameters.wanPublisherId));
        assertTrue(isEqual(aString, parameters.mapName));
    }

    @Test
    public void test_MCCheckWanConsistencyCodec_encodeResponse() {
        int fileClientMessageIndex = 808;
        ClientMessage encoded = ServerMCCheckWanConsistencyCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCPollMCEventsCodec_decodeRequest() {
        int fileClientMessageIndex = 809;
    }

    @Test
    public void test_MCPollMCEventsCodec_encodeResponse() {
        int fileClientMessageIndex = 810;
        ClientMessage encoded = ServerMCPollMCEventsCodec.encodeResponse(aListOfMCEvents);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCGetCPMembersCodec_decodeRequest() {
        int fileClientMessageIndex = 811;
    }

    @Test
    public void test_MCGetCPMembersCodec_encodeResponse() {
        int fileClientMessageIndex = 812;
        ClientMessage encoded = ServerMCGetCPMembersCodec.encodeResponse(aListOfUUIDToUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCPromoteToCPMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 813;
    }

    @Test
    public void test_MCPromoteToCPMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 814;
        ClientMessage encoded = ServerMCPromoteToCPMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCRemoveCPMemberCodec_decodeRequest() {
        int fileClientMessageIndex = 815;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerMCRemoveCPMemberCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_MCRemoveCPMemberCodec_encodeResponse() {
        int fileClientMessageIndex = 816;
        ClientMessage encoded = ServerMCRemoveCPMemberCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCResetCPSubsystemCodec_decodeRequest() {
        int fileClientMessageIndex = 817;
    }

    @Test
    public void test_MCResetCPSubsystemCodec_encodeResponse() {
        int fileClientMessageIndex = 818;
        ClientMessage encoded = ServerMCResetCPSubsystemCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCTriggerPartialStartCodec_decodeRequest() {
        int fileClientMessageIndex = 819;
    }

    @Test
    public void test_MCTriggerPartialStartCodec_encodeResponse() {
        int fileClientMessageIndex = 820;
        ClientMessage encoded = ServerMCTriggerPartialStartCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCTriggerForceStartCodec_decodeRequest() {
        int fileClientMessageIndex = 821;
    }

    @Test
    public void test_MCTriggerForceStartCodec_encodeResponse() {
        int fileClientMessageIndex = 822;
        ClientMessage encoded = ServerMCTriggerForceStartCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCTriggerHotRestartBackupCodec_decodeRequest() {
        int fileClientMessageIndex = 823;
    }

    @Test
    public void test_MCTriggerHotRestartBackupCodec_encodeResponse() {
        int fileClientMessageIndex = 824;
        ClientMessage encoded = ServerMCTriggerHotRestartBackupCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_MCInterruptHotRestartBackupCodec_decodeRequest() {
        int fileClientMessageIndex = 825;
    }

    @Test
    public void test_MCInterruptHotRestartBackupCodec_encodeResponse() {
        int fileClientMessageIndex = 826;
        ClientMessage encoded = ServerMCInterruptHotRestartBackupCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SqlExecute_reservedCodec_decodeRequest() {
        int fileClientMessageIndex = 827;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSqlExecute_reservedCodec.RequestParameters parameters = ServerSqlExecute_reservedCodec.decodeRequest(fromFile);
        assertTrue(isEqual(aString, parameters.sql));
        assertTrue(isEqual(aListOfData, parameters.parameters));
        assertTrue(isEqual(aLong, parameters.timeoutMillis));
        assertTrue(isEqual(anInt, parameters.cursorBufferSize));
    }

    @Test
    public void test_SqlExecute_reservedCodec_encodeResponse() {
        int fileClientMessageIndex = 828;
        ClientMessage encoded = ServerSqlExecute_reservedCodec.encodeResponse(anSqlQueryId, aListOfSqlColumnMetadata, aListOfListOfData, aBoolean, aLong, anSqlError);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SqlFetch_reservedCodec_decodeRequest() {
        int fileClientMessageIndex = 829;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ServerSqlFetch_reservedCodec.RequestParameters parameters = ServerSqlFetch_reservedCodec.decodeRequest(fromFile);
        assertTrue(isEqual(anSqlQueryId, parameters.queryId));
        assertTrue(isEqual(anInt, parameters.cursorBufferSize));
    }

    @Test
    public void test_SqlFetch_reservedCodec_encodeResponse() {
        int fileClientMessageIndex = 830;
        ClientMessage encoded = ServerSqlFetch_reservedCodec.encodeResponse(aListOfListOfData, aBoolean, anSqlError);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_SqlCloseCodec_decodeRequest() {
        int fileClientMessageIndex = 831;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(anSqlQueryId, ServerSqlCloseCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_SqlCloseCodec_encodeResponse() {
        int fileClientMessageIndex = 832;
        ClientMessage encoded = ServerSqlCloseCodec.encodeResponse();
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemAddMembershipListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 833;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aBoolean, ServerCPSubsystemAddMembershipListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPSubsystemAddMembershipListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 834;
        ClientMessage encoded = ServerCPSubsystemAddMembershipListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemAddMembershipListenerCodec_encodeMembershipEventEvent() {
        int fileClientMessageIndex = 835;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCPSubsystemAddMembershipListenerCodec.encodeMembershipEventEvent(aCpMember, aByte);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemRemoveMembershipListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 836;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerCPSubsystemRemoveMembershipListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPSubsystemRemoveMembershipListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 837;
        ClientMessage encoded = ServerCPSubsystemRemoveMembershipListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemAddGroupAvailabilityListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 838;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aBoolean, ServerCPSubsystemAddGroupAvailabilityListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPSubsystemAddGroupAvailabilityListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 839;
        ClientMessage encoded = ServerCPSubsystemAddGroupAvailabilityListenerCodec.encodeResponse(aUUID);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemAddGroupAvailabilityListenerCodec_encodeGroupAvailabilityEventEvent() {
        int fileClientMessageIndex = 840;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        ClientMessage encoded = ServerCPSubsystemAddGroupAvailabilityListenerCodec.encodeGroupAvailabilityEventEvent(aRaftGroupId, aListOfCpMembers, aListOfCpMembers);
        compareClientMessages(fromFile, encoded);
    }

    @Test
    public void test_CPSubsystemRemoveGroupAvailabilityListenerCodec_decodeRequest() {
        int fileClientMessageIndex = 841;
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        assertTrue(isEqual(aUUID, ServerCPSubsystemRemoveGroupAvailabilityListenerCodec.decodeRequest(fromFile)));
    }

    @Test
    public void test_CPSubsystemRemoveGroupAvailabilityListenerCodec_encodeResponse() {
        int fileClientMessageIndex = 842;
        ClientMessage encoded = ServerCPSubsystemRemoveGroupAvailabilityListenerCodec.encodeResponse(aBoolean);
        ClientMessage fromFile = clientMessages.get(fileClientMessageIndex);
        compareClientMessages(fromFile, encoded);
    }

    private void compareClientMessages(ClientMessage binaryMessage, ClientMessage encodedMessage) {
        ClientMessage.Frame binaryFrame, encodedFrame;

        ClientMessage.ForwardFrameIterator binaryFrameIterator = binaryMessage.frameIterator();
        ClientMessage.ForwardFrameIterator encodedFrameIterator = encodedMessage.frameIterator();
        assertTrue("Client message that is read from the binary file does not have any frames", binaryFrameIterator.hasNext());

        while (binaryFrameIterator.hasNext()) {
            binaryFrame = binaryFrameIterator.next();
            encodedFrame = encodedFrameIterator.next();
            assertNotNull("Encoded client message has less frames.", encodedFrame);

            if (binaryFrame.isEndFrame() && !encodedFrame.isEndFrame()) {
                if (encodedFrame.isBeginFrame()) {
                    HazelcastClientUtil.fastForwardToEndFrame(encodedFrameIterator);
                }
                encodedFrame = HazelcastClientUtil.fastForwardToEndFrame(encodedFrameIterator);
            }

            boolean isFinal = binaryFrameIterator.peekNext() == null;
            int flags = isFinal ? encodedFrame.flags | IS_FINAL_FLAG : encodedFrame.flags;
            assertArrayEquals("Frames have different contents", binaryFrame.content, Arrays.copyOf(encodedFrame.content, binaryFrame.content.length));
            assertEquals("Frames have different flags", binaryFrame.flags, flags);
        }
    }
}