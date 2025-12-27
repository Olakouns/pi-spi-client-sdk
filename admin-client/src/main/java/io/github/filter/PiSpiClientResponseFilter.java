package io.github.filter;

import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

public class PiSpiClientResponseFilter extends BaseFilter implements ClientResponseFilter {

    private static final Logger logger = Logger.getLogger(PiSpiClientResponseFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        String method = requestContext.getMethod();
        String uri = requestContext.getUri().toString();

        if (logger.isDebugEnabled()) {
            logResponse(method, uri, status, responseContext);
        }

//        if (status >= 400 && status != 401) {
        if (status >= 400) {
            handleErrorResponse(method, uri, status, responseContext);
        }
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
