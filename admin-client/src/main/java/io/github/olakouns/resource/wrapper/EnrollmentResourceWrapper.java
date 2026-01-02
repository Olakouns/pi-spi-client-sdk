package io.github.olakouns.resource.wrapper;

import io.github.olakouns.representation.EnrollmentRepresentation;
import io.github.olakouns.resource.EnrollmentResource;

import javax.ws.rs.client.WebTarget;

public class EnrollmentResourceWrapper extends BaseWrapper<EnrollmentResource> {

    public EnrollmentResourceWrapper(EnrollmentResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public EnrollmentRepresentation check(String cle) {
        validateNotEmpty(cle, "Alias cle");
        return proxy.check(cle);
    }
}
