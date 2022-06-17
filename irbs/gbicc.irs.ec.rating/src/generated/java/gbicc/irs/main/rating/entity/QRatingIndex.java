package gbicc.irs.main.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingIndex is a Querydsl query type for RatingIndex
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingIndex extends EntityPathBase<RatingIndex> {

    private static final long serialVersionUID = -1714176086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingIndex ratingIndex = new QRatingIndex("ratingIndex");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dxText = createString("dxText");

    public final StringPath fdmodel = createString("fdmodel");

    public final StringPath id = createString("id");

    public final StringPath indexCategory = createString("indexCategory");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexConfigId = createString("indexConfigId");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final NumberPath<java.math.BigDecimal> indexScore = createNumber("indexScore", java.math.BigDecimal.class);

    public final EnumPath<gbicc.irs.main.rating.support.RatingStepType> indexType = createEnum("indexType", gbicc.irs.main.rating.support.RatingStepType.class);

    public final StringPath indexValue = createString("indexValue");

    public final StringPath indexWeight = createString("indexWeight");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath level = createString("level");

    public final StringPath parentId = createString("parentId");

    public final StringPath quOptions = createString("quOptions");

    public final QRatingMainStep ratingStep;

    public QRatingIndex(String variable) {
        this(RatingIndex.class, forVariable(variable), INITS);
    }

    public QRatingIndex(Path<? extends RatingIndex> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingIndex(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingIndex(PathMetadata metadata, PathInits inits) {
        this(RatingIndex.class, metadata, inits);
    }

    public QRatingIndex(Class<? extends RatingIndex> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ratingStep = inits.isInitialized("ratingStep") ? new QRatingMainStep(forProperty("ratingStep"), inits.get("ratingStep")) : null;
    }

}

