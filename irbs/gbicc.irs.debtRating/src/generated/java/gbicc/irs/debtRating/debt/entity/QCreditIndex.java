package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreditIndex is a Querydsl query type for CreditIndex
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCreditIndex extends EntityPathBase<CreditIndex> {

    private static final long serialVersionUID = -2043719889L;

    public static final QCreditIndex creditIndex = new QCreditIndex("creditIndex");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Integer> creditNumber = createNumber("creditNumber", Integer.class);

    public final StringPath creditType = createString("creditType");

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

    public final StringPath ratingId = createString("ratingId");

    public final StringPath ratingStatus = createString("ratingStatus");

    public QCreditIndex(String variable) {
        super(CreditIndex.class, forVariable(variable));
    }

    public QCreditIndex(Path<? extends CreditIndex> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreditIndex(PathMetadata metadata) {
        super(CreditIndex.class, metadata);
    }

}

