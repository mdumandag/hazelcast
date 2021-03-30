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

package com.hazelcast.client.impl.proxy.txn;

import com.hazelcast.client.impl.connection.ClientConnection;
import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.spi.ClientTransactionContext;
import com.hazelcast.client.impl.spi.impl.ClientTransactionManagerServiceImpl;
import com.hazelcast.core.ServiceNames;
import com.hazelcast.transaction.TransactionalList;
import com.hazelcast.transaction.TransactionalMap;
import com.hazelcast.transaction.TransactionalMultiMap;
import com.hazelcast.transaction.TransactionalQueue;
import com.hazelcast.transaction.TransactionalSet;
import com.hazelcast.transaction.TransactionException;
import com.hazelcast.transaction.TransactionNotActiveException;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionalObject;
import com.hazelcast.transaction.impl.TransactionBase;
import com.hazelcast.transaction.impl.TransactionalObjectKey;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Provides a context to perform transactional operations: beginning/committing transactions, but also retrieving
 * transactional data-structures like the {@link TransactionalMap}.
 *
 * Provides client instance and client connection proxies that need to be accessed for sending invocations.
 */
public class TransactionContextProxy implements ClientTransactionContext {

    final ClientTransactionManagerServiceImpl transactionManager;
    final HazelcastClientInstanceImpl client;
    final TransactionProxy transaction;
    final ClientConnection connection;

    private final Map<TransactionalObjectKey, TransactionalObject> txnObjectMap =
            new HashMap<TransactionalObjectKey, TransactionalObject>(2);

    public TransactionContextProxy(@Nonnull ClientTransactionManagerServiceImpl transactionManager,
                                   @Nonnull TransactionOptions options) {
        this.transactionManager = transactionManager;
        this.client = transactionManager.getClient();
        try {
            connection = transactionManager.connect();
        } catch (Exception e) {
            throw new TransactionException("Could not obtain a connection!", e);
        }
        this.transaction = new TransactionProxy(client, options, connection);
    }

    @Override
    public UUID getTxnId() {
        return transaction.getTxnId();
    }

    @Override
    public void beginTransaction() {
        transaction.begin();
    }

    @Override
    public void commitTransaction() throws TransactionException {
        transaction.commit();
    }

    @Override
    public void rollbackTransaction() {
        transaction.rollback();
    }

    @Override
    public <K, V> TransactionalMap<K, V> getMap(String name) {
        return getTransactionalObject(ServiceNames.MAP, name);
    }

    @Override
    public <E> TransactionalQueue<E> getQueue(String name) {
        return getTransactionalObject(ServiceNames.QUEUE, name);
    }

    @Override
    public <K, V> TransactionalMultiMap<K, V> getMultiMap(String name) {
        return getTransactionalObject(ServiceNames.MULTI_MAP, name);
    }

    @Override
    public <E> TransactionalList<E> getList(String name) {
        return getTransactionalObject(ServiceNames.LIST, name);
    }

    @Override
    public <E> TransactionalSet<E> getSet(String name) {
        return getTransactionalObject(ServiceNames.SET, name);
    }

    @Override
    public <T extends TransactionalObject> T getTransactionalObject(String serviceName, String name) {
        if (transaction.getState() != TransactionBase.State.ACTIVE) {
            throw new TransactionNotActiveException("No transaction is found while accessing "
                    + "transactional object -> " + serviceName + "[" + name + "]!");
        }
        TransactionalObjectKey key = new TransactionalObjectKey(serviceName, name);
        TransactionalObject obj = txnObjectMap.get(key);
        if (obj == null) {
            if (serviceName.equals(ServiceNames.QUEUE)) {
                obj = new ClientTxnQueueProxy(name, this);
            } else if (serviceName.equals(ServiceNames.MAP)) {
                obj = new ClientTxnMapProxy(name, this);
            } else if (serviceName.equals(ServiceNames.MULTI_MAP)) {
                obj = new ClientTxnMultiMapProxy(name, this);
            } else if (serviceName.equals(ServiceNames.LIST)) {
                obj = new ClientTxnListProxy(name, this);
            } else if (serviceName.equals(ServiceNames.SET)) {
                obj = new ClientTxnSetProxy(name, this);
            }

            if (obj == null) {
                throw new IllegalArgumentException("Service[" + serviceName + "] is not transactional!");
            }
            txnObjectMap.put(key, obj);
        }
        return (T) obj;
    }

    @Override
    public ClientConnection getConnection() {
        return connection;
    }

    @Override
    public HazelcastClientInstanceImpl getClient() {
        return client;
    }
}
