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

package com.hazelcast.client.impl.protocol;

import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongAddAndGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongAlterCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongApplyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongCompareAndSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongGetAndAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongGetAndSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicLongGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicRefApplyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicRefCompareAndSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicRefContainsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicRefGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerAtomicRefSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPGroupCreateCPGroupCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPGroupDestroyCPObjectCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSessionCloseSessionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSessionCreateSessionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSessionGenerateThreadIdCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSessionHeartbeatSessionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSubsystemAddGroupAvailabilityListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSubsystemAddMembershipListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSubsystemRemoveGroupAvailabilityListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCPSubsystemRemoveMembershipListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheAddEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheAddNearCacheInvalidationListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheAddPartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheContainsKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheCreateConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheDestroyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheEntryProcessorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheEventJournalReadCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheEventJournalSubscribeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheFetchNearCacheInvalidationMetadataCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetAndRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetAndReplaceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheIterateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheIterateEntriesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheListenerRegistrationCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheLoadAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheManagementConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCachePutAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCachePutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCachePutIfAbsentCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemoveAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemoveAllKeysCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemoveEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemoveInvalidationListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheRemovePartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheReplaceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheSetExpiryPolicyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCacheSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCardinalityEstimatorAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCardinalityEstimatorEstimateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAddClusterViewListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAddDistributedObjectListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAddMigrationListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAddPartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAuthenticationCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientAuthenticationCustomCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientCreateProxiesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientCreateProxyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientDeployClassesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientDestroyProxyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientGetDistributedObjectsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientLocalBackupListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientPingCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientRemoveDistributedObjectListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientRemoveMigrationListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientRemovePartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientStatisticsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerClientTriggerPartitionAssignmentCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQueryAddListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQueryDestroyCacheCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQueryMadePublishableCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQueryPublisherCreateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQueryPublisherCreateWithValueCodec;
import com.hazelcast.client.impl.protocol.codec.ServerContinuousQuerySetReadCursorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCountDownLatchAwaitCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCountDownLatchCountDownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCountDownLatchGetCountCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCountDownLatchGetRoundCodec;
import com.hazelcast.client.impl.protocol.codec.ServerCountDownLatchTrySetCountCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorDisposeResultCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorIsShutdownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorRetrieveAndDisposeResultCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorRetrieveResultCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorShutdownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDurableExecutorSubmitToPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddCacheConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddCardinalityEstimatorConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddDurableExecutorConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddExecutorConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddFlakeIdGeneratorConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddListConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddMapConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddMultiMapConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddPNCounterConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddQueueConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddReliableTopicConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddReplicatedMapConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddRingbufferConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddScheduledExecutorConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddSetConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddTopicConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceCancelOnMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceCancelOnPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceIsShutdownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceShutdownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceSubmitToMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerExecutorServiceSubmitToPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerFencedLockGetLockOwnershipCodec;
import com.hazelcast.client.impl.protocol.codec.ServerFencedLockLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerFencedLockTryLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerFencedLockUnlockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerFlakeIdGeneratorNewIdBatchCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListAddAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListAddAllWithIndexCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListAddListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListAddWithIndexCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListCompareAndRemoveAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListCompareAndRetainAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListContainsAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListContainsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListGetAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListIndexOfCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListIteratorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListLastIndexOfCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListListIteratorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListRemoveListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListRemoveWithIndexCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerListSubCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCApplyMCConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCChangeClusterStateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCChangeClusterVersionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetCPMembersCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetClusterMetadataCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetMapConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetMemberConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetSystemPropertiesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetThreadDumpCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCGetTimedMemberStateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCInterruptHotRestartBackupCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCMatchMCConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCPollMCEventsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCPromoteLiteMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCPromoteToCPMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCReadMetricsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCRemoveCPMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCResetCPSubsystemCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCRunConsoleCommandCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCRunGcCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCRunScriptCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCShutdownClusterCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCShutdownMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCTriggerForceStartCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCTriggerHotRestartBackupCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCTriggerPartialStartCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMCUpdateMapConfigCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddEntryListenerToKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddEntryListenerToKeyWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddEntryListenerWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddIndexCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddInterceptorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddNearCacheInvalidationListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAddPartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAggregateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapAggregateWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapContainsKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapContainsValueCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapDeleteCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEntriesWithPagingPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEntriesWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEntrySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEventJournalReadCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEventJournalSubscribeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEvictAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapEvictCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapExecuteOnAllKeysCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapExecuteOnKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapExecuteOnKeysCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapExecuteWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapFetchEntriesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapFetchKeysCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapFetchNearCacheInvalidationMetadataCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapFetchWithQueryCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapFlushCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapForceUnlockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapGetAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapGetEntryViewCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapIsLockedCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapKeySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapKeySetWithPagingPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapKeySetWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapLoadAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapLoadGivenKeysCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapProjectCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapProjectWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutIfAbsentCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutIfAbsentWithMaxIdleCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutTransientCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutTransientWithMaxIdleCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapPutWithMaxIdleCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemoveAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemoveEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemoveIfSameCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemoveInterceptorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapRemovePartitionLostListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapReplaceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapReplaceIfSameCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapSetTtlCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapSetWithMaxIdleCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapSubmitToKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapTryLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapTryPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapTryRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapUnlockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapValuesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapValuesWithPagingPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMapValuesWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapAddEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapAddEntryListenerToKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapContainsEntryCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapContainsKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapContainsValueCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapDeleteCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapEntrySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapForceUnlockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapIsLockedCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapKeySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapPutAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapRemoveEntryCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapRemoveEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapTryLockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapUnlockCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapValueCountCodec;
import com.hazelcast.client.impl.protocol.codec.ServerMultiMapValuesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerPNCounterAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerPNCounterGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerPNCounterGetConfiguredReplicaCountCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueAddAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueAddListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueCompareAndRemoveAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueCompareAndRetainAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueContainsAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueContainsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueDrainToCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueDrainToMaxSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueIteratorCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueOfferCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueuePeekCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueuePollCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueuePutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueRemainingCapacityCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueRemoveListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerQueueTakeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapAddEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapAddEntryListenerToKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapAddEntryListenerWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapAddNearCacheEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapContainsKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapContainsValueCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapEntrySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapKeySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapPutAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapRemoveEntryListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerReplicatedMapValuesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferAddAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferCapacityCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferHeadSequenceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferReadManyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferReadOneCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferRemainingCapacityCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerRingbufferTailSequenceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorCancelFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorCancelFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorDisposeFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorDisposeFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetAllScheduledFuturesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetDelayFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetDelayFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetResultFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetResultFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetStatsFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorGetStatsFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorIsCancelledFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorIsCancelledFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorIsDoneFromMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorIsDoneFromPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorShutdownCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorSubmitToMemberCodec;
import com.hazelcast.client.impl.protocol.codec.ServerScheduledExecutorSubmitToPartitionCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreAcquireCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreAvailablePermitsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreChangeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreDrainCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreGetSemaphoreTypeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreInitCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSemaphoreReleaseCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetAddAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetAddListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetClearCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetCompareAndRemoveAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetCompareAndRetainAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetContainsAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetContainsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetGetAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetRemoveListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSetSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSqlCloseCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSqlExecuteCodec;
import com.hazelcast.client.impl.protocol.codec.ServerSqlFetchCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTopicAddMessageListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTopicPublishAllCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTopicPublishCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTopicRemoveMessageListenerCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionCommitCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionCreateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionRollbackCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalListAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalListRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalListSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapContainsKeyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapDeleteCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapGetForUpdateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapIsEmptyCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapKeySetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapKeySetWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapPutIfAbsentCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapRemoveIfSameCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapReplaceCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapReplaceIfSameCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapSetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapValuesCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMapValuesWithPredicateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapGetCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapPutCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapRemoveEntryCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalMultiMapValueCountCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalQueueOfferCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalQueuePeekCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalQueuePollCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalQueueSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalQueueTakeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalSetAddCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalSetRemoveCodec;
import com.hazelcast.client.impl.protocol.codec.ServerTransactionalSetSizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionClearRemoteCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionCollectTransactionsCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionCommitCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionCreateCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionFinalizeCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionPrepareCodec;
import com.hazelcast.client.impl.protocol.codec.ServerXATransactionRollbackCodec;
import com.hazelcast.client.impl.protocol.task.AddBackupListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.AddClusterViewListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.AddDistributedObjectListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.AddMigrationListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.AddPartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.AuthenticationCustomCredentialsMessageTask;
import com.hazelcast.client.impl.protocol.task.AuthenticationMessageTask;
import com.hazelcast.client.impl.protocol.task.ClientStatisticsMessageTask;
import com.hazelcast.client.impl.protocol.task.CreateProxiesMessageTask;
import com.hazelcast.client.impl.protocol.task.CreateProxyMessageTask;
import com.hazelcast.client.impl.protocol.task.DeployClassesMessageTask;
import com.hazelcast.client.impl.protocol.task.DestroyProxyMessageTask;
import com.hazelcast.client.impl.protocol.task.GetDistributedObjectsMessageTask;
import com.hazelcast.client.impl.protocol.task.PingMessageTask;
import com.hazelcast.client.impl.protocol.task.RemoveDistributedObjectListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.RemoveMigrationListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.RemovePartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.TriggerPartitionAssignmentMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheAddEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheAddNearCacheInvalidationListenerTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheAddPartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheClearMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheContainsKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheCreateConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheDestroyMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheEntryProcessorMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheEventJournalReadTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheEventJournalSubscribeTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheFetchNearCacheInvalidationMetadataTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheGetAllMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheGetAndRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheGetAndReplaceMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheGetConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheGetMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheIterateEntriesMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheIterateMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheListenerRegistrationMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheLoadAllMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheManagementConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CachePutAllMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CachePutIfAbsentMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CachePutMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemoveAllKeysMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemoveAllMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemoveEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemoveInvalidationListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheRemovePartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheReplaceMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheSetExpiryPolicyMessageTask;
import com.hazelcast.client.impl.protocol.task.cache.CacheSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.cardinality.CardinalityEstimatorAddMessageTask;
import com.hazelcast.client.impl.protocol.task.cardinality.CardinalityEstimatorEstimateMessageTask;
import com.hazelcast.client.impl.protocol.task.crdt.pncounter.PNCounterAddMessageTask;
import com.hazelcast.client.impl.protocol.task.crdt.pncounter.PNCounterGetConfiguredReplicaCountMessageTask;
import com.hazelcast.client.impl.protocol.task.crdt.pncounter.PNCounterGetMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddCacheConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddCardinalityEstimatorConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddDurableExecutorConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddExecutorConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddFlakeIdGeneratorConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddListConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddMapConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddMultiMapConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddPNCounterConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddQueueConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddReliableTopicConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddReplicatedMapConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddRingbufferConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddScheduledExecutorConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddSetConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.dynamicconfig.AddTopicConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceCancelOnAddressMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceCancelOnPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceIsShutdownMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceShutdownMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceSubmitToAddressMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.ExecutorServiceSubmitToPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorDisposeResultMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorIsShutdownMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorRetrieveAndDisposeResultMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorRetrieveResultMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorShutdownMessageTask;
import com.hazelcast.client.impl.protocol.task.executorservice.durable.DurableExecutorSubmitToPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListAddAllMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListAddAllWithIndexMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListAddListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListAddMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListAddWithIndexMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListClearMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListCompareAndRemoveAllMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListCompareAndRetainAllMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListContainsAllMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListContainsMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListGetAllMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListGetMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListIndexOfMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListIteratorMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListLastIndexOfMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListListIteratorMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListRemoveListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListRemoveWithIndexMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListSetMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.list.ListSubMessageTask;
import com.hazelcast.client.impl.protocol.task.management.AddWanBatchPublisherConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ApplyClientFilteringConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ChangeClusterStateMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ChangeClusterVersionMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ChangeWanReplicationStateMessageTask;
import com.hazelcast.client.impl.protocol.task.management.CheckWanConsistencyMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ClearWanQueuesMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetCPMembersMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetClusterMetadataMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetMapConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetMemberConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetSystemPropertiesMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetThreadDumpMessageTask;
import com.hazelcast.client.impl.protocol.task.management.GetTimedMemberStateMessageTask;
import com.hazelcast.client.impl.protocol.task.management.HotRestartInterruptBackupMessageTask;
import com.hazelcast.client.impl.protocol.task.management.HotRestartTriggerBackupMessageTask;
import com.hazelcast.client.impl.protocol.task.management.HotRestartTriggerForceStartMessageTask;
import com.hazelcast.client.impl.protocol.task.management.HotRestartTriggerPartialStartMessageTask;
import com.hazelcast.client.impl.protocol.task.management.MatchClientFilteringConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.PollMCEventsMessageTask;
import com.hazelcast.client.impl.protocol.task.management.PromoteLiteMemberMessageTask;
import com.hazelcast.client.impl.protocol.task.management.PromoteToCPMemberMessageTask;
import com.hazelcast.client.impl.protocol.task.management.RemoveCPMemberMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ResetCPSubsystemMessageTask;
import com.hazelcast.client.impl.protocol.task.management.RunConsoleCommandMessageTask;
import com.hazelcast.client.impl.protocol.task.management.RunGcMessageTask;
import com.hazelcast.client.impl.protocol.task.management.RunScriptMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ShutdownClusterMessageTask;
import com.hazelcast.client.impl.protocol.task.management.ShutdownMemberMessageTask;
import com.hazelcast.client.impl.protocol.task.management.UpdateMapConfigMessageTask;
import com.hazelcast.client.impl.protocol.task.management.WanSyncMapMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddEntryListenerToKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddEntryListenerToKeyWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddEntryListenerWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddIndexMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddInterceptorMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddNearCacheInvalidationListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAddPartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAggregateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapAggregateWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapClearMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapContainsKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapContainsValueMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapDeleteMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapDestroyCacheMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapEntriesWithPagingPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapEntriesWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapEntrySetMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapEventJournalReadTask;
import com.hazelcast.client.impl.protocol.task.map.MapEventJournalSubscribeTask;
import com.hazelcast.client.impl.protocol.task.map.MapEvictAllMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapEvictMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapExecuteOnAllKeysMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapExecuteOnKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapExecuteOnKeysMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapExecuteWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapFetchEntriesMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapFetchKeysMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapFetchNearCacheInvalidationMetadataTask;
import com.hazelcast.client.impl.protocol.task.map.MapFetchWithQueryMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapFlushMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapForceUnlockMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapGetAllMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapGetEntryViewMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapGetMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapIsLockedMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapKeySetMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapKeySetWithPagingPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapKeySetWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapLoadAllMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapLoadGivenKeysMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapLockMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapMadePublishableMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapProjectionMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapProjectionWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPublisherCreateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPublisherCreateWithValueMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutAllMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutIfAbsentMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutIfAbsentWithMaxIdleMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutTransientMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutTransientWithMaxIdleMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapPutWithMaxIdleMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemoveAllMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemoveEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemoveIfSameMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemoveInterceptorMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapRemovePartitionLostListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapReplaceIfSameMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapReplaceMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSetMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSetReadCursorMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSetTtlMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSetWithMaxIdleMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapSubmitToKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapTryLockMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapTryPutMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapTryRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapUnlockMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapValuesMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapValuesWithPagingPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.map.MapValuesWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.metrics.ReadMetricsMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapAddEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapAddEntryListenerToKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapClearMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapContainsEntryMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapContainsKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapContainsValueMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapDeleteMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapEntrySetMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapForceUnlockMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapGetMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapIsLockedMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapKeySetMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapLockMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapPutAllMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapPutMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapRemoveEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapRemoveEntryMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapTryLockMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapUnlockMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapValueCountMessageTask;
import com.hazelcast.client.impl.protocol.task.multimap.MultiMapValuesMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueAddAllMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueAddListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueClearMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueCompareAndRemoveAllMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueCompareAndRetainAllMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueContainsAllMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueContainsMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueDrainMaxSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueDrainMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueIteratorMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueOfferMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueuePeekMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueuePollMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueuePutMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueRemainingCapacityMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueRemoveListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.queue.QueueTakeMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapAddEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapAddEntryListenerToKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapAddEntryListenerToKeyWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapAddEntryListenerWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapAddNearCacheListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapClearMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapContainsKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapContainsValueMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapEntrySetMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapGetMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapKeySetMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapPutAllMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapPutMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapRemoveEntryListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.replicatedmap.ReplicatedMapValuesMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferAddAllMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferAddMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferCapacityMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferHeadSequenceMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferReadManyMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferReadOneMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferRemainingCapacityMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.ringbuffer.RingbufferTailSequenceMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorGetAllScheduledMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorShutdownMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorSubmitToPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorSubmitToTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskCancelFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskCancelFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskDisposeFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskDisposeFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetDelayFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetDelayFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetResultFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetResultFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetStatisticsFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskGetStatisticsFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskIsCancelledFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskIsCancelledFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskIsDoneFromPartitionMessageTask;
import com.hazelcast.client.impl.protocol.task.scheduledexecutor.ScheduledExecutorTaskIsDoneFromTargetMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetAddAllMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetAddListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetAddMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetClearMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetCompareAndRemoveAllMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetCompareAndRetainAllMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetContainsAllMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetContainsMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetGetAllMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetRemoveListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.set.SetSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.topic.TopicAddMessageListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.topic.TopicPublishAllMessageTask;
import com.hazelcast.client.impl.protocol.task.topic.TopicPublishMessageTask;
import com.hazelcast.client.impl.protocol.task.topic.TopicRemoveMessageListenerMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.TransactionCommitMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.TransactionCreateMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.TransactionRollbackMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XAClearRemoteTransactionMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XACollectTransactionsMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XAFinalizeTransactionMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XATransactionCommitMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XATransactionCreateMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XATransactionPrepareMessageTask;
import com.hazelcast.client.impl.protocol.task.transaction.XATransactionRollbackMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionallist.TransactionalListAddMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionallist.TransactionalListRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionallist.TransactionalListSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapContainsKeyMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapDeleteMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapGetForUpdateMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapGetMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapIsEmptyMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapKeySetMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapKeySetWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapPutIfAbsentMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapPutMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapRemoveIfSameMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapReplaceIfSameMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapReplaceMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapSetMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapValuesMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmap.TransactionalMapValuesWithPredicateMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapGetMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapPutMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapRemoveEntryMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalmultimap.TransactionalMultiMapValueCountMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalqueue.TransactionalQueueOfferMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalqueue.TransactionalQueuePeekMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalqueue.TransactionalQueuePollMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalqueue.TransactionalQueueSizeMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalqueue.TransactionalQueueTakeMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalset.TransactionalSetAddMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalset.TransactionalSetRemoveMessageTask;
import com.hazelcast.client.impl.protocol.task.transactionalset.TransactionalSetSizeMessageTask;
import com.hazelcast.cp.internal.client.AddCPGroupAvailabilityListenerMessageTask;
import com.hazelcast.cp.internal.client.AddCPMembershipListenerMessageTask;
import com.hazelcast.cp.internal.client.RemoveCPGroupAvailabilityListenerMessageTask;
import com.hazelcast.cp.internal.client.RemoveCPMembershipListenerMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.AddAndGetMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.AlterMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.ApplyMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.CompareAndSetMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.GetAndAddMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.GetAndSetMessageTask;
import com.hazelcast.cp.internal.datastructures.atomiclong.client.GetMessageTask;
import com.hazelcast.cp.internal.datastructures.atomicref.client.ContainsMessageTask;
import com.hazelcast.cp.internal.datastructures.atomicref.client.SetMessageTask;
import com.hazelcast.cp.internal.datastructures.countdownlatch.client.AwaitMessageTask;
import com.hazelcast.cp.internal.datastructures.countdownlatch.client.CountDownMessageTask;
import com.hazelcast.cp.internal.datastructures.countdownlatch.client.GetCountMessageTask;
import com.hazelcast.cp.internal.datastructures.countdownlatch.client.GetRoundMessageTask;
import com.hazelcast.cp.internal.datastructures.countdownlatch.client.TrySetCountMessageTask;
import com.hazelcast.cp.internal.datastructures.lock.client.GetLockOwnershipStateMessageTask;
import com.hazelcast.cp.internal.datastructures.lock.client.LockMessageTask;
import com.hazelcast.cp.internal.datastructures.lock.client.TryLockMessageTask;
import com.hazelcast.cp.internal.datastructures.lock.client.UnlockMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.AcquirePermitsMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.AvailablePermitsMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.ChangePermitsMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.DrainPermitsMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.GetSemaphoreTypeMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.InitSemaphoreMessageTask;
import com.hazelcast.cp.internal.datastructures.semaphore.client.ReleasePermitsMessageTask;
import com.hazelcast.cp.internal.datastructures.spi.client.CreateRaftGroupMessageTask;
import com.hazelcast.cp.internal.datastructures.spi.client.DestroyRaftObjectMessageTask;
import com.hazelcast.cp.internal.session.client.CloseSessionMessageTask;
import com.hazelcast.cp.internal.session.client.CreateSessionMessageTask;
import com.hazelcast.cp.internal.session.client.GenerateThreadIdMessageTask;
import com.hazelcast.cp.internal.session.client.HeartbeatSessionMessageTask;
import com.hazelcast.flakeidgen.impl.client.NewIdBatchMessageTask;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.longregister.client.codec.LongRegisterAddAndGetCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterDecrementAndGetCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterGetAndAddCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterGetAndIncrementCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterGetAndSetCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterGetCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterIncrementAndGetCodec;
import com.hazelcast.internal.longregister.client.codec.LongRegisterSetCodec;
import com.hazelcast.internal.longregister.client.task.LongRegisterAddAndGetMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterDecrementAndGetMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterGetAndAddMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterGetAndIncrementMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterGetAndSetMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterGetMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterIncrementAndGetMessageTask;
import com.hazelcast.internal.longregister.client.task.LongRegisterSetMessageTask;
import com.hazelcast.internal.util.collection.Int2ObjectHashMap;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.NodeEngineImpl;
import com.hazelcast.sql.impl.client.SqlCloseMessageTask;
import com.hazelcast.sql.impl.client.SqlExecuteMessageTask;
import com.hazelcast.sql.impl.client.SqlFetchMessageTask;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static com.hazelcast.internal.util.MapUtil.createInt2ObjectHashMap;

