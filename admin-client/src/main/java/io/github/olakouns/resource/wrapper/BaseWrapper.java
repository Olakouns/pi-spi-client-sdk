package io.github.olakouns.resource.wrapper;

import io.github.olakouns.exception.PiSpiException;

import javax.ws.rs.client.WebTarget;

public abstract class BaseWrapper<R> {
    protected final R proxy;
    protected final WebTarget target;

    protected BaseWrapper(R proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public R proxy() {
        return proxy;
    }

    protected void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new PiSpiException(paramName + " cannot be null", new IllegalArgumentException(paramName));
        }
    }

    protected void validateNotEmpty(String str, String paramName) {
        if (str == null || str.trim().isEmpty()) {
            throw new PiSpiException(paramName + " cannot be null or empty");
        }
    }
}