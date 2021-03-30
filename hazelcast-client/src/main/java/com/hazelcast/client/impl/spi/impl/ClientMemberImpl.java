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

package com.hazelcast.client.impl.spi.impl;

import com.hazelcast.cluster.Address;
import com.hazelcast.cluster.Member;
import com.hazelcast.cluster.impl.AbstractMember;
import com.hazelcast.instance.EndpointQualifier;
import com.hazelcast.internal.util.Preconditions;
import com.hazelcast.logging.ILogger;
import com.hazelcast.version.MemberVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hazelcast.instance.EndpointQualifier.MEMBER;

public final class ClientMemberImpl extends AbstractMember implements Member {

    private final ILogger logger;

    public ClientMemberImpl(Map<EndpointQualifier, Address> addresses, Address address, MemberVersion version,
                             UUID uuid, Map<String, String> attributes, boolean liteMember,
                             ILogger logger) {
        super(addresses, address, version, uuid, attributes, liteMember);
        this.logger = logger;
    }

    @Override
    protected ILogger getLogger() {
        return logger;
    }

    @Override
    public boolean localMember() {
        return false;
    }

    @Override
    public String getAttribute(String key) {
        return attributes.get(key);
    }


    private static Map<EndpointQualifier, Address> newHashMap(EndpointQualifier member, Address address) {
        Map<EndpointQualifier, Address> result = new HashMap<>();
        result.put(member, address);
        return result;
    }

    public static class Builder {
        private Address address;
        private Map<EndpointQualifier, Address> addressMap;
        private Map<String, String> attributes;
        private UUID uuid;
        private boolean liteMember;
        private MemberVersion version;
        private ILogger logger;

        public Builder(Address address) {
            Preconditions.isNotNull(address, "address");
            this.address = address;
        }

        public Builder(Map<EndpointQualifier, Address> addresses) {
            Preconditions.isNotNull(addresses, "addresses");
            Preconditions.isNotNull(addresses.get(MEMBER), "addresses.get(MEMBER)");
            this.addressMap = addresses;
        }

        public Builder address(Address address) {
            this.address = Preconditions.isNotNull(address, "address");
            return this;
        }

        public Builder version(MemberVersion memberVersion) {
            this.version = memberVersion;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder logger(ILogger logger) {
            this.logger = logger;
            return this;
        }

        public Builder liteMember(boolean liteMember) {
            this.liteMember = liteMember;
            return this;
        }

        public ClientMemberImpl build() {
            if (addressMap == null) {
                addressMap = newHashMap(MEMBER, address);
            }
            if (address == null) {
                address = addressMap.get(MEMBER);
            }
            return new ClientMemberImpl(addressMap, address, version, uuid,
                    attributes, liteMember, logger);
        }
    }
}
