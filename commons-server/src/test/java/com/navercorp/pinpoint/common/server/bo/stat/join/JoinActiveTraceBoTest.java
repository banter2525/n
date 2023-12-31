/*
 * Copyright 2017 NAVER Corp.
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

package com.navercorp.pinpoint.common.server.bo.stat.join;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author minwoo.jung
 */
public class JoinActiveTraceBoTest {

    @Test
    public void joinActiveTraceBoTest() {
        List<JoinActiveTraceBo> joinActiveTraceBoList = List.of(
                new JoinActiveTraceBo("agent1", 1, (short) 2, 40, 10, "agent1", 70, "agent1", 1496988667231L),
                new JoinActiveTraceBo("agent2", 1, (short) 2, 10, 9, "agent2", 71, "agent2", 1496988667231L),
                new JoinActiveTraceBo("agent3", 1, (short) 2, 20, 14, "agent3", 88, "agent3", 1496988667231L),
                new JoinActiveTraceBo("agent4", 1, (short) 2, 30, 22, "agent4", 10, "agent4", 1496988667231L),
                new JoinActiveTraceBo("agent5", 1, (short) 2, 50, 17, "agent5", 99, "agent5", 1496988667231L)
        );

        JoinActiveTraceBo joinActiveTraceBo = JoinActiveTraceBo.joinActiveTraceBoList(joinActiveTraceBoList, 1496988667231L);
        assertEquals("agent1", joinActiveTraceBo.getId());
        assertEquals(1496988667231L, joinActiveTraceBo.getTimestamp());
        assertEquals(1, joinActiveTraceBo.getHistogramSchemaType());
        assertEquals(2, joinActiveTraceBo.getVersion());
        assertEquals(new JoinIntFieldBo(30, 9, "agent2", 99, "agent5"), joinActiveTraceBo.getTotalCountJoinValue());
    }

    @Test
    public void joinActiveTraceBo2Test() {
        JoinActiveTraceBo joinActiveTraceBo = JoinActiveTraceBo.joinActiveTraceBoList(List.of(), 1496988667231L);
        assertEquals(joinActiveTraceBo, JoinActiveTraceBo.EMPTY_JOIN_ACTIVE_TRACE_BO);
    }
}