package com.rafi.esb.infra.routes.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class HeaderProcessor implements Processor {
    private final String headers;

    public HeaderProcessor(String headers) {
        this.headers = headers;
    }
    @Override
    public void process(Exchange exchange) {
        if (!"0".equals(headers)){
            String[] headerArray = headers.split(",");
            for (String header : headerArray) {
                String[] keyValue = header.split("=");
                if (keyValue.length == 2) {
                    exchange.getIn().setHeader(keyValue[0].trim(), keyValue[1].trim());
                } else {
                    throw new IllegalArgumentException("Invalid header format: " + header);
                }
            }
        }
    }
}
