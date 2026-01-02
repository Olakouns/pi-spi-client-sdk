package io.github.olakouns.representation;

import io.github.olakouns.representation.enums.PiWebhookEvent;

import java.util.ArrayList;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final WebhookRequest request = new WebhookRequest();

        public Builder callbackUrl(String callbackUrl) {
            request.setCallbackUrl(callbackUrl);
            return this;
        }

        public Builder alias(String alias) {
            request.setAlias(alias);
            return this;
        }


        public Builder events(List<PiWebhookEvent> events) {
            request.setEvents(events);
            return this;
        }


        public Builder addEvent(PiWebhookEvent event) {
            if (request.getEvents() == null) {
                request.setEvents(new ArrayList<>());
            }
            request.getEvents().add(event);
            return this;
        }

        public WebhookRequest build() {
            return request;
        }
    }
}
