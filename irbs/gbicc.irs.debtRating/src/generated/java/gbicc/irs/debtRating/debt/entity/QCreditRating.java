package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreditRating is a Querydsl query type for CreditRating
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCreditRating extends EntityPathBase<CreditRating> {

    private static final long serialVersionUID = 1315329760L;

    public static final QCreditRating creditRating = new QCreditRating("creditRating");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath assetsCode = createString("assetsCode");

    public final StringPath assetsName = createString("assetsName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath creditType = createString("creditType");

    public final StringPath custCode = createString("custCode");

    public final StringPath custName = createString("custName");

    public final DateTimePath<java.util.Date> fdDate = createDateTime("fdDate", java.util.Date.class);

    public final StringPath fdVersion = createString("fdVersion");

    public final StringPath finalAdvice = createString("finalAdvice");

    public final StringPath finalCode = createString("finalCode");

    public final DateTimePath<java.util.Date> finalDate = createDateTime("finalDate", java.util.Date.class);

    public final StringPath finalLevel = createString("finalLevel");

    public final StringPath finalName = createString("finalName");

    public final NumberPath<java.math.BigDecimal> finalPd = createNumber("finalPd", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> finalSco = createNumber("finalSco", java.math.BigDecimal.class);

    public final StringPath id = createString("id");

    public final StringPath internCode = createString("internCode");

    public final DateTimePath<java.util.Date> internDate = createDateTime("internDate", java.util.Date.class);

    public final StringPath internLevel = createString("internLevel");

    public final StringPath internName = createString("internName");

    public final NumberPath<java.math.BigDecimal> internSco = createNumber("internSco", java.math.BigDecimal.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<java.math.BigDecimal> pd = createNumber("pd", java.math.BigDecimal.class);

    public final StringPath projectCode = createString("projectCode");

    public final StringPath projectName = createString("projectName");

    public final StringPath ratingStatus = createString("ratingStatus");

    public final StringPath ratingType = createString("ratingType");

    public final StringPath ratingVaild = createString("ratingVaild");

    public final StringPath type = createString("type");

    public final StringPath vaild = createString("vaild");

    public QCreditRating(String variable) {
        super(CreditRating.class, forVariable(variable));
    }

    public QCreditRating(Path<? extends CreditRating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreditRating(PathMetadata metadata) {
        super(CreditRating.class, metadata);
    }

}

