package com.navercorp.pinpoint.common.server.scatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OneByteFuzzyRowKeyFactoryTest {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Test
    public void test() {
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        logger.debug("{}", fuzzyKeyFactory);
    }

    @Test
    public void test_100() {
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        Assertions.assertEquals(0, fuzzyKeyFactory.getKey(100).byteValue());
    }

    @Test
    public void test_200() {
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        Assertions.assertEquals(1, fuzzyKeyFactory.getKey(199).byteValue());
    }

    @Test
    public void test_500() {
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        Assertions.assertEquals(3, fuzzyKeyFactory.getKey(500).byteValue());
    }

    @Test
    public void test_max() {
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        long[] slot = fuzzyKeyFactory.slot;
        long max = slot[slot.length - 1] + 1;
        Assertions.assertEquals(slot.length, fuzzyKeyFactory.getKey(max).byteValue());
    }

    @Test
    public void test_range1() {
//               0    1,    2,  3,   4,    5,    6,     7,     8,    9,      10,     11,    12
//        slot=[100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400, 204800, 409600]
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        List<Byte> rangeKey = fuzzyKeyFactory.getRangeKey(101, 500);

        List<Byte> expected = List.of((byte) 1, (byte) 2, (byte) 3);
        Assertions.assertEquals(expected, rangeKey);

    }

    @Test
    public void test_range2() {
//               0    1,    2,  3,   4,    5,    6,     7,     8,    9,      10,     11,    12     13
//        slot=[100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400, 204800, 409600]
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        List<Byte> rangeKey = fuzzyKeyFactory.getRangeKey(409601, 409602);
        List<Byte> expected = List.of((byte) 13);
        Assertions.assertEquals(expected, rangeKey);
    }

    @Test
    public void test_range3() {
//               0    1,    2,  3,   4,    5,    6,     7,     8,    9,      10,     11,    12     13
//        slot=[100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400, 204800, 409600]
        OneByteFuzzyRowKeyFactory fuzzyKeyFactory = new OneByteFuzzyRowKeyFactory();
        List<Byte> rangeKey = fuzzyKeyFactory.getRangeKey(1, 10);
        List<Byte> expected = List.of((byte) 0);
        Assertions.assertEquals(expected, rangeKey);
    }

}