package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinancialIndicators is a Querydsl query type for FinancialIndicators
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialIndicators extends EntityPathBase<FinancialIndicators> {

    private static final long serialVersionUID = -516468623L;

    public static final QFinancialIndicators financialIndicators = new QFinancialIndicators("financialIndicators");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final SetPath<String, StringPath> applyModel = this.<String, StringPath>createSet("applyModel", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final ListPath<IndicatorsCalculationFormula, QIndicatorsCalculationFormula> indicatorsCalculationFormulas = this.<IndicatorsCalculationFormula, QIndicatorsCalculationFormula>createList("indicatorsCalculationFormulas", IndicatorsCalculationFormula.class, QIndicatorsCalculationFormula.class, PathInits.DIRECT2);

    public final StringPath indicatorsCode = createString("indicatorsCode");

    public final StringPath indicatorsDescribe = createString("indicatorsDescribe");

    public final StringPath indicatorsName = createString("indicatorsName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFinancialIndicators(String variable) {
        super(FinancialIndicators.class, forVariable(variable));
    }

    public QFinancialIndicators(Path<? extends FinancialIndicators> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialIndicators(PathMetadata metadata) {
        super(FinancialIndicators.class, metadata);
    }

}

