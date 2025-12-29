package io.github.razacki.representation;

import io.github.razacki.representation.enums.AliasType;

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
