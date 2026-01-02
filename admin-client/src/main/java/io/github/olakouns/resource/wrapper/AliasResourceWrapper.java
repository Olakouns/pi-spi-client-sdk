package io.github.olakouns.resource.wrapper;

import io.github.olakouns.representation.AliasRepresentation;
import io.github.olakouns.representation.CreateAliasRequest;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.enums.AliasType;
import io.github.olakouns.resource.AliasResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class AliasResourceWrapper extends ListableWrapper<AliasRepresentation, AliasResource> {
    public AliasResourceWrapper(AliasResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<AliasRepresentation>>() {});
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
