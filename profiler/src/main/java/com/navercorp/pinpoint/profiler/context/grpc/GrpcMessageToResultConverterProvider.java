/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.context.grpc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.navercorp.pinpoint.common.profiler.message.MessageConverter;
import com.navercorp.pinpoint.common.profiler.message.ResultResponse;

/**
 * @author jaehong.kim
 */
public class GrpcMessageToResultConverterProvider implements Provider<MessageConverter<Object, ResultResponse>> {
    @Inject
    public GrpcMessageToResultConverterProvider() {
    }

    @Override
    public MessageConverter<Object, ResultResponse> get() {
        return new GrpcMessageToResultConverter();
    }
}
