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

package com.navercorp.pinpoint.profiler.plugin;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentContext;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.exception.PinpointException;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DynamicTransformCallbackProvider implements TransformCallbackProvider {
    private final String transformCallbackClassName;
    private final Object[] parameters;
    private final Class<?>[] parameterTypes;

    public DynamicTransformCallbackProvider(String transformCallbackClassName) {
        this.transformCallbackClassName = Objects.requireNonNull(transformCallbackClassName, "transformCallbackClassName");
        this.parameters = null;
        this.parameterTypes = null;
    }

    public DynamicTransformCallbackProvider(String transformCallbackClassName, Object[] parameters, Class<?>[] parameterTypes) {
        this.transformCallbackClassName = Objects.requireNonNull(transformCallbackClassName, "transformCallbackClassName");
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public TransformCallback getTransformCallback(InstrumentContext instrumentContext, ClassLoader loader) {
        try {
            final Class<? extends TransformCallback> transformCallbackClass = instrumentContext.injectClass(loader, transformCallbackClassName);
            Constructor<? extends TransformCallback> constructor = transformCallbackClass.getConstructor(parameterTypes);
            return constructor.newInstance(parameters);
        } catch (ReflectiveOperationException e) {
            throw new PinpointException(transformCallbackClassName + " load fail Caused by:" + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "DynamicTransformCallbackProvider{" +
                "transformCallbackClassName='" + transformCallbackClassName + '\'' +
                '}';
    }
}
