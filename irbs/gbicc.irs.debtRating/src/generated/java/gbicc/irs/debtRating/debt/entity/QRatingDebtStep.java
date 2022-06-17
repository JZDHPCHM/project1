package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingDebtStep is a Querydsl query type for RatingDebtStep
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingDebtStep extends EntityPathBase<RatingDebtStep> {

    private static final long serialVersionUID = 1416460582L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingDebtStep ratingDebtStep = new QRatingDebtStep("ratingDebtStep");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final QRatingDebtStep lastStep;

    public final StringPath modelId = createString("modelId");

    public final QRatingDebtStep nextStep;

    public final QDebtRating ratingMain;

    public final NumberPath<Integer> sno = createNumber("sno", Integer.class);

    public final ListPath<RatingDebtIndex, QRatingDebtIndex> stepIndexs = this.<RatingDebtIndex, QRatingDebtIndex>createList("stepIndexs", RatingDebtIndex.class, QRatingDebtIndex.class, PathInits.DIRECT2);

    public final StringPath stepName = createString("stepName");

    public final StringPath stepResources = createString("stepResources");

    public final EnumPath<gbicc.irs.main.rating.support.RatingStepType> stepType = createEnum("stepType", gbicc.irs.main.rating.support.RatingStepType.class);

    public final StringPath vaild = createString("vaild");

    public QRatingDebtStep(String variable) {
        this(RatingDebtStep.class, forVariable(variable), INITS);
    }

    public QRatingDebtStep(Path<? extends RatingDebtStep> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingDebtStep(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingDebtStep(PathMetadata metadata, PathInits inits) {
        this(RatingDebtStep.class, metadata, inits);
    }

    public QRatingDebtStep(Class<? extends RatingDebtStep> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lastStep = inits.isInitialized("lastStep") ? new QRatingDebtStep(forProperty("lastStep"), inits.get("lastStep")) : null;
        this.nextStep = inits.isInitialized("nextStep") ? new QRatingDebtStep(forProperty("nextStep"), inits.get("nextStep")) : null;
        this.ratingMain = inits.isInitialized("ratingMain") ? new QDebtRating(forProperty("ratingMain")) : null;
    }

}

