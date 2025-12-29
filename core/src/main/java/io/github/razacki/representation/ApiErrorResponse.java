package io.github.razacki.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiErrorResponse {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

    @JsonProperty("invalid-params")
    private List<InvalidParam> invalidParams;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public List<InvalidParam> getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(List<InvalidParam> invalidParams) {
        this.invalidParams = invalidParams;
    }

    public static class InvalidParam {
        private String name;
        private String reason;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
