package gbicc.irs.main.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMainRating is a Querydsl query type for MainRating
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMainRating extends EntityPathBase<MainRating> {

    private static final long serialVersionUID = -1034366047L;

    public static final QMainRating mainRating = new QMainRating("mainRating");

    public final StringPath actualRateSubjectId = createString("actualRateSubjectId");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath creator = createString("creator");

    public final StringPath currentStepId = createString("currentStepId");

    public final StringPath custCode = createString("custCode");

    public final StringPath custName = createString("custName");

    public final StringPath fDate = createString("fDate");

    public final StringPath fdVersion = createString("fdVersion");

    public final StringPath finalAdvice = createString("finalAdvice");

    public final StringPath finalCode = createString("finalCode");

    public final DateTimePath<java.util.Date> finalDate = createDateTime("finalDate", java.util.Date.class);

    public final StringPath finalLevel = createString("finalLevel");

    public final StringPath finalName = createString("finalName");

    public final StringPath firRep = createString("firRep");

    public final StringPath id = createString("id");

    public final StringPath initSco = createString("initSco");

    public final StringPath internCode = createString("internCode");

    public final DateTimePath<java.util.Date> internDate = createDateTime("internDate", java.util.Date.class);

    public final StringPath internLevel = createString("internLevel");

    public final StringPath internName = createString("internName");

    public final StringPath lastModifier = createString("lastModifier");

    public final DateTimePath<java.util.Date> lastModifydate = createDateTime("lastModifydate", java.util.Date.class);

    public final StringPath pd = createString("pd");

    public final StringPath qualSco = createString("qualSco");

    public final StringPath quanSco = createString("quanSco");

    public final StringPath ratingStatus = createString("ratingStatus");

    public final StringPath ratingType = createString("ratingType");

    public final StringPath ratingVaild = createString("ratingVaild");

    public final StringPath reviewerOpinion = createString("reviewerOpinion");

    public final StringPath sco = createString("sco");

    public final StringPath secRep = createString("secRep");

    public final ListPath<RatingMainStep, QRatingMainStep> steps = this.<RatingMainStep, QRatingMainStep>createList("steps", RatingMainStep.class, QRatingMainStep.class, PathInits.DIRECT2);

    public final StringPath thiRep = createString("thiRep");

    public final StringPath trackType = createString("trackType");

    public final StringPath treatN = createString("treatN");

    public final StringPath type = createString("type");

    public final BooleanPath vaild = createBoolean("vaild");

    public QMainRating(String variable) {
        super(MainRating.class, forVariable(variable));
    }

    public QMainRating(Path<? extends MainRating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMainRating(PathMetadata metadata) {
        super(MainRating.class, metadata);
    }

}

