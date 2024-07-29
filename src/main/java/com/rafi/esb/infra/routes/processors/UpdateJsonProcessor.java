package com.rafi.esb.infra.routes.processors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafi.esb.infra.utils.JsonProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class UpdateJsonProcessor implements Processor {
    private final String path;
    private final Object value;

    public UpdateJsonProcessor(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JsonProcessor jp = new JsonProcessor(new ObjectMapper());

        String originalJson = exchange.getIn().getBody(String.class);
        JsonNode resultJson = jp.updateJsonValue(originalJson, path, value);
        exchange.getIn().setBody(resultJson);
    }
}
