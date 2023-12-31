/*
 * Copyright 2019 NAVER Corp.
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

package com.navercorp.pinpoint.common.buffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author emeroad
 */
public class OffsetAutomaticBufferTest {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Test
    public void testGetBuffer() {
        final int bufferSize = 10;
        byte[] byteArray = new byte[bufferSize];
        Buffer buffer = new OffsetAutomaticBuffer(byteArray, 2, byteArray.length - 2);

        final int putValue = 10;
        buffer.putInt(putValue);
        byte[] intBuffer = buffer.getBuffer();
        Assertions.assertNotSame(intBuffer, byteArray, "deepcopy");
        assertThat(intBuffer).hasSize(4);

        Buffer read = new FixedBuffer(intBuffer);
        int value = read.readInt();
        Assertions.assertEquals(putValue, value);
    }

    @Test
    public void testGetBuffer_shallowcopy() {
        final int bufferSize = 4;
        byte[] byteArray = new byte[bufferSize];
        Buffer buffer = new OffsetAutomaticBuffer(byteArray);

        final int putValue = 10;
        buffer.putInt(putValue);
        byte[] intBuffer = buffer.getBuffer();
        Assertions.assertSame(intBuffer, byteArray, "shallowcopy");
        assertThat(intBuffer).hasSize(4);

        Buffer read = new FixedBuffer(intBuffer);
        int value = read.readInt();
        Assertions.assertEquals(putValue, value);
    }

    @Test
    public void testCopyBuffer() {
        final int bufferSize = 10;
        byte[] byteArray = new byte[bufferSize];
        Buffer buffer = new OffsetAutomaticBuffer(byteArray, 2, bufferSize - 2);

        final int putValue = 10;
        buffer.putInt(putValue);
        byte[] intBuffer = buffer.copyBuffer();
        Assertions.assertNotSame(intBuffer, byteArray, "deepcopy");
        assertThat(intBuffer).hasSize(4);

        Buffer read = new FixedBuffer(intBuffer);
        int value = read.readInt();
        Assertions.assertEquals(putValue, value);
    }

    @Test
    public void testWrapByteBuffer() {
        final int bufferSize = 10;
        byte[] byteArray = new byte[bufferSize];
        Buffer buffer = new OffsetAutomaticBuffer(byteArray, 2, bufferSize - 2);
        buffer.putInt(1);
        buffer.putInt(2);

        ByteBuffer byteBuffer = buffer.wrapByteBuffer();
        Assertions.assertSame(byteArray, byteBuffer.array(), "shallowcopy");
        Assertions.assertEquals(1, byteBuffer.getInt());
        Assertions.assertEquals(2, byteBuffer.getInt());
    }


    @Test
    public void testCheckExpand() {
        final int bufferSize = 4;
        int startOffset = 2;
        Buffer buffer = new OffsetAutomaticBuffer(new byte[bufferSize], startOffset, bufferSize - startOffset);
        Assertions.assertEquals(buffer.remaining(), 2, "remaining");
        buffer.putInt(1);
        buffer.putInt(2);

        int remaining = buffer.remaining();
        logger.debug("remaining:{}", remaining);

        ByteBuffer byteBuffer = buffer.wrapByteBuffer();
        Assertions.assertEquals(1, byteBuffer.getInt());
        Assertions.assertEquals(2, byteBuffer.getInt());

    }

    @Test
    public void testCheckExpand_test() {
        final int bufferSize = 4;
        Buffer buffer = new OffsetAutomaticBuffer(new byte[bufferSize], 0, bufferSize);
        Assertions.assertEquals(buffer.remaining(), 4, "remaining");
        buffer.putInt(1);
        Assertions.assertEquals(buffer.remaining(), 0, "remaining");
        buffer.putInt(2);

        int remaining = buffer.remaining();
        logger.debug("remaining:{}", remaining);

        ByteBuffer byteBuffer = buffer.wrapByteBuffer();
        Assertions.assertEquals(1, byteBuffer.getInt());
        Assertions.assertEquals(2, byteBuffer.getInt());

    }
}