public class DefaultMessageTaskFactoryProvider implements MessageTaskFactoryProvider {
    private static final int MESSAGE_TASK_PROVIDER_INITIAL_CAPACITY = 500;

    private final Int2ObjectHashMap<MessageTaskFactory> factories;

    private final Node node;

    public DefaultMessageTaskFactoryProvider(NodeEngine nodeEngine) {
        this.node = ((NodeEngineImpl) nodeEngine).getNode();
        this.factories = createInt2ObjectHashMap(MESSAGE_TASK_PROVIDER_INITIAL_CAPACITY);
        initFactories();
    }

    public void initFactories() {
        initializeSetTaskFactories();
        initializeRingBufferTaskFactories();
        initializeCacheTaskFactories();
        initializeReplicatedMapTaskFactories();
        initializeLongRegisterClientTaskFactories();
        initializeTransactionalListTaskFactories();
        initializeTransactionalMultiMapTaskFactories();
        initializeListTaskFactories();
        initializeTransactionalQueueTaskFactories();
        initializeMultiMapTaskFactories();
        initializeTopicTaskFactories();
        initializeTransactionalMapTaskFactories();
        initializeExecutorServiceTaskFactories();
        initializeDurableExecutorTaskFactories();
        initializeTransactionTaskFactories();
        initializeTransactionalSetTaskFactories();
        initializeMapTaskFactories();
        initializeGeneralTaskFactories();
        initializeQueueTaskFactories();
        initializeCardinalityTaskFactories();
        initializeScheduledExecutorTaskFactories();
        initializeContinuousMapQueryOperations();
        initializeDynamicConfigTaskFactories();
        initializeFlakeIdGeneratorTaskFactories();
        initializePnCounterTaskFactories();
        initializeCPGroupTaskFactories();
        initializeCPListenerTaskFactories();
        initializeAtomicLongTaskFactories();
        initializeAtomicReferenceTaskFactories();
        initializeCountDownLatchTaskFactories();
        initializeFencedLockTaskFactories();
        initializeSemaphoreTaskFactories();
        initializeManagementCenterTaskFactories();
        initializeSqlTaskFactories();
    }

