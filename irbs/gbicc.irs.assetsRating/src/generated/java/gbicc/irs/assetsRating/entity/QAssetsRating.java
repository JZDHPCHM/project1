package gbicc.irs.assetsRating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAssetsRating is a Querydsl query type for AssetsRating
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAssetsRating extends EntityPathBase<AssetsRating> {

    private static final long serialVersionUID = -1163278719L;

    public static final QAssetsRating assetsRating = new QAssetsRating("assetsRating");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath assessedValue = createString("assessedValue");

    public final StringPath assessmentMethods = createString("assessmentMethods");

    public final StringPath assetsCode = createString("assetsCode");

    public final StringPath assetsName = createString("assetsName");

    public final StringPath coreleaseProportion = createString("coreleaseProportion");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath currentStepId = createString("currentStepId");

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

    public final StringPath financeAmount = createString("financeAmount");

    public final StringPath id = createString("id");

    public final StringPath internCode = createString("internCode");

    public final DateTimePath<java.util.Date> internDate = createDateTime("internDate", java.util.Date.class);

    public final StringPath internLevel = createString("internLevel");

    public final StringPath internName = createString("internName");

    public final NumberPath<java.math.BigDecimal> internSco = createNumber("internSco", java.math.BigDecimal.class);

    public final StringPath isCorelease = createString("isCorelease");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath leaseame = createString("leaseame");

    public final StringPath leaseTerm = createString("leaseTerm");

    public final StringPath majorWarning = createString("majorWarning");

    public final StringPath margin = createString("margin");

    public final StringPath netValue = createString("netValue");

    public final StringPath originalValue = createString("originalValue");

    public final NumberPath<java.math.BigDecimal> pd = createNumber("pd", java.math.BigDecimal.class);

    public final StringPath projectCode = createString("projectCode");

    public final StringPath projectName = createString("projectName");

    public final StringPath ratingStatus = createString("ratingStatus");

    public final StringPath ratingType = createString("ratingType");

    public final StringPath ratingVaild = createString("ratingVaild");

    public final StringPath startDate = createString("startDate");

    public final ListPath<RatingAssetsStep, QRatingAssetsStep> steps = this.<RatingAssetsStep, QRatingAssetsStep>createList("steps", RatingAssetsStep.class, QRatingAssetsStep.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public final StringPath vaild = createString("vaild");

    public QAssetsRating(String variable) {
        super(AssetsRating.class, forVariable(variable));
    }

    public QAssetsRating(Path<? extends AssetsRating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAssetsRating(PathMetadata metadata) {
        super(AssetsRating.class, metadata);
    }

}

