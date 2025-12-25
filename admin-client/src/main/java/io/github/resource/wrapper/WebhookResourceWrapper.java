package io.github.resource.wrapper;

import io.github.representation.WebhookRenewRequest;
import io.github.representation.WebhookRepresentation;
import io.github.representation.WebhookRequest;
import io.github.resource.WebhookResource;

import javax.ws.rs.client.WebTarget;

public class WebhookResourceWrapper extends BaseWrapper<WebhookRepresentation, WebhookResource> {

    private static final String WEBHOOK_ID = "Webhook ID";

    public WebhookResourceWrapper(WebhookResource proxy, WebTarget target) {
        super(proxy, target);
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

    public WebhookRepresentation renewSecret(String id, WebhookRenewRequest renewRequest) {
        validateNotEmpty(id, WEBHOOK_ID);
        validateNotNull(renewRequest, "WebhookRenewRequest");
        return proxy.renewSecret(id, renewRequest);
    }
}
