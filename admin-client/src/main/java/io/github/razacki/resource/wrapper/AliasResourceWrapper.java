package io.github.razacki.resource.wrapper;

import io.github.razacki.representation.AliasRepresentation;
import io.github.razacki.representation.CreateAliasRequest;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.enums.AliasType;
import io.github.razacki.resource.AliasResource;

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
