/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.batch;

import com.navercorp.pinpoint.batch.common.DefaultDivider;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author minwoo.jung
 */
public class DefaultDividerTest {

    @Test
    public void devide() {
        DefaultDivider defaultDivider = new DefaultDivider();
        Map<String, ExecutionContext> map = defaultDivider.divide("test_partition", "test");
        assertThat(map).hasSize(1);
    }

}