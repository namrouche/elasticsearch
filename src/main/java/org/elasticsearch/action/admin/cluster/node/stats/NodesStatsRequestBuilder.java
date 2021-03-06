/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.admin.cluster.node.stats;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.nodes.NodesOperationRequestBuilder;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.internal.InternalClusterAdminClient;

/**
 *
 */
public class NodesStatsRequestBuilder extends NodesOperationRequestBuilder<NodesStatsRequest, NodesStatsResponse, NodesStatsRequestBuilder> {

    public NodesStatsRequestBuilder(ClusterAdminClient clusterClient) {
        super((InternalClusterAdminClient) clusterClient, new NodesStatsRequest());
    }

    /**
     * Sets all the request flags.
     */
    public NodesStatsRequestBuilder all() {
        request.all();
        return this;
    }

    /**
     * Clears all stats flags.
     */
    public NodesStatsRequestBuilder clear() {
        request.clear();
        return this;
    }

    /**
     * Should the node indices stats be returned.
     */
    public NodesStatsRequestBuilder setIndices(boolean indices) {
        request.setIndices(indices);
        return this;
    }

    /**
     * Should the node OS stats be returned.
     */
    public NodesStatsRequestBuilder setOs(boolean os) {
        request.setOs(os);
        return this;
    }

    /**
     * Should the node OS stats be returned.
     */
    public NodesStatsRequestBuilder setProcess(boolean process) {
        request.setProcess(process);
        return this;
    }

    /**
     * Should the node JVM stats be returned.
     */
    public NodesStatsRequestBuilder setJvm(boolean jvm) {
        request.setJvm(jvm);
        return this;
    }

    /**
     * Should the node thread pool stats be returned.
     */
    public NodesStatsRequestBuilder setThreadPool(boolean threadPool) {
        request.setThreadPool(threadPool);
        return this;
    }

    /**
     * Should the node Network stats be returned.
     */
    public NodesStatsRequestBuilder setNetwork(boolean network) {
        request.setNetwork(network);
        return this;
    }

    /**
     * Should the node file system stats be returned.
     */
    public NodesStatsRequestBuilder setFs(boolean fs) {
        request.setFs(fs);
        return this;
    }

    /**
     * Should the node Transport stats be returned.
     */
    public NodesStatsRequestBuilder setTransport(boolean transport) {
        request.setTransport(transport);
        return this;
    }

    /**
     * Should the node HTTP stats be returned.
     */
    public NodesStatsRequestBuilder setHttp(boolean http) {
        request.setHttp(http);
        return this;
    }

    @Override
    protected void doExecute(ActionListener<NodesStatsResponse> listener) {
        ((ClusterAdminClient) client).nodesStats(request, listener);
    }
}