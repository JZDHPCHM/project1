package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinancialStatements is a Querydsl query type for FinancialStatements
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialStatements extends EntityPathBase<FinancialStatements> {

    private static final long serialVersionUID = -1108466L;

    public static final QFinancialStatements financialStatements = new QFinancialStatements("financialStatements");

    public final StringPath auditOpinion = createString("auditOpinion");

    public final StringPath auditOrganization = createString("auditOrganization");

    public final StringPath bpCode = createString("bpCode");

    public final StringPath checkPassRate = createString("checkPassRate");

    public final StringPath currencyCodeDesc = createString("currencyCodeDesc");

    public final StringPath finStatementId = createString("finStatementId");

    public final StringPath fiscalTimes = createString("fiscalTimes");

    public final StringPath isAudit = createString("isAudit");

    public final StringPath isNullBalance = createString("isNullBalance");

    public final StringPath isNullTable = createString("isNullTable");

    public final StringPath reportCycle = createString("reportCycle");

    public final StringPath reportDetailType = createString("reportDetailType");

    public final StringPath reportSource = createString("reportSource");

    public final StringPath reportState = createString("reportState");

    public final StringPath reportType = createString("reportType");

    public final StringPath vaild = createString("vaild");

    public final NumberPath<Long> year = createNumber("year", Long.class);

    public QFinancialStatements(String variable) {
        super(FinancialStatements.class, forVariable(variable));
    }

    public QFinancialStatements(Path<? extends FinancialStatements> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialStatements(PathMetadata metadata) {
        super(FinancialStatements.class, metadata);
    }

}

