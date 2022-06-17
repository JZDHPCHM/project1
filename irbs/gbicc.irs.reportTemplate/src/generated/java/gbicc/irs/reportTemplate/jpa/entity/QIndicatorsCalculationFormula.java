package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIndicatorsCalculationFormula is a Querydsl query type for IndicatorsCalculationFormula
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIndicatorsCalculationFormula extends EntityPathBase<IndicatorsCalculationFormula> {

    private static final long serialVersionUID = -909234307L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIndicatorsCalculationFormula indicatorsCalculationFormula = new QIndicatorsCalculationFormula("indicatorsCalculationFormula");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath calculationFormula = createString("calculationFormula");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final QFinancialIndicators financialIndicators;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath reportTemplate = createString("reportTemplate");

    public QIndicatorsCalculationFormula(String variable) {
        this(IndicatorsCalculationFormula.class, forVariable(variable), INITS);
    }

    public QIndicatorsCalculationFormula(Path<? extends IndicatorsCalculationFormula> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIndicatorsCalculationFormula(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIndicatorsCalculationFormula(PathMetadata metadata, PathInits inits) {
        this(IndicatorsCalculationFormula.class, metadata, inits);
    }

    public QIndicatorsCalculationFormula(Class<? extends IndicatorsCalculationFormula> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.financialIndicators = inits.isInitialized("financialIndicators") ? new QFinancialIndicators(forProperty("financialIndicators")) : null;
    }

}

