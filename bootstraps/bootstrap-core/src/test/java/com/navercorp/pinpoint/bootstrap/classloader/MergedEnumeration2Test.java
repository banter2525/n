/*
 * Copyright 2018 NAVER Corp.
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

package com.navercorp.pinpoint.bootstrap.classloader;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Woonduk Kang(emeroad)
 */
public class MergedEnumeration2Test {

    @Test
    public void hasMoreElements() {
        Enumeration<String> enumeration0 = newEnumeration("a", "b", "c");
        Enumeration<String> enumeration1 = newEnumeration("1", "2", "3");

        Enumeration<String> mergedEnumeration = new MergedEnumeration2<>(enumeration0, enumeration1);

        List<String> list = Collections.list(mergedEnumeration);

        assertThat(list)
                .containsExactly("a", "b", "c", "1", "2", "3");
    }

    private <T> Enumeration<T> newEnumeration(T... t) {
        return Collections.enumeration(Arrays.asList(t));
    }

    @Test
    public void null0() {
        Enumeration<String> enumeration0 = null;
        Enumeration<String> enumeration1 = newEnumeration("1", "2", "3");

        Enumeration<String> mergedEnumeration = new MergedEnumeration2<>(enumeration0, enumeration1);

        List<String> list = Collections.list(mergedEnumeration);

        assertThat(list).containsExactly("1", "2", "3");
    }


    @Test
    public void null1() {
        Enumeration<String> enumeration0 = newEnumeration("a", "b", "c");
        Enumeration<String> enumeration1 = null;

        Enumeration<String> mergedEnumeration = new MergedEnumeration2<>(enumeration0, enumeration1);

        List<String> list = Collections.list(mergedEnumeration);

        assertThat(list).containsExactly("a", "b", "c");
    }

}