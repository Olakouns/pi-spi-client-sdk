package io.github.resource.wrapper;

import io.github.representation.AliasRepresentation;
import io.github.representation.CreateAliasRequest;
import io.github.resource.AliasResource;

import javax.ws.rs.client.WebTarget;

public class AliasResourceWrapper extends BaseWrapper<AliasRepresentation, AliasResource> {
    public AliasResourceWrapper(AliasResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public AliasRepresentation create(CreateAliasRequest createAliasRequest) {
        return proxy.create(createAliasRequest);
    }

    public void delete(String cle) {
        proxy.delete(cle);
    }
}
