package com.rafi.esb.infra.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.tooling.model.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class JsonProcessor {
    private final Logger logger = LoggerFactory.getLogger(JsonProcessor.class);
    final ObjectMapper objectMapper;
    @Inject
    public JsonProcessor(ObjectMapper objectMapper){
        this.objectMapper =objectMapper;
    }

    public String extractTagFromJson(String json, String fullPath) {
        try {
            // Check if the input is a valid JSON object or array
            if (!json.trim().startsWith("{") && !json.trim().startsWith("[")) {
                return "";
            }
            if(Strings.isNullOrEmpty(fullPath)){
                return "";
            }
            // Deserialize JSON string to a JsonNode
            JsonNode rootNode;
            // Deserialize JSON string to a JsonNode
            rootNode = objectMapper.readTree(json);
            logger.info("json:{}",json);
            // Split the full path into individual keys
            List<String> keys = Arrays.asList(fullPath.split("/"));
            logger.info("keys:{}",keys);
            // Navigate through the JSON structure according to the path using Stream API
            JsonNode currentNode = rootNode;
            for (String key : keys) {
                currentNode = navigateJsonNode(key, currentNode);
                if (currentNode == null) {
                    return "";
                }
            }
            // Return the value associated with the tag
            return currentNode.asText();
        } catch (Exception e) {
            logger.error("Error processing JSON - {}",e.getMessage(),e);
            return "";
        }
    }
    private JsonNode navigateJsonNode(String key, JsonNode currentNode) {
        if (currentNode.isArray()){
            return currentNode.get(Integer.parseInt(key));
        } else {
            return currentNode.get(key);
        }
    }

    public String extractTagFromJsonPath(String json, String fullPath) {
        Object dataObject = JsonPath.parse(json).read(fullPath);
        return dataObject.toString();
    }

    public JsonNode updateJsonValue(String json, String jsonPath, Object newValue) throws Exception {
        JsonNode root = objectMapper.readTree(json);

        String[] pathElements = jsonPath.replace("$.", "").split("\\.");
        JsonNode currentNode = root;
        for (int i = 0; i < pathElements.length - 1; i++) {
            currentNode = currentNode.path(pathElements[i]);
        }

        // Update the value
        if (currentNode instanceof ObjectNode) {
            if (newValue instanceof String) {
                ((ObjectNode) currentNode).put(pathElements[pathElements.length - 1], newValue.toString());
            } else {
                ((ObjectNode) currentNode).put(pathElements[pathElements.length - 1], (JsonNode) newValue);
            }
        }

        return root;
    }
}