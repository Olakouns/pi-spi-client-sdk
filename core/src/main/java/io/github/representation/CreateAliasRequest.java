package io.github.representation;

public class CreateAliasRequest {
    private String type;


    public CreateAliasRequest() {
    }

    public CreateAliasRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
