package ai.z.openapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes the structured output schema used when {@link ResponseFormat#getType()} is
 * {@code json_schema}.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJsonSchema {

	/**
	 * Name of the structured output schema. Required and must not be empty when the
	 * response format type is {@code json_schema}.
	 */
	private String name;

	/**
	 * Description of the structured output schema. Optional.
	 */
	private String description;

	/**
	 * Whether to enable strict mode. Defaults to {@code true} when omitted. When enabled,
	 * the model strictly follows {@link #schema}.
	 */
	private Boolean strict;

	/**
	 * Target output JSON Schema. Must be a JSON object (root type must be {@code object};
	 * {@code anyOf} and other combinators are not supported). Carried as a free-form
	 * {@link JsonNode} so any valid JSON Schema can be passed through without
	 * strong-typed assumptions by the gateway.
	 */
	private JsonNode schema;

}