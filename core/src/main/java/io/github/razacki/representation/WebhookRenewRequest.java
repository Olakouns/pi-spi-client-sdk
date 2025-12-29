package io.github.razacki.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.razacki.util.DataUtil;

import java.time.OffsetDateTime;

public class WebhookRenewRequest {
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateExpiration;

    public WebhookRenewRequest() {
    }

    public WebhookRenewRequest(OffsetDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public OffsetDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(OffsetDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
}
