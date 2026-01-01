package io.github.razacki.resource.wrapper;

import io.github.razacki.representation.EnrollmentRepresentation;
import io.github.razacki.resource.EnrollmentResource;

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
