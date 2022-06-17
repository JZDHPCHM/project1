package gbicc.irs.main.rating.support;


import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.main.rating.entity.RatingMainStep;

/**
 * 评级步骤 json 序列化器
 * @author kou
 *
 */
public class RatingMainStepJsonSerializer extends JsonSerializer<RatingMainStep>{
	@Override
	public void serialize(RatingMainStep value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		
		gen.writeObjectField("id", value.getId());
		gen.writeObjectField("sno", value.getSno());
		gen.writeObjectField("stepName", value.getStepName());
		gen.writeObjectField("stepResources", value.getStepResources());
		gen.writeObjectField("stepType", value.getStepType());
		gen.writeObjectField("modelId", value.getModelId());
		gen.writeObjectField("showHtml", value.getShowHtml());
		if(value.getLastStep() != null) {
			gen.writeObjectField("lastStep", value.getLastStep().getId());
		}
		if(value.getNextStep() != null) {
			gen.writeObjectField("nextStep", value.getNextStep().getId());
		}
		AuditorEntityJsonSerializer.serializeJson(value, gen, serializers);
		gen.writeEndObject();
	}
}
