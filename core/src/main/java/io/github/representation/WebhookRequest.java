package io.github.representation;

import io.github.representation.enums.PiWebhookEvent;

import java.util.List;

public class WebhookRequest {
    private String callbackUrl;
    private String alias;
    private List<PiWebhookEvent> events;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<PiWebhookEvent> getEvents() {
        return events;
    }

    public void setEvents(List<PiWebhookEvent> events) {
        this.events = events;
    }
}
