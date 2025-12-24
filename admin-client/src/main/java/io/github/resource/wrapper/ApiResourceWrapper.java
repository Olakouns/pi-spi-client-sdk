package io.github.resource.wrapper;

import io.github.resource.ApiResource;

import javax.ws.rs.client.WebTarget;

public class ApiResourceWrapper {
    private final ApiResource proxy;
    private final WebTarget target;

    private ComptesResourceWrapper comptes;

    public ApiResourceWrapper(ApiResource proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public synchronized ComptesResourceWrapper comptes() {
        if (this.comptes == null) {
            this.comptes = new ComptesResourceWrapper(proxy.comptes(), target.path("/comptes"));
        }
        return this.comptes;
    }
}
