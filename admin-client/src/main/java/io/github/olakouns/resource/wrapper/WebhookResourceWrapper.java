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

import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.WebhookRenewRequest;
import io.github.olakouns.representation.WebhookRepresentation;
import io.github.olakouns.representation.WebhookRequest;
import io.github.olakouns.resource.WebhookResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.time.OffsetDateTime;

public class WebhookResourceWrapper extends ListableWrapper<WebhookRepresentation, WebhookResource> {
    private static final String WEBHOOK_ID = "Webhook ID";

    public WebhookResourceWrapper(WebhookResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<WebhookRepresentation>>() {});
    }

    public WebhookRepresentation create(WebhookRequest webhook) {
        validateNotNull(webhook, "WebhookRequest");
        return proxy.create(webhook);
    }

    public WebhookRepresentation findById(String id) {
        validateNotEmpty(id, WEBHOOK_ID);
        return proxy.findById(id);
    }

    public WebhookRepresentation update(String id, WebhookRequest webhook) {
        validateNotEmpty(id, WEBHOOK_ID);
        validateNotNull(webhook, "WebhookRequest");
        return proxy.update(id, webhook);
    }

    public void delete(String id) {
        validateNotEmpty(id, WEBHOOK_ID);
        proxy.delete(id);
    }

    public WebhookRepresentation renewSecret(String id, OffsetDateTime dateExpiration) {
        validateNotEmpty(id, WEBHOOK_ID);
        validateNotNull(dateExpiration, "DateExpiration");
        return proxy.renewSecret(id, new WebhookRenewRequest(dateExpiration));
    }
}
