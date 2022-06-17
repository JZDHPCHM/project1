package gbicc.irs.assetsRating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingAssetsStep is a Querydsl query type for RatingAssetsStep
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingAssetsStep extends EntityPathBase<RatingAssetsStep> {

    private static final long serialVersionUID = 1262312813L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingAssetsStep ratingAssetsStep = new QRatingAssetsStep("ratingAssetsStep");

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

    public final QRatingAssetsStep lastStep;

    public final StringPath modelId = createString("modelId");

    public final QRatingAssetsStep nextStep;

    public final QAssetsRating ratingMain;

    public final NumberPath<Integer> sno = createNumber("sno", Integer.class);

    public final ListPath<AssetsIndex, QAssetsIndex> stepIndexs = this.<AssetsIndex, QAssetsIndex>createList("stepIndexs", AssetsIndex.class, QAssetsIndex.class, PathInits.DIRECT2);

    public final StringPath stepName = createString("stepName");

    public final StringPath stepResources = createString("stepResources");

    public final EnumPath<gbicc.irs.main.rating.support.RatingStepType> stepType = createEnum("stepType", gbicc.irs.main.rating.support.RatingStepType.class);

    public final StringPath vaild = createString("vaild");

    public QRatingAssetsStep(String variable) {
        this(RatingAssetsStep.class, forVariable(variable), INITS);
    }

    public QRatingAssetsStep(Path<? extends RatingAssetsStep> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingAssetsStep(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingAssetsStep(PathMetadata metadata, PathInits inits) {
        this(RatingAssetsStep.class, metadata, inits);
    }

    public QRatingAssetsStep(Class<? extends RatingAssetsStep> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lastStep = inits.isInitialized("lastStep") ? new QRatingAssetsStep(forProperty("lastStep"), inits.get("lastStep")) : null;
        this.nextStep = inits.isInitialized("nextStep") ? new QRatingAssetsStep(forProperty("nextStep"), inits.get("nextStep")) : null;
        this.ratingMain = inits.isInitialized("ratingMain") ? new QAssetsRating(forProperty("ratingMain")) : null;
    }

}

