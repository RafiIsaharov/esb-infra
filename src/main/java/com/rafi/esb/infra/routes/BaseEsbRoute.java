package com.rafi.esb.infra.routes;
import com.rafi.esb.infra.routes.config.BaseRouteConfig;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;

import java.util.Arrays;

@ApplicationScoped
public class BaseEsbRoute extends RouteBuilder {
    protected final JacksonDataFormat jsonDataFormat;
    public BaseEsbRoute(){
        this.jsonDataFormat = new JacksonDataFormat();
    }

    @Override
    public void configure() {
        log.debug("Starting route configuration in BaseEsbRoute :");
        setupRoute();
    }
    public void setupRoute() {
        exceptionHandler();
       for (BaseRouteConfig config : BaseRouteConfig.values())
           if (Boolean.parseBoolean(getContext().resolvePropertyPlaceholders(config.getProperty())))
               config.configureRoute(this, this.jsonDataFormat);
    }
    public void exceptionHandler() {
        onException(HttpOperationFailedException.class)
                .process(exchange -> {
                    HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            HttpOperationFailedException.class);
                    int statusCode = exception.getStatusCode();
                    if (statusCode == 400 || statusCode == 415 || statusCode == 500) {
                    String responseBody = exception.getResponseBody();
                    log.error(String.format("Error %d: %s",statusCode, responseBody));
                    }
                }).handled(true);

        onException(Exception.class)
                .process(exchange -> {
                    Exception exc = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    log.error("Route ESB Request ERROR: ");
                    log.error(String.format("Error %s",exc.getMessage()));
                    Arrays.stream(exc.getStackTrace()).forEach(ste -> log.error(String.valueOf(ste)));
                }).handled(true);
    }
}