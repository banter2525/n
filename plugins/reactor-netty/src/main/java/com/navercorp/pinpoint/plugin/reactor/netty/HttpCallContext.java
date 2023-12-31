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

package com.navercorp.pinpoint.plugin.reactor.netty;

public class HttpCallContext {
    private long readBeginTime;
    private long readEndTime;
    private boolean readFail;

    private long writeBeginTime;
    private long writeEndTime;
    private boolean writeFail;

    public void setReadBeginTime(long readBeginTime) {
        this.readBeginTime = readBeginTime;
    }

    public void setReadEndTime(long readEndTime) {
        this.readEndTime = readEndTime;
    }

    public boolean isReadFail() {
        return readFail;
    }

    public void setReadFail(boolean readFail) {
        this.readFail = readFail;
    }

    public void setWriteBeginTime(long writeBeginTime) {
        this.writeBeginTime = writeBeginTime;
    }

    public void setWriteEndTime(long writeEndTime) {
        this.writeEndTime = writeEndTime;
    }

    public boolean isWriteFail() {
        return writeFail;
    }

    public void setWriteFail(boolean writeFail) {
        this.writeFail = writeFail;
    }

    public long getWriteElapsedTime() {
        if (writeBeginTime == 0) {
            return 0;
        }
        long result = writeEndTime - writeBeginTime;
        return result > 0 ? result : 0;
    }

    public long getReadElapsedTime() {
        if (readBeginTime == 0) {
            return 0;
        }
        long result = readEndTime - readBeginTime;
        return result > 0 ? result : 0;
    }

    @Override
    public String toString() {
        return "HttpCallContext{" +
                "readBeginTime=" + readBeginTime +
                ", readEndTime=" + readEndTime +
                ", readFail=" + readFail +
                ", writeBeginTime=" + writeBeginTime +
                ", writeEndTime=" + writeEndTime +
                ", writeFail=" + writeFail +
                '}';
    }
}
