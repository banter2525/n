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
public class JoinTransactionBoTest {

    @Test
    public void joinTransactionBoListTest() {
        List<JoinTransactionBo> joinTransactionBoList = List.of(
                new JoinTransactionBo("agent1", 5000, 150, 12, "agent1", 230, "agent1", 1496988667231L),
                new JoinTransactionBo("agent2", 5000, 110, 40, "agent2", 240, "agent2", 1496988667231L),
                new JoinTransactionBo("agent3", 5000, 120, 50, "agent3", 130, "agent3", 1496988667231L),
                new JoinTransactionBo("agent4", 5000, 130, 60, "agent4", 630, "agent4", 1496988667231L),
                new JoinTransactionBo("agent5", 5000, 140, 11, "agent5", 230, "agent5", 1496988667231L)
        );

        JoinTransactionBo joinTransactionBo = JoinTransactionBo.joinTransactionBoList(joinTransactionBoList, 1496988667231L);
        assertEquals("agent1", joinTransactionBo.getId());
        assertEquals(1496988667231L, joinTransactionBo.getTimestamp());
        assertEquals(5000, joinTransactionBo.getCollectInterval());
        assertEquals(new JoinLongFieldBo(130L, 11L, "agent5", 630L, "agent4"), joinTransactionBo.getTotalCountJoinValue());
    }

    @Test
    public void joinTransactionBoList2Test() {
        JoinTransactionBo joinTransactionBo = JoinTransactionBo.joinTransactionBoList(List.of(), 1496988667231L);
        assertEquals(joinTransactionBo, JoinTransactionBo.EMPTY_JOIN_TRANSACTION_BO);
    }
}