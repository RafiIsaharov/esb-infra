package com.rafi.esb.infra.routes.config;

import com.rafi.esb.infra.routes.handlers.ResponseRouteHandlers;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import static com.rafi.esb.infra.routes.config.RouteFields.*;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.log;

public enum ResponseRouteConfig implements RouteProperties{
    ROUTE_RESPONSE_FROM_VENDOR_TO_P2G(ENABLE_ROUTE_RESPONSE_FROM_VENDOR_TO_P2G) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            builder.from("platform-http:/response?httpMethodRestrict=POST")
                    .to(log("platform-http [before]:").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .process(exchange -> {
                        exchange.getIn().removeHeaders("*", "JMS*");
                        exchange.setPattern(ExchangePattern.InOnly);
                    })
                    .convertBodyTo(String.class)
                    .bean(responseRouteHandlers.responseMatchingHandler())
                    .bean(responseRouteHandlers.responseHandlerFrom())
                    .bean(responseRouteHandlers.responseTransformationHandler())
                    .bean(responseRouteHandlers.responseHandlerTo())
                    .to(log("platform-http [after]:").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .to(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME), resolveProperty(P2G_AMQ_CONNECTION_FACTORY)));
        }
    },
    ROUTE_SECOND_RESPONSE_FROM_VENDOR_TO_P2G(ENABLE_ROUTE_SECOND_RESPONSE_FROM_VENDOR_TO_P2G) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            builder.from("platform-http:/second_response?httpMethodRestrict=POST")
                    .to(log("platform-http [before]:").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .process(exchange -> {
                        exchange.getIn().removeHeaders("*", "JMS*");
                        exchange.setPattern(ExchangePattern.InOnly);
                    })
                    .convertBodyTo(String.class)
                    .bean(responseRouteHandlers.secondResponseMatchingHandler())
                    .bean(responseRouteHandlers.secondResponseHandlerFrom())
                    .bean(responseRouteHandlers.secondResponseTransformationHandler())
                    .bean(responseRouteHandlers.responseHandlerTo())
                    .to(log("platform-http [after]:").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .to(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(SECOND_RESPONSE_P2G_AMQ_QUEUE_NAME),
                            resolveProperty(P2G_AMQ_CONNECTION_FACTORY)));
        }
    },
    ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_P2G_AMQ(ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_P2G_AMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            builder.from(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(RESPONSE_VENDOR_AMQ_QUEUE_NAME),resolveProperty(VENDOR_AMQ_CONNECTION_FACTORY)))
                    .routeId("from-activemq-routing-%s".formatted(resolveProperty(RESPONSE_VENDOR_AMQ_QUEUE_NAME)))
                    .to(log("Amq-message").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .convertBodyTo(String.class)
                    .bean(responseRouteHandlers.responseMatchingHandler())
                    .bean(responseRouteHandlers.responseHandlerFrom())
                    .to(SEDA_CAMEL.formatted(resolveProperty(VENDOR_NAME),resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME)));

            builder.from(SEDA_CAMEL.formatted(resolveProperty(VENDOR_NAME),resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME)))
                    .routeId("to-activemq-routing-%s".formatted(resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME)))
                    .log("body: ${body}")
                    .bean(responseRouteHandlers.responseTransformationHandler())
                    .bean(responseRouteHandlers.responseHandlerTo())
                    .to(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)));
        }
    },
    ROUTE_RESPONSE_FROM_VENDOR_IBMMQ_TO_P2G_AMQ(ENABLE_ROUTE_RESPONSE_FROM_VENDOR_IBMMQ_TO_P2G_AMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");

            builder.from(MQ_CONNECTION.formatted(IBMMQ_CAMEL,resolveProperty(RESPONSE_VENDOR_IBMMQ_QUEUE_NAME),resolveProperty(VENDOR_IBM_CONNECTION_FACTORY)))
                    .log("Received response from vendor: ${body}")
                    .bean(responseRouteHandlers.responseMatchingHandler())
                    .bean(responseRouteHandlers.responseHandlerFrom())
                    .to(SEDA_CAMEL.formatted(resolveProperty(RESPONSE_VENDOR_IBMMQ_QUEUE_NAME), resolveProperty(VENDOR_NAME)));

            builder.from(SEDA_CAMEL.formatted(resolveProperty(RESPONSE_VENDOR_IBMMQ_QUEUE_NAME), resolveProperty(VENDOR_NAME)))
                    .bean(responseRouteHandlers.responseTransformationHandler())
                    .bean(responseRouteHandlers.responseHandlerTo())
                    .to(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)));
        }
    },
    ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PW(ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PW) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            String sftpComponent = "%s%s".formatted(SFTP_CAMEL_WITH_PASSWORD, SFTP_CAMEL_FILE_NAME)
                    .formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(SFTP_TO_FOLDER),
                            resolveProperty(SFTP_USERNAME),
                            resolveProperty(SFTP_PASSWORD),
                            resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME),
                            SFTP_FILE_NAME_DATE_FORMAT);

            routeFromVendorAmqToSftp(builder, sftpComponent);
        }
    },
    ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PK(ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PK) {
        @Override
        public void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers) {
            String sftpComponent = "%s%s".formatted(SFTP_CAMEL_WITH_PRIVATE_KEY, SFTP_CAMEL_FILE_NAME)
                    .formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(SFTP_TO_FOLDER),
                            resolveProperty(SFTP_USERNAME),
                            resolveProperty(SFTP_PRIVATE_KEY_FILE),
                            resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME),
                            SFTP_FILE_NAME_DATE_FORMAT);

            routeFromVendorAmqToSftp(builder, sftpComponent);
        }
    };

    protected void routeFromVendorAmqToSftp(RouteBuilder builder, String sftpComponent) {
        builder.from(MQ_CONNECTION.formatted(AMQ_CAMEL,resolveProperty(RESPONSE_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)))
                .log("Received message: ${body}")
                .to(sftpComponent);
    }
    private final String property;
    ResponseRouteConfig(String property) {
        this.property = property;
    }
    public String getProperty() {
        return resolveProperty(property);
    }
    public abstract void configureRoute(RouteBuilder builder, ResponseRouteHandlers responseRouteHandlers);
}
