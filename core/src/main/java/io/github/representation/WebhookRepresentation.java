package io.github.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.representation.enums.PiWebhookEvent;
import io.github.util.DataUtil;

import java.time.OffsetDateTime;
import java.util.List;

public class WebhookRepresentation {
    private String id;
    private String callbackUrl;
    private String secret;
    private String alias;
    private List<PiWebhookEvent> events;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateCreation;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateModification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<PiWebhookEvent> getEvents() {
        return events;
    }

    public void setEvents(List<PiWebhookEvent> events) {
        this.events = events;
    }

    public OffsetDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(OffsetDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public OffsetDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(OffsetDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
