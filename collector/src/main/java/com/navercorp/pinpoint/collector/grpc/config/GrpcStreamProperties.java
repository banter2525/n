/*
 * Copyright 2021 NAVER Corp.
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

package com.navercorp.pinpoint.collector.grpc.config;

public class GrpcStreamProperties {
    private final int schedulerThreadSize;
    private final int callInitRequestCount;
    private final int schedulerPeriodMillis;
    private final int schedulerRecoveryMessageCount;

    private final long idleTimeout;
    private final long throttledLoggerRatio;

    GrpcStreamProperties(int schedulerThreadSize, int callInitRequestCount,
                         int schedulerPeriodMillis, int schedulerRecoveryMessageCount,
                         long idleTimeout, long throttledLoggerRatio) {
        this.schedulerThreadSize = schedulerThreadSize;
        this.callInitRequestCount = callInitRequestCount;
        this.schedulerPeriodMillis = schedulerPeriodMillis;
        this.schedulerRecoveryMessageCount = schedulerRecoveryMessageCount;
        this.idleTimeout = idleTimeout;
        this.throttledLoggerRatio = throttledLoggerRatio;
    }

    public int getSchedulerThreadSize() {
        return schedulerThreadSize;
    }

    public int getCallInitRequestCount() {
        return callInitRequestCount;
    }

    public int getSchedulerPeriodMillis() {
        return schedulerPeriodMillis;
    }

    public int getSchedulerRecoveryMessageCount() {
        return schedulerRecoveryMessageCount;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public long getThrottledLoggerRatio() {
        return throttledLoggerRatio;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int schedulerThreadSize = 1;
        private int callInitRequestCount = 1000;
        private int schedulerPeriodMillis =  64;
        private int schedulerRecoveryMessageCount = 10;
        private long idleTimeout = -1;
        private long throttledLoggerRatio = 1;


        public int getSchedulerThreadSize() {
            return schedulerThreadSize;
        }

        public void setSchedulerThreadSize(int schedulerThreadSize) {
            this.schedulerThreadSize = schedulerThreadSize;
        }

        public int getCallInitRequestCount() {
            return callInitRequestCount;
        }

        public void setCallInitRequestCount(int callInitRequestCount) {
            this.callInitRequestCount = callInitRequestCount;
        }

        public int getSchedulerPeriodMillis() {
            return schedulerPeriodMillis;
        }

        public void setSchedulerPeriodMillis(int schedulerPeriodMillis) {
            this.schedulerPeriodMillis = schedulerPeriodMillis;
        }

        public int getSchedulerRecoveryMessageCount() {
            return schedulerRecoveryMessageCount;
        }

        public void setSchedulerRecoveryMessageCount(int schedulerRecoveryMessageCount) {
            this.schedulerRecoveryMessageCount = schedulerRecoveryMessageCount;
        }

        public long getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(long idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public long getThrottledLoggerRatio() {
            return throttledLoggerRatio;
        }

        public void setThrottledLoggerRatio(long throttledLoggerRatio) {
            this.throttledLoggerRatio = throttledLoggerRatio;
        }

        public GrpcStreamProperties build() {
            return new GrpcStreamProperties(this.schedulerThreadSize, this.callInitRequestCount,
                    this.schedulerPeriodMillis, this.schedulerRecoveryMessageCount, this.idleTimeout, this.throttledLoggerRatio);
        }
    }

    @Override
    public String toString() {
        return "GrpcStreamProperties{" +
                "schedulerThreadSize=" + schedulerThreadSize +
                ", callInitRequestCount=" + callInitRequestCount +
                ", schedulerPeriodMillis=" + schedulerPeriodMillis +
                ", schedulerRecoveryMessageCount=" + schedulerRecoveryMessageCount +
                ", idleTimeout=" + idleTimeout +
                ", throttledLoggerRatio=" + throttledLoggerRatio +
                '}';
    }
}
