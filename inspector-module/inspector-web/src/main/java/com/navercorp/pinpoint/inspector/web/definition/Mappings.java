/*
 * Copyright 2023 NAVER Corp.
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

package com.navercorp.pinpoint.inspector.web.definition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * @author minwoo.jung
 */
public class Mappings {
    private final List<MetricDefinition> mappings;

    @JsonCreator
    public Mappings(@JsonProperty("mappings") List<MetricDefinition> mappings) {
        this.mappings = Objects.requireNonNull(mappings, "mappings");
    }

    public List<MetricDefinition> getMappings() {
        return mappings;
    }

    @Override
    public String toString() {
        return "Mappings{" +
                "groups=" + mappings +
                '}';
    }
}
