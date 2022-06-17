package gbicc.irs.assetsRating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAssetsIndex is a Querydsl query type for AssetsIndex
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAssetsIndex extends EntityPathBase<AssetsIndex> {

    private static final long serialVersionUID = 1340008302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAssetsIndex assetsIndex = new QAssetsIndex("assetsIndex");

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

    public final QRatingAssetsStep ratingStep;

    public QAssetsIndex(String variable) {
        this(AssetsIndex.class, forVariable(variable), INITS);
    }

    public QAssetsIndex(Path<? extends AssetsIndex> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAssetsIndex(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAssetsIndex(PathMetadata metadata, PathInits inits) {
        this(AssetsIndex.class, metadata, inits);
    }

    public QAssetsIndex(Class<? extends AssetsIndex> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ratingStep = inits.isInitialized("ratingStep") ? new QRatingAssetsStep(forProperty("ratingStep"), inits.get("ratingStep")) : null;
    }

}

