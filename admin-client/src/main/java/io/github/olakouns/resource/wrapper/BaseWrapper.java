/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
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
package io.github.olakouns.resource.wrapper;

import io.github.olakouns.exception.PiSpiException;

import javax.ws.rs.client.WebTarget;

public abstract class BaseWrapper<R> {
    protected final R proxy;
    protected final WebTarget target;

    protected BaseWrapper(R proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public R proxy() {
        return proxy;
    }

    protected void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new PiSpiException(paramName + " cannot be null", new IllegalArgumentException(paramName));
        }
    }

    protected void validateNotEmpty(String str, String paramName) {
        if (str == null || str.trim().isEmpty()) {
            throw new PiSpiException(paramName + " cannot be null or empty");
        }
    }
}