    private void initializeSetTaskFactories() {
        factories.put(ServerSetRemoveListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetRemoveListenerMessageTask(cm, node, con));
        factories.put(ServerSetClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetClearMessageTask(cm, node, con));
        factories.put(ServerSetCompareAndRemoveAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetCompareAndRemoveAllMessageTask(cm, node, con));
        factories.put(ServerSetContainsAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetContainsAllMessageTask(cm, node, con));
        factories.put(ServerSetIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetIsEmptyMessageTask(cm, node, con));
        factories.put(ServerSetAddAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetAddAllMessageTask(cm, node, con));
        factories.put(ServerSetAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetAddMessageTask(cm, node, con));
        factories.put(ServerSetCompareAndRetainAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetCompareAndRetainAllMessageTask(cm, node, con));
        factories.put(ServerSetGetAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetGetAllMessageTask(cm, node, con));
        factories.put(ServerSetRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetRemoveMessageTask(cm, node, con));
        factories.put(ServerSetAddListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetAddListenerMessageTask(cm, node, con));
        factories.put(ServerSetContainsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetContainsMessageTask(cm, node, con));
        factories.put(ServerSetSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetSizeMessageTask(cm, node, con));
    }

    private void initializeRingBufferTaskFactories() {
        factories.put(ServerRingbufferReadOneCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferReadOneMessageTask(cm, node, con));
        factories.put(ServerRingbufferAddAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferAddAllMessageTask(cm, node, con));
        factories.put(ServerRingbufferCapacityCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferCapacityMessageTask(cm, node, con));
        factories.put(ServerRingbufferTailSequenceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferTailSequenceMessageTask(cm, node, con));
        factories.put(ServerRingbufferAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferAddMessageTask(cm, node, con));
        factories.put(ServerRingbufferRemainingCapacityCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferRemainingCapacityMessageTask(cm, node, con));
        factories.put(ServerRingbufferReadManyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferReadManyMessageTask(cm, node, con));
        factories.put(ServerRingbufferHeadSequenceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferHeadSequenceMessageTask(cm, node, con));
        factories.put(ServerRingbufferSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RingbufferSizeMessageTask(cm, node, con));
    }

