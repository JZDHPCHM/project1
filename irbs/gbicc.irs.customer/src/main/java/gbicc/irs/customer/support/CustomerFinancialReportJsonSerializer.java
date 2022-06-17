package gbicc.irs.customer.support;

import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.customer.entity.CustomerFinancialReport;

/**
 * 国民经济类型 json 序列化器
 * @author koupengyang
 *
 */
public class CustomerFinancialReportJsonSerializer extends JsonSerializer<CustomerFinancialReport>{
	@Override
	public void serialize(CustomerFinancialReport value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		
		gen.writeObjectField("id", value.getId());
		gen.writeObjectField("ctmNo", value.getCtmNo());
		gen.writeObjectField("financialFeportType", value.getFinancialFeportType());
		gen.writeObjectField("financialReport", value.getFinancialReport());
		gen.writeObjectField("financialReportDt", value.getFinancialReportDt());
		AuditorEntityJsonSerializer.serializeJson(value, gen, serializers);
		gen.writeEndObject();
	}

	
}
