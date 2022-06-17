package gbicc.irs.main.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingMainStep is a Querydsl query type for RatingMainStep
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingMainStep extends EntityPathBase<RatingMainStep> {

    private static final long serialVersionUID = 20502093L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingMainStep ratingMainStep = new QRatingMainStep("ratingMainStep");

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

    public final QRatingMainStep lastStep;

    public final StringPath modelId = createString("modelId");

    public final QRatingMainStep nextStep;

    public final QMainRating ratingMain;

    public final NumberPath<Integer> sno = createNumber("sno", Integer.class);

    public final ListPath<RatingIndex, QRatingIndex> stepIndexs = this.<RatingIndex, QRatingIndex>createList("stepIndexs", RatingIndex.class, QRatingIndex.class, PathInits.DIRECT2);

    public final StringPath stepName = createString("stepName");

    public final StringPath stepResources = createString("stepResources");

    public final EnumPath<gbicc.irs.main.rating.support.RatingStepType> stepType = createEnum("stepType", gbicc.irs.main.rating.support.RatingStepType.class);

    public final StringPath vaild = createString("vaild");

    public QRatingMainStep(String variable) {
        this(RatingMainStep.class, forVariable(variable), INITS);
    }

    public QRatingMainStep(Path<? extends RatingMainStep> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingMainStep(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingMainStep(PathMetadata metadata, PathInits inits) {
        this(RatingMainStep.class, metadata, inits);
    }

    public QRatingMainStep(Class<? extends RatingMainStep> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lastStep = inits.isInitialized("lastStep") ? new QRatingMainStep(forProperty("lastStep"), inits.get("lastStep")) : null;
        this.nextStep = inits.isInitialized("nextStep") ? new QRatingMainStep(forProperty("nextStep"), inits.get("nextStep")) : null;
        this.ratingMain = inits.isInitialized("ratingMain") ? new QMainRating(forProperty("ratingMain")) : null;
    }

}

