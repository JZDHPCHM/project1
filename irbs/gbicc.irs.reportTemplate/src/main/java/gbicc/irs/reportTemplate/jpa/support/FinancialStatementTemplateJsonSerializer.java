package gbicc.irs.reportTemplate.jpa.support;

import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;

/**
 * 财报模板定义 json 序列化器
 * @author kpy
 *
 */
public class FinancialStatementTemplateJsonSerializer extends JsonSerializer<FinancialStatementTemplate>{
	@Override
	public void serialize(FinancialStatementTemplate value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeObjectField("id", value.getId());
		gen.writeObjectField("fsType", value.getFsType());
		gen.writeObjectField("fsFormerType", value.getFsFormerType());
		gen.writeObjectField("fsValid", value.getFsValid());
		AuditorEntityJsonSerializer.serializeJson(value, gen, serializers);
		gen.writeEndObject();
	}
}
