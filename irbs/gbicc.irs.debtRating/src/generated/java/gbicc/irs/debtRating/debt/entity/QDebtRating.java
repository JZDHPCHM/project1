package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDebtRating is a Querydsl query type for DebtRating
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDebtRating extends EntityPathBase<DebtRating> {

    private static final long serialVersionUID = -1858551942L;

    public static final QDebtRating debtRating = new QDebtRating("debtRating");

    public final StringPath assetReview = createString("assetReview");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath creator = createString("creator");

    public final StringPath currentStepId = createString("currentStepId");

    public final StringPath custCode = createString("custCode");

    public final StringPath custName = createString("custName");

    public final StringPath fdVersion = createString("fdVersion");

    public final StringPath finalAdvice = createString("finalAdvice");

    public final StringPath finalBs = createString("finalBs");

    public final StringPath finalCode = createString("finalCode");

    public final DateTimePath<java.util.Date> finalDate = createDateTime("finalDate", java.util.Date.class);

    public final StringPath finalLevel = createString("finalLevel");

    public final StringPath finalName = createString("finalName");

    public final StringPath firRep = createString("firRep");

    public final StringPath id = createString("id");

    public final StringPath internBs = createString("internBs");

    public final StringPath internCode = createString("internCode");

    public final DateTimePath<java.util.Date> internDate = createDateTime("internDate", java.util.Date.class);

    public final StringPath internLevel = createString("internLevel");

    public final StringPath internName = createString("internName");

    public final StringPath khxySco = createString("khxySco");

    public final StringPath lastModifier = createString("lastModifier");

    public final DateTimePath<java.util.Date> lastModifydate = createDateTime("lastModifydate", java.util.Date.class);

    public final StringPath pd = createString("pd");

    public final StringPath proId = createString("proId");

    public final StringPath projectCode = createString("projectCode");

    public final StringPath projectName = createString("projectName");

    public final StringPath ratingStatus = createString("ratingStatus");

    public final StringPath ratingType = createString("ratingType");

    public final StringPath ratingVaild = createString("ratingVaild");

    public final StringPath sco = createString("sco");

    public final StringPath secRep = createString("secRep");

    public final ListPath<RatingDebtStep, QRatingDebtStep> steps = this.<RatingDebtStep, QRatingDebtStep>createList("steps", RatingDebtStep.class, QRatingDebtStep.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public final BooleanPath vaild = createBoolean("vaild");

    public final StringPath xjlSco = createString("xjlSco");

    public final StringPath zlwSco = createString("zlwSco");

    public final StringPath zxcsSco = createString("zxcsSco");

    public QDebtRating(String variable) {
        super(DebtRating.class, forVariable(variable));
    }

    public QDebtRating(Path<? extends DebtRating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDebtRating(PathMetadata metadata) {
        super(DebtRating.class, metadata);
    }

}

