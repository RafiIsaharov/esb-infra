package com.rafi.esb.infra.routes.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.rafi.esb.infra.utils.JsonProcessor;

import java.util.Map;

public class SetJsonParentsProcessor implements Processor {
    private final String path;


    public SetJsonParentsProcessor(String path) {
        this.path = path;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JsonProcessor jp = new JsonProcessor(new ObjectMapper());
        String originalJson = exchange.getProperty("originalBody", String.class);
        Map<String, Object> splitPart = exchange.getIn().getBody(Map.class);

        JsonNode splitPartJsonNode = new ObjectMapper().valueToTree(splitPart);

        JsonNode resultJson = jp.updateJsonValue(originalJson, path, splitPartJsonNode)   ;
        exchange.getIn().setBody(resultJson);
    }
}
