package io.github.olakouns.representation;

import io.github.olakouns.representation.enums.AliasType;

public class CreateAliasRequest {
    private AliasType type;


    public CreateAliasRequest() {
    }

    public CreateAliasRequest(AliasType type) {
        this.type = type;
    }

    public AliasType getType() {
        return type;
    }

    public void setType(AliasType type) {
        this.type = type;
    }
}
