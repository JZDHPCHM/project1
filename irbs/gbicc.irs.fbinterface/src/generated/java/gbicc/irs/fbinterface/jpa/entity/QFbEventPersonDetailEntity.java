package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventPersonDetailEntity is a Querydsl query type for FbEventPersonDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventPersonDetailEntity extends EntityPathBase<FbEventPersonDetailEntity> {

    private static final long serialVersionUID = -212419804L;

    public static final QFbEventPersonDetailEntity fbEventPersonDetailEntity = new QFbEventPersonDetailEntity("fbEventPersonDetailEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    public final NumberPath<java.math.BigDecimal> count = createNumber("count", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventCompanyId = createString("eventCompanyId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbEventPersonDetailEntity(String variable) {
        super(FbEventPersonDetailEntity.class, forVariable(variable));
    }

    public QFbEventPersonDetailEntity(Path<? extends FbEventPersonDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventPersonDetailEntity(PathMetadata metadata) {
        super(FbEventPersonDetailEntity.class, metadata);
    }

}

