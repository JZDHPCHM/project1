/*package gbicc.irs.customer.support;

import java.io.IOException;

import org.wsp.framework.jpa.entity.support.AuditorEntityJsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import gbicc.irs.customer.entity.FinancialStatements;

*//**
 * 财报实体 json 序列化器
 * @author kou
 *
 *//*
public class FinancialStatementsJsonSerializer extends JsonSerializer<FinancialStatements>{
	@Override
	public void serialize(FinancialStatements value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeObjectField("id", value.getFinStatementId());
		gen.writeObjectField("bpCode", value.getBpCode());
		gen.writeObjectField("fiscalTimes",value.getFiscalTimes());
		gen.writeObjectField("currencyCodeDesc",value.getCurrencyCodeDesc());
		gen.writeObjectField("reportDetailType",value.getReportDetailType());
		gen.writeObjectField("financialSubjectDatas",value.getFinancialSubjectDatas());
		gen.writeEndObject();
	}
}
*/