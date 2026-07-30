package ai.z.openapi.samples;

import ai.z.openapi.ZaiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import ai.z.openapi.service.model.ResponseFormat;
import ai.z.openapi.service.model.ResponseFormatType;
import ai.z.openapi.service.model.ResponseJsonSchema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

/**
 * Structured Output (json_schema) Example Demonstrates how to constrain model output with
 * an explicit JSON Schema via {@code response_format.type = json_schema}.
 */
public class ChatCompletionJsonSchemaExample {

	public static void main(String[] args) {
		// export ZAI_API_KEY=your.api_key
		ZaiClient client = ZaiClient.builder().ofZAI().build();

		// Build the target JSON Schema as a free-form JsonNode so any valid JSON Schema
		// can be passed through to the model.
		ObjectMapper mapper = new ObjectMapper();
		JsonNode schema = mapper.valueToTree(Collections.singletonMap("type", "object"));

		ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
			.model("glm-5.2")
			.messages(Collections.singletonList(ChatMessage.builder()
				.role(ChatMessageRole.USER.value())
				.content("用 JSON 描述北京当前的天气，包含城市(city)、温度(temperature)和天气状况(condition)")
				.build()))
			.responseFormat(ResponseFormat.builder()
				.type(ResponseFormatType.JSON_SCHEMA.value())
				.jsonSchema(ResponseJsonSchema.builder()
					.name("weather")
					.description("天气信息结构化输出")
					.strict(true)
					.schema(schema)
					.build())
				.build())
			.build();

		try {
			ChatCompletionResponse response = client.chat().createChatCompletion(request);
			if (response.isSuccess()) {
				Object content = response.getData().getChoices().get(0).getMessage();
				System.out.println("Response: " + content);
			}
			else {
				System.err.println("Error: " + response.getMsg());
			}
		}
		catch (Exception e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			client.close();
		}
	}

}