    private void initializeCacheTaskFactories() {
        factories.put(ServerCacheClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheClearMessageTask(cm, node, con));
        factories.put(ServerCacheFetchNearCacheInvalidationMetadataCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheFetchNearCacheInvalidationMetadataTask(cm, node, con));
        factories.put(ServerCacheReplaceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheReplaceMessageTask(cm, node, con));
        factories.put(ServerCacheContainsKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheContainsKeyMessageTask(cm, node, con));
        factories.put(ServerCacheCreateConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheCreateConfigMessageTask(cm, node, con));
        factories.put(ServerCacheGetAndReplaceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheGetAndReplaceMessageTask(cm, node, con));
        factories.put(ServerCacheGetAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheGetAllMessageTask(cm, node, con));
        factories.put(ServerCachePutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CachePutMessageTask(cm, node, con));
        factories.put(ServerCacheAddNearCacheInvalidationListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheAddNearCacheInvalidationListenerTask(cm, node, con));
        factories.put(ServerCachePutAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CachePutAllMessageTask(cm, node, con));
        factories.put(ServerCacheSetExpiryPolicyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheSetExpiryPolicyMessageTask(cm, node, con));
        factories.put(ServerCacheLoadAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheLoadAllMessageTask(cm, node, con));
        factories.put(ServerCacheListenerRegistrationCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheListenerRegistrationMessageTask(cm, node, con));
        factories.put(ServerCacheAddEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheAddEntryListenerMessageTask(cm, node, con));
        factories.put(ServerCacheRemoveEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemoveEntryListenerMessageTask(cm, node, con));
        factories.put(ServerCacheRemoveInvalidationListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemoveInvalidationListenerMessageTask(cm, node, con));
        factories.put(ServerCacheDestroyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheDestroyMessageTask(cm, node, con));
        factories.put(ServerCacheRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemoveMessageTask(cm, node, con));
        factories.put(ServerCacheEntryProcessorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheEntryProcessorMessageTask(cm, node, con));
        factories.put(ServerCacheGetAndRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheGetAndRemoveMessageTask(cm, node, con));
        factories.put(ServerCacheManagementConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheManagementConfigMessageTask(cm, node, con));
        factories.put(ServerCachePutIfAbsentCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CachePutIfAbsentMessageTask(cm, node, con));
        factories.put(ServerCacheRemoveAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemoveAllMessageTask(cm, node, con));
        factories.put(ServerCacheRemoveAllKeysCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemoveAllKeysMessageTask(cm, node, con));
        factories.put(ServerCacheIterateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheIterateMessageTask(cm, node, con));
        factories.put(ServerCacheAddPartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheAddPartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerCacheGetConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheGetConfigMessageTask(cm, node, con));
        factories.put(ServerCacheGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheGetMessageTask(cm, node, con));
        factories.put(ServerCacheRemovePartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheRemovePartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerCacheSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheSizeMessageTask(cm, node, con));
        factories.put(ServerCacheIterateEntriesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheIterateEntriesMessageTask(cm, node, con));
        factories.put(ServerCacheEventJournalSubscribeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheEventJournalSubscribeTask(cm, node, con));
        factories.put(ServerCacheEventJournalReadCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CacheEventJournalReadTask<>(cm, node, con));
    }

    private void initializeReplicatedMapTaskFactories() {
        factories.put(ServerReplicatedMapSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapSizeMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapRemoveEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapRemoveEntryListenerMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapAddEntryListenerToKeyWithPredicateMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapIsEmptyMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapPutAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapPutAllMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapContainsKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapContainsKeyMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapContainsValueCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapContainsValueMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapAddNearCacheEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapAddNearCacheListenerMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapGetMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapAddEntryListenerWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapAddEntryListenerWithPredicateMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapAddEntryListenerToKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapAddEntryListenerToKeyMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapRemoveMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapClearMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapValuesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapValuesMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapEntrySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapEntrySetMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapPutMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapAddEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapAddEntryListenerMessageTask(cm, node, con));
        factories.put(ServerReplicatedMapKeySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReplicatedMapKeySetMessageTask(cm, node, con));
    }

    private void initializeLongRegisterClientTaskFactories() {
        factories.put(LongRegisterDecrementAndGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterDecrementAndGetMessageTask(cm, node, con));
        factories.put(LongRegisterGetAndAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterGetAndAddMessageTask(cm, node, con));
        factories.put(LongRegisterAddAndGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterAddAndGetMessageTask(cm, node, con));
        factories.put(LongRegisterGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterGetMessageTask(cm, node, con));
        factories.put(LongRegisterSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterSetMessageTask(cm, node, con));
        factories.put(LongRegisterIncrementAndGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterIncrementAndGetMessageTask(cm, node, con));
        factories.put(LongRegisterGetAndSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterGetAndSetMessageTask(cm, node, con));
        factories.put(LongRegisterGetAndIncrementCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LongRegisterGetAndIncrementMessageTask(cm, node, con));
    }

    private void initializeTransactionalListTaskFactories() {
        factories.put(ServerTransactionalListSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalListSizeMessageTask(cm, node, con));
        factories.put(ServerTransactionalListRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalListRemoveMessageTask(cm, node, con));
        factories.put(ServerTransactionalListAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalListAddMessageTask(cm, node, con));
    }

