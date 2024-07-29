package com.rafi.esb.infra.routes.config;

import com.rafi.esb.infra.routes.processors.HeaderProcessor;
import com.rafi.esb.infra.routes.handlers.RequestRouteHandlers;
import com.rafi.esb.infra.routes.processors.SetJsonParentsProcessor;
import com.rafi.esb.infra.routes.processors.UpdateJsonProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import static com.rafi.esb.infra.routes.config.RouteFields.*;
import static org.apache.camel.builder.Builder.simple;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.log;

public enum RequestRouteConfig implements RouteProperties {
    ROUTE_REQUEST_FROM_P2G_TO_VENDOR(ENABLE_ROUTE_REQUEST_P2G_TO_VENDOR) {
        @Override
        public void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers) {
            builder.from(MQ_CONNECTION.formatted(AMQ_CAMEL, resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)))
                    .routeId("from-activemq-routing-%s".formatted(resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME)))
                    .to(log("Amq-message").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .convertBodyTo(String.class)
                    .bean(requestRouteHandlers.requestMatchingHandler())
                    .bean(requestRouteHandlers.requestHandlerFrom())
                    .to(SEDA_CAMEL.formatted(resolveProperty(REQUEST_REST_METHOD), resolveProperty(VENDOR_NAME)));

            builder.from(SEDA_CAMEL.formatted(resolveProperty(REQUEST_REST_METHOD), resolveProperty(VENDOR_NAME)))
                    .routeId("%s-%s".formatted(resolveProperty(REQUEST_REST_METHOD), resolveProperty(VENDOR_NAME)))
                    .log(BODY_CONTENT)
                    .setHeader(Exchange.HTTP_METHOD).simple(resolveProperty(REQUEST_REST_METHOD))
                    .setHeader("Accept").simple(HTTP_CONTENT_TYPE)
                    .setHeader(Exchange.CONTENT_TYPE).simple(HTTP_CONTENT_TYPE)
                    .to(log("%s-%s".formatted(resolveProperty(REQUEST_REST_METHOD), resolveProperty(VENDOR_NAME))).showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .bean(requestRouteHandlers.requestTransformationHandler())
                    .bean(requestRouteHandlers.requestHandlerTo())
                    .process(new HeaderProcessor(builder.getContext().resolvePropertyPlaceholders(resolveProperty(REQUEST_REST_HEADERS))))
                    .process(exchange ->
                        exchange.getIn().setHeader(AUTH,
                                "Bearer %s".formatted(exchange.getContext().getVariable("accessTokenRequest",
                                        String.class)))
                    )
                    .log("Request headers: ${headers}")
                    .to(resolveProperty(REQUEST_REST_URL))
                    .convertBodyTo(String.class)
                    .choice()
                        .when().simple("${header.CamelHttpResponseCode} == 200")
                            .log("response Http-code: ${header.CamelHttpResponseCode} from %s API:".formatted(resolveProperty(REQUEST_REST_URL)))
                            .log("response body: ${body}")
                            .choice()
                                .when().simple(resolveProperty(RESPONSE_SYNC) + " == true")
                                .to("seda:syncResponse")
                            .endChoice()
                        .otherwise()
                            .log("Error!!! - CamelHttpResponseCode:${header.CamelHttpResponseCode}")
                    .endChoice();

            builder.from("seda:syncResponse")
                    .routeId("syncResponse")
                    .log(BODY_CONTENT)
                    .process(exchange -> {
                        String authPath = exchange.getContext().resolvePropertyPlaceholders(
                                resolveProperty(RESPONSE_AUTHENTICATED_PATHS));
                        if(authPath.contains("response")){
                            exchange.getIn().setHeader(AUTH,
                                    "Bearer " + exchange.getContext().getVariable("accessTokenResponse",
                                            String.class));
                        }else{
                            exchange.getIn().removeHeader(AUTH);
                        }
                    })
                    .setHeader(Exchange.HTTP_METHOD).simple("POST")
                    .setHeader("Accept").simple(HTTP_CONTENT_TYPE)
                    .setHeader(Exchange.CONTENT_TYPE).simple(HTTP_CONTENT_TYPE)
                    .to(log("syncResponse").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .to(resolveProperty(RESPONSE_REST_URL))
                    .convertBodyTo(String.class)
                    .choice()
                        .when().simple("${header.CamelHttpResponseCode} == 200")
                            .log("response from %s API: ${body}".formatted(resolveProperty(RESPONSE_REST_URL)))
                        .otherwise()
                            .log("Error!!! - CamelHttpResponseCode:${header.CamelHttpResponseCode}")
                    .endChoice();
        }
    },
    ROUTE_REQUEST_FROM_P2G_AMQ_TO_VENDOR_AMQ(ENABLE_ROUTE_REQUEST_P2G_AMQ_TO_VENDOR_AMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers) {

            builder.from(MQ_CONNECTION.formatted(AMQ_CAMEL, resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)))
                    .routeId("from-activemq-routing-%s".formatted(resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME)))
                    .to(log("Amq-message").showExchangePattern(true).showBodyType(true).showHeaders(true))
                    .convertBodyTo(String.class)
                    .bean(requestRouteHandlers.requestMatchingHandler())
                    .bean(requestRouteHandlers.requestHandlerFrom())
                    .to(SEDA_CAMEL.formatted(resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME),resolveProperty(VENDOR_NAME)));

            builder.from(SEDA_CAMEL.formatted(resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME),resolveProperty(VENDOR_NAME)))
                    .routeId("to-activemq-routing-%s".formatted(resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                    .log("body: ${body}")
                    .bean(requestRouteHandlers.requestTransformationHandler())
                    .bean(requestRouteHandlers.requestHandlerTo())
                    .to(MQ_CONNECTION.formatted(AMQ_CAMEL, resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME),resolveProperty(VENDOR_AMQ_CONNECTION_FACTORY)));
        }
    },
    ROUTE_REQUEST_FROM_P2G_AMQ_TO_VENDOR_IBMMQ(ENABLE_ROUTE_REQUEST_FROM_P2G_AMQ_TO_VENDOR_IBMMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers) {
            System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
            builder.from(MQ_CONNECTION.formatted(AMQ_CAMEL, resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME),resolveProperty(P2G_AMQ_CONNECTION_FACTORY)))
                    .log("Received request from p2g: ${body}")
                    .routeId("from-activemq:%s".formatted(resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME)))
                    .bean(requestRouteHandlers.requestMatchingHandler())
                    .bean(requestRouteHandlers.requestHandlerFrom())
                    .to(SEDA_CAMEL.formatted(resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME), resolveProperty(VENDOR_NAME)));

            builder.from(SEDA_CAMEL.formatted(resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME), resolveProperty(VENDOR_NAME)))
                    .bean(requestRouteHandlers.requestHandlerTo())
                    .bean(requestRouteHandlers.requestTransformationHandler())
                    .to(MQ_CONNECTION.formatted(IBMMQ_CAMEL,resolveProperty(REQUEST_VENDOR_IBMMQ_QUEUE_NAME),resolveProperty(VENDOR_IBM_CONNECTION_FACTORY)));
        }
    },
    ROUTE_REQUEST_FROM_SFTP_PW_TO_VENDOR_AMQ(ENABLE_ROUTE_REQUEST_FROM_SFTP_PW_TO_VENDOR_AMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers) {
            String sftpComponent = "%s%s".formatted(SFTP_CAMEL_WITH_PASSWORD, SFTP_CAMEL_MOVE)
                    .formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(SFTP_FROM_FOLDER),
                            resolveProperty(SFTP_USERNAME),
                            resolveProperty(SFTP_PASSWORD),
                            resolveProperty(SFTP_ARCHIVE_FOLDER),
                            SFTP_FILE_NAME_DATE_FORMAT);
            routeFromSftpToVendorAmq(builder, sftpComponent);
        }
    },
    ROUTE_REQUEST_FROM_SFTP_PK_TO_VENDOR_AMQ(ENABLE_ROUTE_REQUEST_FROM_SFTP_PK_TO_VENDOR_AMQ) {
        @Override
        public void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers) {
            String sftpComponent = "%s%s".formatted(SFTP_CAMEL_WITH_PRIVATE_KEY, SFTP_CAMEL_MOVE)
                    .formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(SFTP_FROM_FOLDER),
                            resolveProperty(SFTP_USERNAME),
                            resolveProperty(SFTP_PRIVATE_KEY_FILE),
                            resolveProperty(SFTP_ARCHIVE_FOLDER),
                            SFTP_FILE_NAME_DATE_FORMAT);
            routeFromSftpToVendorAmq(builder, sftpComponent);
        }
    };
    protected void routeFromSftpToVendorAmq(RouteBuilder builder, String sftpComponent) {
        String splitAlgorithm = builder.getContext().resolvePropertyPlaceholders(resolveProperty(SFTP_SPLIT_ALGO));
        String splitTag = builder.getContext().resolvePropertyPlaceholders(resolveProperty(SFTP_SPLIT_TAG));

        builder.from(sftpComponent)
                .log("File content: ${body} \n")
                .log ("Split Algorithm:" + splitAlgorithm + "\n")
                .log ("Split Tag:" + splitTag + "\n")
                .choice()
                .when(exchange -> splitAlgorithm.equals("line"))
                    .split().tokenize("\n", getInt(splitTag)).streaming()
                    .to(SEDA_CAMEL.formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                .endChoice()
                .when(exchange -> splitAlgorithm.equals("delimiter"))
                    .split().tokenize(splitTag).streaming()
                    .to(SEDA_CAMEL.formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                .endChoice()
                .when(exchange -> splitAlgorithm.equals("xmltag"))
                    .split().tokenizeXML(splitTag,"*").streaming()
                    .to(SEDA_CAMEL.formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                    .endChoice()
                .when(exchange -> splitAlgorithm.equals("jsontag"))
                    .setProperty("originalBody", simple("${body}"))
                    .split().jsonpath(splitTag).streaming()
                    .process (new SetJsonParentsProcessor(splitTag))
                    .process(new UpdateJsonProcessor("$.numberOfTransactions", "1"))
                    .marshal().json(JsonLibrary.Jackson)
                    .to(SEDA_CAMEL.formatted(resolveProperty(SFTP_ADDRESS),
                            resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                    .endChoice()
                .end();

        builder.from(SEDA_CAMEL.formatted(resolveProperty(SFTP_ADDRESS),
                        resolveProperty(REQUEST_VENDOR_AMQ_QUEUE_NAME)))
                .log("File split content:\n${body}")
                .to(MQ_CONNECTION.formatted(AMQ_CAMEL,
                        resolveProperty(REQUEST_P2G_AMQ_QUEUE_NAME),
                        resolveProperty(P2G_AMQ_CONNECTION_FACTORY)))
                .log("Total number of elements split: ${exchangeProperty.CamelSplitSize}");
    }
    private int getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 200;
        }
    }
    private final String property;
    RequestRouteConfig(String property) {
        this.property = property;
    }
    public String getProperty() {
        return resolveProperty(property);
    }

    public abstract void configureRoute(RouteBuilder builder, RequestRouteHandlers requestRouteHandlers);
}
