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

package com.navercorp.pinpoint.web.cluster;

import com.navercorp.pinpoint.thrift.dto.command.TRouteResult;
import org.apache.thrift.TBase;

import java.util.Objects;

/**
 * @author Taejin Koo
 */
public class DefaultPinpointRouteResponse implements PinpointRouteResponse {

    private final TRouteResult routeResult;
    private final TBase<?, ?> response;
    private final String message;

    public DefaultPinpointRouteResponse(TRouteResult routeResult, TBase<?, ?> response, String message) {
        this.routeResult = Objects.requireNonNull(routeResult, "routeResult");
        this.response = response;
        this.message = message;
    }

    public DefaultPinpointRouteResponse(TRouteResult routeResult) {
        this(routeResult, null, null);
    }

    @Override
    public TRouteResult getRouteResult() {
        return routeResult;
    }

    @Override
    public TBase<?, ?> getResponse() {
        return response;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public <R extends TBase<?, ?>> R getResponse(Class<R> clazz) {
        TBase<?, ?> response = getResponse();

        if (clazz.isInstance(response)) {
            return (R) response;
        }

        throw new ClassCastException("Not expected " + clazz + " type.");
    }

    @Override
    public <R extends TBase<?, ?>> R getResponse(Class<R> clazz, R defaultValue) {
        TBase<?, ?> response = getResponse();

        if (clazz.isInstance(response)) {
            return (R) response;
        }

        return defaultValue;
    }





}
