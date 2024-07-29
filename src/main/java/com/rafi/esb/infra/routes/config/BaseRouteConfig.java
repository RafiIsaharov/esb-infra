package com.rafi.esb.infra.routes.config;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.component.jackson.JacksonDataFormat;

import java.util.Base64;
import java.util.Map;

import static com.rafi.esb.infra.routes.config.RouteFields.AUTH;
import static com.rafi.esb.infra.routes.config.RouteFields.HTTP_CONTENT_TYPE;

public enum BaseRouteConfig implements RouteProperties {
    TIMER_TO_VENDOR_REQUEST_BASIC_AUTH_SERVICE(ENABLE_TIMER_TO_VENDOR_REQUEST_BASIC_AUTH_SERVICE) {
        @Override
        public void configureRoute(RouteBuilder builder, JacksonDataFormat jsonDataFormat) {
            timerToVendorAuthService(builder, jsonDataFormat,"Request" , "Basic");
        }
    },
    TIMER_TO_VENDOR_REQUEST_AUTH_SERVICE(ENABLE_TIMER_TO_VENDOR_REQUEST_AUTH_SERVICE) {
        @Override
        public void configureRoute(RouteBuilder builder, JacksonDataFormat jsonDataFormat) {
            timerToVendorAuthService(builder, jsonDataFormat, "Request", "Bearer");
        }
    },
    TIMER_TO_VENDOR_RESPONSE_AUTH_SERVICE(ENABLE_TIMER_TO_VENDOR_RESPONSE_AUTH_SERVICE) {
        @Override
        public void configureRoute(RouteBuilder builder, JacksonDataFormat jsonDataFormat) {
            timerToVendorAuthService(builder, jsonDataFormat, "Response", "Bearer");
        }
    };
    private final String property;
    BaseRouteConfig(String property) {
        this.property = property;
    }
    protected void timerToVendorAuthService(RouteBuilder builder, JacksonDataFormat jsonDataFormat, String routeType, String authType){
        builder.from(String.format("timer:authServiceTimer%s?period=%s", routeType, resolveProperty(REST_PERIOD,
                routeType)))
                .routeId("authServiceTimer%s".formatted(routeType))
                .log("get access token %s".formatted(routeType))
                .process(exchange -> {
                    exchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpMethods.POST);
                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, HTTP_CONTENT_TYPE);
                    exchange.getIn().setHeader("Accept", HTTP_CONTENT_TYPE);
                    if("Basic".equals(authType)){
                        exchange.getIn().setHeader(AUTH,getBasicAuth(builder));
                    }
                    else{
                        exchange.getIn().setBody(getBearerAuthContent(routeType));
                    }
                })
                .to(resolveProperty(REST_AUTH_URL,routeType))
                .convertBodyTo(String.class)
                .log("response from API: ${body}")
                .choice()
                .when().simple("${header.CamelHttpResponseCode} == 200")
                .unmarshal(jsonDataFormat)
                .process(exchange -> exchange.getContext().setVariable("global:accessToken%s".formatted(routeType),
                        exchange.getIn().getBody(Map.class).get("access_token").toString()))
                .otherwise()
                .log("Not Authenticated!!!");
    }
    private String getBasicAuth(RouteBuilder builder) {
        String username = builder.getContext().resolvePropertyPlaceholders(resolveProperty(REST_USERNAME,"request"));
        String password = builder.getContext().resolvePropertyPlaceholders(resolveProperty(REST_PASSWORD,"request"));
        String auth = username + ":" + password;
        return "Basic %s".formatted(Base64.getEncoder().encodeToString(auth.getBytes()));
    }
    private String getBearerAuthContent(String routeType){
        StringBuilder bearerContent = new StringBuilder();
        String grantType = resolveProperty(REST_GRANT_TYPE, routeType);
        bearerContent.append("grant_type=").append(grantType)
                .append("&client_id=").append(resolveProperty(REST_CLIENT_ID, routeType))
                .append("&client_secret=").append(resolveProperty(REST_CLIENT_SECRET, routeType));
        if("password".equalsIgnoreCase(grantType)){
            bearerContent.append("&username=").append(resolveProperty(REST_USERNAME, routeType))
                    .append("&password=").append(resolveProperty(REST_PASSWORD, routeType));
        }
        return bearerContent.append("&scope=").append(resolveProperty(REST_SCOPE, routeType)).toString();
    }
    public String getProperty() {
        return resolveProperty(property);
    }
    public abstract void configureRoute(RouteBuilder builder, JacksonDataFormat jsonDataFormat);
}
