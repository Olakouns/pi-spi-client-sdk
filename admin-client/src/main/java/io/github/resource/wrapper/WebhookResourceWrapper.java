package io.github.resource.wrapper;

import io.github.representation.*;
import io.github.resource.WebhookResource;

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