    private void initializeTransactionalMultiMapTaskFactories() {
        factories.put(ServerTransactionalMultiMapPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapPutMessageTask(cm, node, con));
        factories.put(ServerTransactionalMultiMapRemoveEntryCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapRemoveEntryMessageTask(cm, node, con));
        factories.put(ServerTransactionalMultiMapGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapGetMessageTask(cm, node, con));
        factories.put(ServerTransactionalMultiMapRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapRemoveMessageTask(cm, node, con));
        factories.put(ServerTransactionalMultiMapSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapSizeMessageTask(cm, node, con));
        factories.put(ServerTransactionalMultiMapValueCountCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMultiMapValueCountMessageTask(cm, node, con));
    }

    private void initializeListTaskFactories() {
        factories.put(ServerListGetAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListGetAllMessageTask(cm, node, con));
        factories.put(ServerListListIteratorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListListIteratorMessageTask(cm, node, con));
        factories.put(ServerListSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListSetMessageTask(cm, node, con));
        factories.put(ServerListAddAllWithIndexCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListAddAllWithIndexMessageTask(cm, node, con));
        factories.put(ServerListCompareAndRemoveAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListCompareAndRemoveAllMessageTask(cm, node, con));
        factories.put(ServerListGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListGetMessageTask(cm, node, con));
        factories.put(ServerListRemoveListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListRemoveListenerMessageTask(cm, node, con));
        factories.put(ServerListRemoveWithIndexCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListRemoveWithIndexMessageTask(cm, node, con));
        factories.put(ServerListAddListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListAddListenerMessageTask(cm, node, con));
        factories.put(ServerListIteratorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListIteratorMessageTask(cm, node, con));
        factories.put(ServerListClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListClearMessageTask(cm, node, con));
        factories.put(ServerListAddAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListAddAllMessageTask(cm, node, con));
        factories.put(ServerListAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListAddMessageTask(cm, node, con));
        factories.put(ServerListAddWithIndexCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListAddWithIndexMessageTask(cm, node, con));
        factories.put(ServerListLastIndexOfCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListLastIndexOfMessageTask(cm, node, con));
        factories.put(ServerListRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListRemoveMessageTask(cm, node, con));
        factories.put(ServerListSubCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListSubMessageTask(cm, node, con));
        factories.put(ServerListContainsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListContainsMessageTask(cm, node, con));
        factories.put(ServerListIndexOfCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListIndexOfMessageTask(cm, node, con));
        factories.put(ServerListSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListSizeMessageTask(cm, node, con));
        factories.put(ServerListContainsAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListContainsAllMessageTask(cm, node, con));
        factories.put(ServerListIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListIsEmptyMessageTask(cm, node, con));
        factories.put(ServerListCompareAndRetainAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ListCompareAndRetainAllMessageTask(cm, node, con));
    }

    private void initializeTransactionalQueueTaskFactories() {
        factories.put(ServerTransactionalQueueSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalQueueSizeMessageTask(cm, node, con));
        factories.put(ServerTransactionalQueueOfferCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalQueueOfferMessageTask(cm, node, con));
        factories.put(ServerTransactionalQueuePeekCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalQueuePeekMessageTask(cm, node, con));
        factories.put(ServerTransactionalQueuePollCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalQueuePollMessageTask(cm, node, con));
        factories.put(ServerTransactionalQueueTakeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalQueueTakeMessageTask(cm, node, con));
    }

    private void initializeMultiMapTaskFactories() {
        factories.put(ServerMultiMapClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapClearMessageTask(cm, node, con));
        factories.put(ServerMultiMapGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapGetMessageTask(cm, node, con));
        factories.put(ServerMultiMapRemoveEntryCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapRemoveEntryMessageTask(cm, node, con));
        factories.put(ServerMultiMapContainsKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapContainsKeyMessageTask(cm, node, con));
        factories.put(ServerMultiMapSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapSizeMessageTask(cm, node, con));
        factories.put(ServerMultiMapAddEntryListenerToKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapAddEntryListenerToKeyMessageTask(cm, node, con));
        factories.put(ServerMultiMapAddEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapAddEntryListenerMessageTask(cm, node, con));
        factories.put(ServerMultiMapRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapRemoveMessageTask(cm, node, con));
        factories.put(ServerMultiMapTryLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapTryLockMessageTask(cm, node, con));
        factories.put(ServerMultiMapIsLockedCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapIsLockedMessageTask(cm, node, con));
        factories.put(ServerMultiMapContainsValueCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapContainsValueMessageTask(cm, node, con));
        factories.put(ServerMultiMapKeySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapKeySetMessageTask(cm, node, con));
        factories.put(ServerMultiMapPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapPutMessageTask(cm, node, con));
        factories.put(ServerMultiMapPutAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapPutAllMessageTask(cm, node, con));
        factories.put(ServerMultiMapEntrySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapEntrySetMessageTask(cm, node, con));
        factories.put(ServerMultiMapValueCountCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapValueCountMessageTask(cm, node, con));
        factories.put(ServerMultiMapUnlockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapUnlockMessageTask(cm, node, con));
        factories.put(ServerMultiMapLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapLockMessageTask(cm, node, con));
        factories.put(ServerMultiMapRemoveEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapRemoveEntryListenerMessageTask(cm, node, con));
        factories.put(ServerMultiMapContainsEntryCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapContainsEntryMessageTask(cm, node, con));
        factories.put(ServerMultiMapForceUnlockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapForceUnlockMessageTask(cm, node, con));
        factories.put(ServerMultiMapValuesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapValuesMessageTask(cm, node, con));
        factories.put(ServerMultiMapDeleteCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MultiMapDeleteMessageTask(cm, node, con));
    }

    private void initializeTopicTaskFactories() {
        factories.put(ServerTopicPublishCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TopicPublishMessageTask(cm, node, con));
        factories.put(ServerTopicPublishAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TopicPublishAllMessageTask(cm, node, con));
        factories.put(ServerTopicAddMessageListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TopicAddMessageListenerMessageTask(cm, node, con));
        factories.put(ServerTopicRemoveMessageListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TopicRemoveMessageListenerMessageTask(cm, node, con));
    }

    private void initializeTransactionalMapTaskFactories() {
        factories.put(ServerTransactionalMapValuesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapValuesMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapSizeMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapPutIfAbsentCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapPutIfAbsentMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapRemoveMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapGetMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapGetForUpdateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapGetForUpdateMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapIsEmptyMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapKeySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapKeySetMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapKeySetWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapKeySetWithPredicateMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapReplaceIfSameCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapReplaceIfSameMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapContainsKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapContainsKeyMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapRemoveIfSameCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapRemoveIfSameMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapSetMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapReplaceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapReplaceMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapPutMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapDeleteCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapDeleteMessageTask(cm, node, con));
        factories.put(ServerTransactionalMapValuesWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalMapValuesWithPredicateMessageTask(cm, node, con));
    }

    private void initializeExecutorServiceTaskFactories() {
        factories.put(ServerExecutorServiceCancelOnPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceCancelOnPartitionMessageTask(cm, node, con));
        factories.put(ServerExecutorServiceSubmitToPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceSubmitToPartitionMessageTask(cm, node, con));
        factories.put(ServerExecutorServiceCancelOnMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceCancelOnAddressMessageTask(cm, node, con));
        factories.put(ServerExecutorServiceIsShutdownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceIsShutdownMessageTask(cm, node, con));
        factories.put(ServerExecutorServiceShutdownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceShutdownMessageTask(cm, node, con));
        factories.put(ServerExecutorServiceSubmitToMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ExecutorServiceSubmitToAddressMessageTask(cm, node, con));
    }

    private void initializeDurableExecutorTaskFactories() {
        factories.put(ServerDurableExecutorSubmitToPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorSubmitToPartitionMessageTask(cm, node, con));
        factories.put(ServerDurableExecutorIsShutdownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorIsShutdownMessageTask(cm, node, con));
        factories.put(ServerDurableExecutorShutdownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorShutdownMessageTask(cm, node, con));
        factories.put(ServerDurableExecutorRetrieveResultCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorRetrieveResultMessageTask(cm, node, con));
        factories.put(ServerDurableExecutorDisposeResultCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorDisposeResultMessageTask(cm, node, con));
        factories.put(ServerDurableExecutorRetrieveAndDisposeResultCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DurableExecutorRetrieveAndDisposeResultMessageTask(cm, node, con));
    }

