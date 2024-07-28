package com.rafi.esb.infra.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.PathNotFoundException;
import com.rafi.esb.infra.tools.HumanReadableTestNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(HumanReadableTestNames.class)
class JsonProcessorTest {

    private final Logger logger = LoggerFactory.getLogger(JsonProcessorTest.class);

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonProcessor jsonProcessor;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    /*                               extractTagFromJson tests                               */
    @Test
    void testExtractTagFromJsonPass_WhentheTagExists() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "key1";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String result = jsonProcessor.extractTagFromJson(json, fullPath);

        assertThat(result).isEqualTo("value1");
    }

    @Test
    void testExtractTagFromJsonFails_WhenInvalidJson() {
        String json = "invalid json";
        String fullPath = "key1";

        String result = jsonProcessor.extractTagFromJson(json, fullPath);

        assertThat(result).isEmpty();
    }

    @Test
    void testExtractTagFromJson_WhenPathIsInvalid() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "key3";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String result = jsonProcessor.extractTagFromJson(json, fullPath);

        assertThat(result).isEmpty();
    }

    @Test
    void testExtractSameTagInDiffPathFromJson_WhenPathExists() throws Exception {
        String json = """
                {
                "header": {
                "key1": "header.value1",
                "key2": "header.value2"
                },
                "body": {
                "key1": "body.value1",
                "key2": "body.value2"
                }
                }""";
        String fullPathHeader = "header/key1";
        String fullPathBody = "body/key1";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String resultHeader = jsonProcessor.extractTagFromJson(json, fullPathHeader);
        String resultBody = jsonProcessor.extractTagFromJson(json, fullPathBody);

        assertThat(resultHeader).isEqualTo("header.value1");
        assertThat(resultBody).isEqualTo("body.value1");
    }
    @Test
    void testExtractSameTagInArrayFromJsonPass_WhenPathExists() throws Exception {
        String json = """
                {
                "header": {
                "key1": "header.value1",
                "key2": "header.value2"
                },
                "body": [
                {
                "key1": "body.value11",
                "key2": "body.value12"
                },
                {
                "key1": "body.value21",
                "key2": "body.value22"
                }
                ]
                }""";
        String fullPathBody_0 = "body/0/key2";
        String fullPathBody_1 = "body/1/key2";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String resultBody_0 = jsonProcessor.extractTagFromJson(json, fullPathBody_0);
        String resultBody_1 = jsonProcessor.extractTagFromJson(json, fullPathBody_1);

        assertThat(resultBody_0).isEqualTo("body.value12");
        assertThat(resultBody_1).isEqualTo("body.value22");
    }

    @Test
    @DisplayName("Verify extracting tag from a real sample response")
    void testExtractSameTagInRealResponse() throws Exception {
        String json = getJsonFromFile();
        String fullPath = "Records/0/ResultID";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String result = jsonProcessor.extractTagFromJson(json, fullPath);

        assertThat(result).isEqualTo("8643684778");
    }

    @Test
    void testReturnsEmptyStringWhenPathIsEmpty() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "";

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));

        String result = jsonProcessor.extractTagFromJson(json, fullPath);

        assertThat(result).isEmpty();
    }

    /*                               extractTagFromJsonPath tests                               */
    @Test
    void testExtractTagFromJsonPathPass_WhentheTagExists() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "key1";

        String result = jsonProcessor.extractTagFromJsonPath(json, fullPath);

        assertThat(result).isEqualTo("value1");
    }

    @Test
    void testExtractTagFromJsonPathFails_WhenInvalidJson() {
        String json = "invalid json";
        String fullPath = "key1";

        assertThatThrownBy(()->jsonProcessor.extractTagFromJsonPath(json, fullPath))
                .isInstanceOf(PathNotFoundException.class);
    }

    @Test
    void testExtractTagFromJsonPath_WhenPathIsInvalid() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "key3";

        assertThatThrownBy(()->jsonProcessor.extractTagFromJsonPath(json, fullPath))
                .isInstanceOf(PathNotFoundException.class);
    }

    @Test
    void testExtractSameTagInDiffPathFromJsonPath_WhenPathExists() throws Exception {
        String json = """
                {
                "header": {
                "key1": "header.value1",
                "key2": "header.value2"
                },
                "body": {
                "key1": "body.value1",
                "key2": "body.value2"
                }
                }""";
        String fullPathHeader = "header.key1";
        String fullPathBody = "body.key1";

        String resultHeader = jsonProcessor.extractTagFromJsonPath(json, fullPathHeader);
        String resultBody = jsonProcessor.extractTagFromJsonPath(json, fullPathBody);

        assertThat(resultHeader).isEqualTo("header.value1");
        assertThat(resultBody).isEqualTo("body.value1");
    }
    @Test
    void testExtractSameTagInArrayFromJsonPathPass_WhenPathExists() throws Exception {
        String json = """
                {
                "header": {
                "key1": "header.value1",
                "key2": "header.value2"
                },
                "body": [
                {
                "key1": "body.value11",
                "key2": "body.value12"
                },
                {
                "key1": "body.value21",
                "key2": "body.value22"
                }
                ]
                }""";
        String fullPathBody_0 = "$.body[0]key2";
        String fullPathBody_1 = "$.body[1]key2";

        String resultBody_0 = jsonProcessor.extractTagFromJsonPath(json, fullPathBody_0);
        String resultBody_1 = jsonProcessor.extractTagFromJsonPath(json, fullPathBody_1);

        assertThat(resultBody_0).isEqualTo("body.value12");
        assertThat(resultBody_1).isEqualTo("body.value22");
    }

    @Test
    @DisplayName("Verify extracting tag from a real sample response")
    void testExtractSameTagFromJsonPathInRealResponse() throws Exception {
        String json = getJsonFromFile();
        String fullPath = "Records[0].ResultID";

        String result = jsonProcessor.extractTagFromJsonPath(json, fullPath);

        assertThat(result).isEqualTo("8643684778");
    }

    @Test
    void testReturnsEmptyStringWhenJsonPathIsEmpty() throws Exception {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        String fullPath = "";

        assertThatThrownBy(()->jsonProcessor.extractTagFromJsonPath(json, fullPath))
                .isInstanceOf(IllegalArgumentException.class);

    }

    /*                               updateJsonValue tests                               */
    @Test
    void testUpdateJsonValueByString() throws Exception {
        String json = "{\"key\":\"oldValue\"}";
        String jsonPath = "$.key";
        String newValue = "newValue";

        JsonNode expected = new ObjectMapper().readTree("{\"key\":\"newValue\"}");

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));
        JsonNode result = jsonProcessor.updateJsonValue(json, jsonPath, newValue);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateJsonValueByObject() throws Exception {
        String json = "{\"key\":\"oldValue\"}";
        String jsonPath = "$.key";
        String newValue = "{\"key1\":\"value1\"}";


        ObjectMapper mapper = new ObjectMapper();
        JsonNode newValueNode = mapper.readTree(newValue);

        JsonNode expected = mapper.readTree("{\"key\":{\"key1\":\"value1\"}}");

        when(objectMapper.readTree(json)).thenReturn(new ObjectMapper().readTree(json));
        //when(objectMapper.readTree("{\"key\":\"newValue\"}")).thenReturn(expected);

        JsonNode result = jsonProcessor.updateJsonValue(json, jsonPath, newValueNode);

        assertEquals(expected, result);
    }

    /*               private utils           */


    private String getJsonFromFile() {
        InputStream inputStream = getClass().getResourceAsStream("/exampleResponse.json");
        if (inputStream == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            logger.error("Error reading JSON file - {}", e.getMessage(), e);
            return "";
        }
    }
}