package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatingDebtIndex is a Querydsl query type for RatingDebtIndex
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingDebtIndex extends EntityPathBase<RatingDebtIndex> {

    private static final long serialVersionUID = 951189944L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatingDebtIndex ratingDebtIndex = new QRatingDebtIndex("ratingDebtIndex");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dxText = createString("dxText");

    public final StringPath id = createString("id");

    public final StringPath indexCategory = createString("indexCategory");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexConfigId = createString("indexConfigId");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final NumberPath<java.math.BigDecimal> indexScore = createNumber("indexScore", java.math.BigDecimal.class);

    public final StringPath indexType = createString("indexType");

    public final StringPath indexValue = createString("indexValue");

    public final StringPath indexWeight = createString("indexWeight");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath level = createString("level");

    public final StringPath parentId = createString("parentId");

    public final StringPath quOptions = createString("quOptions");

    public final QRatingDebtStep ratingStep;

    public QRatingDebtIndex(String variable) {
        this(RatingDebtIndex.class, forVariable(variable), INITS);
    }

    public QRatingDebtIndex(Path<? extends RatingDebtIndex> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatingDebtIndex(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatingDebtIndex(PathMetadata metadata, PathInits inits) {
        this(RatingDebtIndex.class, metadata, inits);
    }

    public QRatingDebtIndex(Class<? extends RatingDebtIndex> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ratingStep = inits.isInitialized("ratingStep") ? new QRatingDebtStep(forProperty("ratingStep"), inits.get("ratingStep")) : null;
    }

}

