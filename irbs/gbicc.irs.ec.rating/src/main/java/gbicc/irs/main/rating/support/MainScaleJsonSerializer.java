package gbicc.irs.main.rating.support;

import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.main.rating.entity.MainScale;

/**
 * 评级步骤 json 序列化器
 * @author ljc
 *
 */
public class MainScaleJsonSerializer extends JsonSerializer<MainScale>{
	@Override
	public void serialize(MainScale value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		
		gen.writeObjectField("id", value.getId());
		gen.writeObjectField("scaleLevel", value.getScaleLevel());
		gen.writeObjectField("pd", value.getPd());
		gen.writeObjectField("type", value.getType());
		gen.writeObjectField("sort", value.getSort());
		gen.writeObjectField("admissionSuggest", value.getAdmissionSuggest());
		AuditorEntityJsonSerializer.serializeJson(value, gen, serializers);
		gen.writeEndObject();
	}
}