    private void initializeTransactionTaskFactories() {
        factories.put(ServerTransactionCreateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionCreateMessageTask(cm, node, con));
        factories.put(ServerXATransactionClearRemoteCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XAClearRemoteTransactionMessageTask(cm, node, con));
        factories.put(ServerXATransactionFinalizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XAFinalizeTransactionMessageTask(cm, node, con));
        factories.put(ServerTransactionCommitCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionCommitMessageTask(cm, node, con));
        factories.put(ServerXATransactionCollectTransactionsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XACollectTransactionsMessageTask(cm, node, con));
        factories.put(ServerXATransactionPrepareCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XATransactionPrepareMessageTask(cm, node, con));
        factories.put(ServerXATransactionCreateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XATransactionCreateMessageTask(cm, node, con));
        factories.put(ServerTransactionRollbackCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionRollbackMessageTask(cm, node, con));
        factories.put(ServerXATransactionCommitCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XATransactionCommitMessageTask(cm, node, con));
        factories.put(ServerXATransactionRollbackCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new XATransactionRollbackMessageTask(cm, node, con));
    }

    private void initializeTransactionalSetTaskFactories() {
        factories.put(ServerTransactionalSetSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalSetSizeMessageTask(cm, node, con));
        factories.put(ServerTransactionalSetAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalSetAddMessageTask(cm, node, con));
        factories.put(ServerTransactionalSetRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TransactionalSetRemoveMessageTask(cm, node, con));
    }

    private void initializeMapTaskFactories() {
        factories.put(ServerMapEntriesWithPagingPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEntriesWithPagingPredicateMessageTask(cm, node, con));
        factories.put(ServerMapAddEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddEntryListenerMessageTask(cm, node, con));
        factories.put(ServerMapFetchNearCacheInvalidationMetadataCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapFetchNearCacheInvalidationMetadataTask(cm, node, con));
        factories.put(ServerMapRemoveIfSameCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemoveIfSameMessageTask(cm, node, con));
        factories.put(ServerMapAddInterceptorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddInterceptorMessageTask(cm, node, con));
        factories.put(ServerMapEntriesWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEntriesWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapPutTransientCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutTransientMessageTask(cm, node, con));
        factories.put(ServerMapContainsValueCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapContainsValueMessageTask(cm, node, con));
        factories.put(ServerMapIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapIsEmptyMessageTask(cm, node, con));
        factories.put(ServerMapReplaceCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapReplaceMessageTask(cm, node, con));
        factories.put(ServerMapRemoveInterceptorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemoveInterceptorMessageTask(cm, node, con));
        factories.put(ServerMapAddNearCacheInvalidationListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddNearCacheInvalidationListenerMessageTask(cm, node, con));
        factories.put(ServerMapExecuteOnAllKeysCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapExecuteOnAllKeysMessageTask(cm, node, con));
        factories.put(ServerMapFlushCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapFlushMessageTask(cm, node, con));
        factories.put(ServerMapSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSetMessageTask(cm, node, con));
        factories.put(ServerMapTryLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapTryLockMessageTask(cm, node, con));
        factories.put(ServerMapAddEntryListenerToKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddEntryListenerToKeyMessageTask(cm, node, con));
        factories.put(ServerMapEntrySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEntrySetMessageTask(cm, node, con));
        factories.put(ServerMapClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapClearMessageTask(cm, node, con));
        factories.put(ServerMapLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapLockMessageTask(cm, node, con));
        factories.put(ServerMapGetEntryViewCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapGetEntryViewMessageTask(cm, node, con));
        factories.put(ServerMapRemovePartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemovePartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerMapLoadGivenKeysCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapLoadGivenKeysMessageTask(cm, node, con));
        factories.put(ServerMapExecuteWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapExecuteWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapRemoveAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemoveAllMessageTask(cm, node, con));
        factories.put(ServerMapPutIfAbsentCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutIfAbsentMessageTask(cm, node, con));
        factories.put(ServerMapTryRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapTryRemoveMessageTask(cm, node, con));
        factories.put(ServerMapPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutMessageTask(cm, node, con));
        factories.put(ServerMapUnlockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapUnlockMessageTask(cm, node, con));
        factories.put(ServerMapSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSizeMessageTask(cm, node, con));
        factories.put(ServerMapValuesWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapValuesWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapAddEntryListenerToKeyWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddEntryListenerToKeyWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapEvictCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEvictMessageTask(cm, node, con));
        factories.put(ServerMapGetAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapGetAllMessageTask(cm, node, con));
        factories.put(ServerMapForceUnlockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapForceUnlockMessageTask(cm, node, con));
        factories.put(ServerMapLoadAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapLoadAllMessageTask(cm, node, con));
        factories.put(ServerMapAddIndexCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddIndexMessageTask(cm, node, con));
        factories.put(ServerMapExecuteOnKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapExecuteOnKeyMessageTask(cm, node, con));
        factories.put(ServerMapKeySetWithPagingPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapKeySetWithPagingPredicateMessageTask(cm, node, con));
        factories.put(ServerMapRemoveEntryListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemoveEntryListenerMessageTask(cm, node, con));
        factories.put(ServerMapIsLockedCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapIsLockedMessageTask(cm, node, con));
        factories.put(ServerMapEvictAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEvictAllMessageTask(cm, node, con));
        factories.put(ServerMapSubmitToKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSubmitToKeyMessageTask(cm, node, con));
        factories.put(ServerMapValuesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapValuesMessageTask(cm, node, con));
        factories.put(ServerMapAddEntryListenerWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddEntryListenerWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapDeleteCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapDeleteMessageTask(cm, node, con));
        factories.put(ServerMapAddPartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddPartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerMapPutAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutAllMessageTask(cm, node, con));
        factories.put(ServerMapRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapRemoveMessageTask(cm, node, con));
        factories.put(ServerMapKeySetWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapKeySetWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapExecuteOnKeysCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapExecuteOnKeysMessageTask(cm, node, con));
        factories.put(ServerMapReplaceIfSameCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapReplaceIfSameMessageTask(cm, node, con));
        factories.put(ServerMapContainsKeyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapContainsKeyMessageTask(cm, node, con));
        factories.put(ServerMapTryPutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapTryPutMessageTask(cm, node, con));
        factories.put(ServerMapValuesWithPagingPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapValuesWithPagingPredicateMessageTask(cm, node, con));
        factories.put(ServerMapGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapGetMessageTask(cm, node, con));
        factories.put(ServerMapKeySetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapKeySetMessageTask(cm, node, con));
        factories.put(ServerMapFetchKeysCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapFetchKeysMessageTask(cm, node, con));
        factories.put(ServerMapFetchEntriesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapFetchEntriesMessageTask(cm, node, con));
        factories.put(ServerMapAggregateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAggregateMessageTask(cm, node, con));
        factories.put(ServerMapAggregateWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAggregateWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapProjectCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapProjectionMessageTask(cm, node, con));
        factories.put(ServerMapProjectWithPredicateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapProjectionWithPredicateMessageTask(cm, node, con));
        factories.put(ServerMapFetchWithQueryCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapFetchWithQueryMessageTask(cm, node, con));
        factories.put(ServerMapEventJournalSubscribeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEventJournalSubscribeTask(cm, node, con));
        factories.put(ServerMapEventJournalReadCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapEventJournalReadTask<>(cm, node, con));
        factories.put(ServerMapSetTtlCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSetTtlMessageTask(cm, node, con));
        factories.put(ServerMapSetWithMaxIdleCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSetWithMaxIdleMessageTask(cm, node, con));
        factories.put(ServerMapPutWithMaxIdleCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutWithMaxIdleMessageTask(cm, node, con));
        factories.put(ServerMapPutIfAbsentWithMaxIdleCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutIfAbsentWithMaxIdleMessageTask(cm, node, con));
        factories.put(ServerMapPutTransientWithMaxIdleCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPutTransientWithMaxIdleMessageTask(cm, node, con));
    }

