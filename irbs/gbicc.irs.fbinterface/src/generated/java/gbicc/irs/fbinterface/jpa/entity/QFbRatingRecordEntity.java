package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbRatingRecordEntity is a Querydsl query type for FbRatingRecordEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbRatingRecordEntity extends EntityPathBase<FbRatingRecordEntity> {

    private static final long serialVersionUID = -1622366768L;

    public static final QFbRatingRecordEntity fbRatingRecordEntity = new QFbRatingRecordEntity("fbRatingRecordEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath issuerName = createString("issuerName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath publicDate = createString("publicDate");

    public final StringPath rating = createString("rating");

    public final StringPath ratingCompany = createString("ratingCompany");

    public final StringPath ratingDate = createString("ratingDate");

    public final StringPath ratingObject = createString("ratingObject");

    public final StringPath ratingType = createString("ratingType");

    public QFbRatingRecordEntity(String variable) {
        super(FbRatingRecordEntity.class, forVariable(variable));
    }

    public QFbRatingRecordEntity(Path<? extends FbRatingRecordEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbRatingRecordEntity(PathMetadata metadata) {
        super(FbRatingRecordEntity.class, metadata);
    }

}

