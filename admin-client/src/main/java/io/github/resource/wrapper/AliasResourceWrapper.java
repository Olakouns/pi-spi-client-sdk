package io.github.resource.wrapper;

import io.github.representation.AliasRepresentation;
import io.github.representation.CreateAliasRequest;
import io.github.representation.enums.AliasType;
import io.github.resource.AliasResource;

import javax.ws.rs.client.WebTarget;

public class AliasResourceWrapper extends ListableWrapper<AliasRepresentation, AliasResource> {
    public AliasResourceWrapper(AliasResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public AliasRepresentation create(AliasType type) {
        validateNotNull(type, "AliasType");
        return proxy.create(new CreateAliasRequest(type));
    }

    public void delete(String cle) {
        validateNotEmpty(cle, "Alias cle");
        proxy.delete(cle);
    }
}
