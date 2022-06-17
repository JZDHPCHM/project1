package gbicc.irs.reportTemplate.jpa.support;

import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.reportTemplate.jpa.entity.FinancialIndicators;

/**
 * 财报指标 json 序列化器
 * @author kpy
 *
 */
public class FinancialIndicatorsJsonSerializer extends JsonSerializer<FinancialIndicators>{
	@Override
	public void serialize(FinancialIndicators value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeObjectField("id", value.getId());
		gen.writeObjectField("indicatorsCode", value.getIndicatorsCode());
		gen.writeObjectField("indicatorsName", value.getIndicatorsName());
		gen.writeObjectField("indicatorsDescribe", value.getIndicatorsDescribe());
		gen.writeObjectField("applyModel", value.getApplyModel());
		AuditorEntityJsonSerializer.serializeJson(value, gen, serializers);
		gen.writeEndObject();
	}
}
