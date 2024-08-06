package com.rafi.esb.infra.routes.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.rafi.esb.infra.utils.JsonProcessor;
import org.apache.camel.tooling.model.Strings;

import java.util.Map;

public class JsonSplitProcessor implements Processor {
    String pathDelimiter;
    String updateTags;


    public JsonSplitProcessor(String path, String updateTags) {
        this.pathDelimiter = path;
        this.updateTags = updateTags;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JsonProcessor jp = new JsonProcessor(new ObjectMapper());
        String originalJson = exchange.getProperty("origBody", String.class);
        JsonNode splitPartJsonNode = new ObjectMapper().valueToTree(exchange.getIn().getBody(Map.class));
        JsonNode resultJson = jp.updateJsonValue(originalJson, pathDelimiter, splitPartJsonNode);
        resultJson = getJsonNodeWithUpdatedTags(resultJson, jp);
        exchange.getIn().setBody(resultJson);
    }

    private JsonNode getJsonNodeWithUpdatedTags(JsonNode resultJson, JsonProcessor jp) throws Exception {
        if (!Strings.isNullOrEmpty(updateTags)) {
            for (String tag : updateTags.split(",")) {
                String[] tagValue = tag.split("=");
                if (tagValue.length == 2)
                    resultJson = jp.updateJsonValue(resultJson.toString(), tagValue[0], tagValue[1]);
                else {
                    throw new IllegalArgumentException("Invalid tag format: " + tag);
                }
            }
        }
        return resultJson;
    }
}
