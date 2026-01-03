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

import io.github.olakouns.filter.ListQueryBuilder;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.resource.PageableResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public abstract class ListableWrapper<T, R extends PageableResource<T>> extends BaseWrapper<R> {
    private final GenericType<PagedResponse<T>> responseType;

    protected ListableWrapper(R proxy, WebTarget target, GenericType<PagedResponse<T>> responseType) {
        super(proxy, target);
        this.responseType = responseType;
    }

    public PagedResponse<T> list(String page, int size) {
        return proxy.list(page, size);
    }

    public ListQueryBuilder<T> query() {
        return new ListQueryBuilder<>(target, responseType);
    }
}