    private void initializeGeneralTaskFactories() {
        factories.put(ServerClientAddPartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddPartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerClientRemovePartitionLostListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemovePartitionLostListenerMessageTask(cm, node, con));
        factories.put(ServerClientAddMigrationListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddMigrationListenerMessageTask(cm, node, con));
        factories.put(ServerClientRemoveMigrationListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemoveMigrationListenerMessageTask(cm, node, con));
        factories.put(ServerClientCreateProxyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CreateProxyMessageTask(cm, node, con));
        factories.put(ServerClientGetDistributedObjectsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetDistributedObjectsMessageTask(cm, node, con));
        factories.put(ServerClientAddDistributedObjectListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddDistributedObjectListenerMessageTask(cm, node, con));
        factories.put(ServerClientDestroyProxyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DestroyProxyMessageTask(cm, node, con));
        factories.put(ServerClientPingCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PingMessageTask(cm, node, con));
        factories.put(ServerClientAddClusterViewListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddClusterViewListenerMessageTask(cm, node, con));
        factories.put(ServerClientAuthenticationCustomCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AuthenticationCustomCredentialsMessageTask(cm, node, con));
        factories.put(ServerClientRemoveDistributedObjectListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemoveDistributedObjectListenerMessageTask(cm, node, con));
        factories.put(ServerClientAuthenticationCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AuthenticationMessageTask(cm, node, con));
        factories.put(ServerClientStatisticsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ClientStatisticsMessageTask(cm, node, con));
        factories.put(ServerClientDeployClassesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DeployClassesMessageTask(cm, node, con));
        factories.put(ServerClientCreateProxiesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CreateProxiesMessageTask(cm, node, con));
        factories.put(ServerClientLocalBackupListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddBackupListenerMessageTask(cm, node, con));
        factories.put(ServerClientTriggerPartitionAssignmentCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TriggerPartitionAssignmentMessageTask(cm, node, con));
    }

    private void initializeQueueTaskFactories() {
        factories.put(ServerQueueCompareAndRemoveAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueCompareAndRemoveAllMessageTask(cm, node, con));
        factories.put(ServerQueueContainsAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueContainsAllMessageTask(cm, node, con));
        factories.put(ServerQueueAddAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueAddAllMessageTask(cm, node, con));
        factories.put(ServerQueueTakeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueTakeMessageTask(cm, node, con));
        factories.put(ServerQueueAddListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueAddListenerMessageTask(cm, node, con));
        factories.put(ServerQueueCompareAndRetainAllCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueCompareAndRetainAllMessageTask(cm, node, con));
        factories.put(ServerQueueOfferCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueOfferMessageTask(cm, node, con));
        factories.put(ServerQueuePeekCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueuePeekMessageTask(cm, node, con));
        factories.put(ServerQueueRemoveCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueRemoveMessageTask(cm, node, con));
        factories.put(ServerQueueIsEmptyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueIsEmptyMessageTask(cm, node, con));
        factories.put(ServerQueueIteratorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueIteratorMessageTask(cm, node, con));
        factories.put(ServerQueueSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueSizeMessageTask(cm, node, con));
        factories.put(ServerQueuePutCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueuePutMessageTask(cm, node, con));
        factories.put(ServerQueueContainsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueContainsMessageTask(cm, node, con));
        factories.put(ServerQueuePollCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueuePollMessageTask(cm, node, con));
        factories.put(ServerQueueDrainToCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueDrainMessageTask(cm, node, con));
        factories.put(ServerQueueRemoveListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueRemoveListenerMessageTask(cm, node, con));
        factories.put(ServerQueueRemainingCapacityCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueRemainingCapacityMessageTask(cm, node, con));
        factories.put(ServerQueueClearCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueClearMessageTask(cm, node, con));
        factories.put(ServerQueueDrainToMaxSizeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new QueueDrainMaxSizeMessageTask(cm, node, con));
    }

    private void initializeCardinalityTaskFactories() {
        factories.put(ServerCardinalityEstimatorAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CardinalityEstimatorAddMessageTask(cm, node, con));
        factories.put(ServerCardinalityEstimatorEstimateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CardinalityEstimatorEstimateMessageTask(cm, node, con));
    }

    private void initializeScheduledExecutorTaskFactories() {
        factories.put(ServerScheduledExecutorSubmitToPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorSubmitToPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorSubmitToMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorSubmitToTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorShutdownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorShutdownMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorDisposeFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskDisposeFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorDisposeFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskDisposeFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorCancelFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskCancelFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorCancelFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskCancelFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorIsDoneFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskIsDoneFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorIsDoneFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskIsDoneFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetDelayFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetDelayFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetDelayFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetDelayFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetStatsFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetStatisticsFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetStatsFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetStatisticsFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetResultFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetResultFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetResultFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskGetResultFromTargetMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorGetAllScheduledFuturesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorGetAllScheduledMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorIsCancelledFromPartitionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskIsCancelledFromPartitionMessageTask(cm, node, con));
        factories.put(ServerScheduledExecutorIsCancelledFromMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ScheduledExecutorTaskIsCancelledFromTargetMessageTask(cm, node, con));
    }

    private void initializeContinuousMapQueryOperations() {
        factories.put(ServerContinuousQueryDestroyCacheCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapDestroyCacheMessageTask(cm, node, con));
        factories.put(ServerContinuousQueryPublisherCreateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPublisherCreateMessageTask(cm, node, con));
        factories.put(ServerContinuousQuerySetReadCursorCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapSetReadCursorMessageTask(cm, node, con));
        factories.put(ServerContinuousQueryAddListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapAddListenerMessageTask(cm, node, con));
        factories.put(ServerContinuousQueryMadePublishableCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapMadePublishableMessageTask(cm, node, con));
        factories.put(ServerContinuousQueryPublisherCreateWithValueCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MapPublisherCreateWithValueMessageTask(cm, node, con));
    }

    private void initializeDynamicConfigTaskFactories() {
        factories.put(ServerDynamicConfigAddMultiMapConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddMultiMapConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddCardinalityEstimatorConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddCardinalityEstimatorConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddExecutorConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddExecutorConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddDurableExecutorConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddDurableExecutorConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddScheduledExecutorConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddScheduledExecutorConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddRingbufferConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddRingbufferConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddListConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddListConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddSetConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddSetConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddTopicConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddTopicConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddReplicatedMapConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddReplicatedMapConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddQueueConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddQueueConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddMapConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddMapConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddReliableTopicConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddReliableTopicConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddCacheConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddCacheConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddFlakeIdGeneratorConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddFlakeIdGeneratorConfigMessageTask(cm, node, con));
        factories.put(ServerDynamicConfigAddPNCounterConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddPNCounterConfigMessageTask(cm, node, con));
    }

    private void initializeFlakeIdGeneratorTaskFactories() {
        factories.put(ServerFlakeIdGeneratorNewIdBatchCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new NewIdBatchMessageTask(cm, node, con));
    }

    private void initializePnCounterTaskFactories() {
        factories.put(ServerPNCounterGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PNCounterGetMessageTask(cm, node, con));
        factories.put(ServerPNCounterAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PNCounterAddMessageTask(cm, node, con));
        factories.put(ServerPNCounterGetConfiguredReplicaCountCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PNCounterGetConfiguredReplicaCountMessageTask(cm, node, con));
    }

    private void initializeCPGroupTaskFactories() {
        factories.put(ServerCPGroupCreateCPGroupCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CreateRaftGroupMessageTask(cm, node, con));
        factories.put(ServerCPGroupDestroyCPObjectCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DestroyRaftObjectMessageTask(cm, node, con));

        factories.put(ServerCPSessionCreateSessionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CreateSessionMessageTask(cm, node, con));
        factories.put(ServerCPSessionHeartbeatSessionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new HeartbeatSessionMessageTask(cm, node, con));
        factories.put(ServerCPSessionCloseSessionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CloseSessionMessageTask(cm, node, con));
        factories.put(ServerCPSessionGenerateThreadIdCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GenerateThreadIdMessageTask(cm, node, con));
    }

