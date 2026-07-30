package ai.z.openapi.service.model;

import ai.z.openapi.service.deserialize.MessageDeserializeFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ResponseFormat} and {@link ResponseJsonSchema} JSON (de)serialization.
 */
class ResponseFormatTest {

	private final ObjectMapper mapper = MessageDeserializeFactory.defaultObjectMapper();

	@Test
	void enumValues() {
		assertEquals("text", ResponseFormatType.TEXT.value());
		assertEquals("json_object", ResponseFormatType.JSON_OBJECT.value());
		assertEquals("json_schema", ResponseFormatType.JSON_SCHEMA.value());
	}

	@Test
	void serializeJsonSchema() throws Exception {
		ObjectNode schema = mapper.createObjectNode();
		schema.put("type", "object");
		schema.set("properties", mapper.createObjectNode());
		schema.set("required", mapper.createArrayNode());

		ResponseFormat format = ResponseFormat.builder()
			.type(ResponseFormatType.JSON_SCHEMA.value())
			.jsonSchema(ResponseJsonSchema.builder()
				.name("math_response")
				.description("数学推理结构化输出")
				.strict(true)
				.schema(schema)
				.build())
			.build();

		String json = mapper.writeValueAsString(format);

		// snake_case keys and nested json_schema object
		assertTrue(json.contains("\"type\":\"json_schema\""));
		assertTrue(json.contains("\"json_schema\":{"));
		assertTrue(json.contains("\"name\":\"math_response\""));
		assertTrue(json.contains("\"strict\":true"));
		assertTrue(json.contains("\"description\":\"数学推理结构化输出\""));
		// schema is passed through as a free-form object
		assertTrue(json.contains("\"schema\":{\"type\":\"object\""));
	}

	@Test
	void roundTripJsonSchema() throws Exception {
		String json = "{\"type\":\"json_schema\",\"json_schema\":{\"name\":\"weather\",\"strict\":true,"
				+ "\"schema\":{\"type\":\"object\",\"properties\":{\"city\":{\"type\":\"string\"}}}}}";

		ResponseFormat format = mapper.readValue(json, ResponseFormat.class);
		assertEquals("json_schema", format.getType());
		assertEquals("weather", format.getJsonSchema().getName());
		assertTrue(format.getJsonSchema().getStrict());

		JsonNode schema = format.getJsonSchema().getSchema();
		assertEquals("object", schema.get("type").asText());
		assertEquals("string", schema.get("properties").get("city").get("type").asText());

		// serialize back should keep the same keys
		String back = mapper.writeValueAsString(format);
		assertTrue(back.contains("\"json_schema\":{"));
	}

	@Test
	void serializeTextOmitsJsonSchema() throws Exception {
		ResponseFormat format = ResponseFormat.builder().type(ResponseFormatType.TEXT.value()).build();
		String json = mapper.writeValueAsString(format);
		assertEquals("{\"type\":\"text\"}", json);
	}

}