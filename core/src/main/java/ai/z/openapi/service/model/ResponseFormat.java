package ai.z.openapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseFormat {

	/**
	 * Response format type. Allowed values: text, json_object, json_schema. Defaults to
	 * text.
	 */
	@JsonProperty("type")
	private String type;

	/**
	 * Required when type is json_schema. Describes the structured output schema.
	 */
	@JsonProperty("json_schema")
	private ResponseJsonSchema jsonSchema;

}