    private void initializeCPListenerTaskFactories() {
        factories.put(ServerCPSubsystemAddMembershipListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddCPMembershipListenerMessageTask(cm, node, con));
        factories.put(ServerCPSubsystemRemoveMembershipListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemoveCPMembershipListenerMessageTask(cm, node, con));
        factories.put(ServerCPSubsystemAddGroupAvailabilityListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddCPGroupAvailabilityListenerMessageTask(cm, node, con));
        factories.put(ServerCPSubsystemRemoveGroupAvailabilityListenerCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemoveCPGroupAvailabilityListenerMessageTask(cm, node, con));
    }

    private void initializeAtomicLongTaskFactories() {
        factories.put(ServerAtomicLongAddAndGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddAndGetMessageTask(cm, node, con));
        factories.put(ServerAtomicLongCompareAndSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CompareAndSetMessageTask(cm, node, con));
        factories.put(ServerAtomicLongGetAndAddCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetAndAddMessageTask(cm, node, con));
        factories.put(ServerAtomicLongGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetMessageTask(cm, node, con));
        factories.put(ServerAtomicLongGetAndSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetAndSetMessageTask(cm, node, con));
        factories.put(ServerAtomicLongApplyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ApplyMessageTask(cm, node, con));
        factories.put(ServerAtomicLongAlterCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AlterMessageTask(cm, node, con));
    }

    private void initializeAtomicReferenceTaskFactories() {
        factories.put(ServerAtomicRefApplyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new com.hazelcast.cp.internal.datastructures.atomicref.client.ApplyMessageTask(cm, node, con));
        factories.put(ServerAtomicRefSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SetMessageTask(cm, node, con));
        factories.put(ServerAtomicRefContainsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ContainsMessageTask(cm, node, con));
        factories.put(ServerAtomicRefGetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new com.hazelcast.cp.internal.datastructures.atomicref.client.GetMessageTask(cm, node, con));
        factories.put(ServerAtomicRefCompareAndSetCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new com.hazelcast.cp.internal.datastructures.atomicref.client.CompareAndSetMessageTask(cm, node, con));
    }

    private void initializeCountDownLatchTaskFactories() {
        factories.put(ServerCountDownLatchAwaitCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AwaitMessageTask(cm, node, con));
        factories.put(ServerCountDownLatchCountDownCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CountDownMessageTask(cm, node, con));
        factories.put(ServerCountDownLatchGetCountCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetCountMessageTask(cm, node, con));
        factories.put(ServerCountDownLatchGetRoundCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetRoundMessageTask(cm, node, con));
        factories.put(ServerCountDownLatchTrySetCountCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TrySetCountMessageTask(cm, node, con));
    }

    private void initializeFencedLockTaskFactories() {
        factories.put(ServerFencedLockLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new LockMessageTask(cm, node, con));
        factories.put(ServerFencedLockTryLockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new TryLockMessageTask(cm, node, con));
        factories.put(ServerFencedLockUnlockCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new UnlockMessageTask(cm, node, con));
        factories.put(ServerFencedLockGetLockOwnershipCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetLockOwnershipStateMessageTask(cm, node, con));
    }

    private void initializeSemaphoreTaskFactories() {
        factories.put(ServerSemaphoreAcquireCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AcquirePermitsMessageTask(cm, node, con));
        factories.put(ServerSemaphoreAvailablePermitsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AvailablePermitsMessageTask(cm, node, con));
        factories.put(ServerSemaphoreChangeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ChangePermitsMessageTask(cm, node, con));
        factories.put(ServerSemaphoreDrainCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new DrainPermitsMessageTask(cm, node, con));
        factories.put(ServerSemaphoreGetSemaphoreTypeCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetSemaphoreTypeMessageTask(cm, node, con));
        factories.put(ServerSemaphoreInitCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new InitSemaphoreMessageTask(cm, node, con));
        factories.put(ServerSemaphoreReleaseCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReleasePermitsMessageTask(cm, node, con));
    }

    private void initializeManagementCenterTaskFactories() {
        factories.put(ServerMCReadMetricsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ReadMetricsMessageTask(cm, node, con));
        factories.put(ServerMCChangeClusterStateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ChangeClusterStateMessageTask(cm, node, con));
        factories.put(ServerMCGetMapConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetMapConfigMessageTask(cm, node, con));
        factories.put(ServerMCUpdateMapConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new UpdateMapConfigMessageTask(cm, node, con));
        factories.put(ServerMCGetMemberConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetMemberConfigMessageTask(cm, node, con));
        factories.put(ServerMCRunGcCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RunGcMessageTask(cm, node, con));
        factories.put(ServerMCGetThreadDumpCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetThreadDumpMessageTask(cm, node, con));
        factories.put(ServerMCShutdownMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ShutdownMemberMessageTask(cm, node, con));
        factories.put(ServerMCPromoteLiteMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PromoteLiteMemberMessageTask(cm, node, con));
        factories.put(ServerMCGetSystemPropertiesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetSystemPropertiesMessageTask(cm, node, con));
        factories.put(ServerMCGetTimedMemberStateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetTimedMemberStateMessageTask(cm, node, con));
        factories.put(ServerMCMatchMCConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new MatchClientFilteringConfigMessageTask(cm, node, con));
        factories.put(ServerMCApplyMCConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ApplyClientFilteringConfigMessageTask(cm, node, con));
        factories.put(ServerMCGetClusterMetadataCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetClusterMetadataMessageTask(cm, node, con));
        factories.put(ServerMCShutdownClusterCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ShutdownClusterMessageTask(cm, node, con));
        factories.put(ServerMCChangeClusterVersionCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ChangeClusterVersionMessageTask(cm, node, con));
        factories.put(ServerMCRunScriptCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RunScriptMessageTask(cm, node, con));
        factories.put(ServerMCRunConsoleCommandCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RunConsoleCommandMessageTask(cm, node, con));
        factories.put(com.hazelcast.client.impl.protocol.codec.ServerMCChangeWanReplicationStateCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ChangeWanReplicationStateMessageTask(cm, node, con));
        factories.put(com.hazelcast.client.impl.protocol.codec.ServerMCClearWanQueuesCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ClearWanQueuesMessageTask(cm, node, con));
        factories.put(com.hazelcast.client.impl.protocol.codec.ServerMCAddWanBatchPublisherConfigCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new AddWanBatchPublisherConfigMessageTask(cm, node, con));
        factories.put(com.hazelcast.client.impl.protocol.codec.ServerMCWanSyncMapCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new WanSyncMapMessageTask(cm, node, con));
        factories.put(com.hazelcast.client.impl.protocol.codec.ServerMCCheckWanConsistencyCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new CheckWanConsistencyMessageTask(cm, node, con));
        factories.put(ServerMCPollMCEventsCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PollMCEventsMessageTask(cm, node, con));
        factories.put(ServerMCGetCPMembersCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new GetCPMembersMessageTask(cm, node, con));
        factories.put(ServerMCPromoteToCPMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new PromoteToCPMemberMessageTask(cm, node, con));
        factories.put(ServerMCRemoveCPMemberCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new RemoveCPMemberMessageTask(cm, node, con));
        factories.put(ServerMCResetCPSubsystemCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new ResetCPSubsystemMessageTask(cm, node, con));
        factories.put(ServerMCTriggerPartialStartCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new HotRestartTriggerPartialStartMessageTask(cm, node, con));
        factories.put(ServerMCTriggerForceStartCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new HotRestartTriggerForceStartMessageTask(cm, node, con));
        factories.put(ServerMCTriggerHotRestartBackupCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new HotRestartTriggerBackupMessageTask(cm, node, con));
        factories.put(ServerMCInterruptHotRestartBackupCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new HotRestartInterruptBackupMessageTask(cm, node, con));
    }

    private void initializeSqlTaskFactories() {
        factories.put(ServerSqlExecuteCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SqlExecuteMessageTask(cm, node, con));
        factories.put(ServerSqlFetchCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SqlFetchMessageTask(cm, node, con));
        factories.put(ServerSqlCloseCodec.REQUEST_MESSAGE_TYPE,
                (cm, con) -> new SqlCloseMessageTask(cm, node, con));
    }

    @SuppressFBWarnings({"MS_EXPOSE_REP", "EI_EXPOSE_REP"})
    @Override
    public Int2ObjectHashMap<MessageTaskFactory> getFactories() {
        return factories;
    }
}
