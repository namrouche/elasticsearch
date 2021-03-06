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

package org.elasticsearch.search.facet;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.common.lucene.docset.AndDocIdSet;
import org.elasticsearch.common.lucene.docset.ContextDocIdSet;
import org.elasticsearch.common.lucene.search.XCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A facet processor ends up actually executing the relevant facet for a specific
 * search request.
 * <p/>
 * The facet executor requires at least one of the {@link #collector()} or {@link #post()} methods to be
 * implemented.
 */
public abstract class FacetExecutor {

    /**
     * A post class extends this class to implement post hits processing.
     */
    public static abstract class Post {

        public abstract void executePost(List<ContextDocIdSet> docSets) throws IOException;

        public static class Filtered extends Post {

            private final Post post;
            private final Filter filter;

            public Filtered(Post post, Filter filter) {
                this.post = post;
                this.filter = filter;
            }

            @Override
            public void executePost(List<ContextDocIdSet> docSets) throws IOException {
                List<ContextDocIdSet> filteredEntries = new ArrayList<ContextDocIdSet>(docSets.size());
                for (int i = 0; i < docSets.size(); i++) {
                    ContextDocIdSet entry = docSets.get(i);
                    DocIdSet filteredSet = filter.getDocIdSet(entry.context, null);
                    filteredEntries.add(new ContextDocIdSet(
                            entry.context,
                            // TODO: can we be smart here, maybe AndDocIdSet is not always fastest?
                            new AndDocIdSet(new DocIdSet[]{entry.docSet, filteredSet})
                    ));
                }
                post.executePost(filteredEntries);
            }
        }
    }

    /**
     * Simple extension to {@link XCollector} that implements methods that are typically
     * not needed when doing collector based faceting.
     */
    public static abstract class Collector extends XCollector {

        @Override
        public void setScorer(Scorer scorer) throws IOException {
        }

        @Override
        public boolean acceptsDocsOutOfOrder() {
            return true;
        }

        @Override
        public abstract void postCollection();
    }

    /**
     * The mode of the execution.
     */
    public static enum Mode {
        /**
         * Collector mode, maps to {@link #collector()}.
         */
        COLLECTOR,
        /**
         * Post mode, maps to {@link #post()}.
         */
        POST
    }

    /**
     * Builds the facet.
     */
    public abstract InternalFacet buildFacet(String facetName);

    /**
     * A collector based facet implementation, collection the facet as hits match.
     */
    public abstract Collector collector();

    /**
     * A post based facet that executes the facet using the aggregated docs relevant.
     */
    public abstract Post post();
}
