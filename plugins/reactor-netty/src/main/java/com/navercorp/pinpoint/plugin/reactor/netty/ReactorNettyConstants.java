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

package com.navercorp.pinpoint.plugin.reactor.netty;

import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeProvider;

/**
 * @author jaehong.kim
 */
public class ReactorNettyConstants {

    public static final int V0_0_0 = 0;
    public static final int V0_7_0 = 1;

    public static final ServiceType REACTOR_NETTY = ServiceTypeProvider.getByName("REACTOR_NETTY");
    public static final ServiceType REACTOR_NETTY_INTERNAL = ServiceTypeProvider.getByName("REACTOR_NETTY_INTERNAL");
    public static final ServiceType REACTOR_NETTY_CLIENT = ServiceTypeProvider.getByName("REACTOR_NETTY_CLIENT");
    public static final ServiceType REACTOR_NETTY_CLIENT_INTERNAL = ServiceTypeProvider.getByName("REACTOR_NETTY_CLIENT_INTERNAL");
}
