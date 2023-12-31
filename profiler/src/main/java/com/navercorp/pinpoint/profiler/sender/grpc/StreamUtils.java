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

package com.navercorp.pinpoint.profiler.sender.grpc;

import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.Logger;


/**
 * @author Woonduk Kang(emeroad)
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    public static void close(final StreamObserver<?> streamObserver, Logger logger) {
        if (streamObserver != null) {
            try {
                streamObserver.onCompleted();
            } catch (Throwable th) {
                if (logger != null) {
                    logger.info("StreamObserver.onCompleted error", th);
                }
            }
        }
    }
}
