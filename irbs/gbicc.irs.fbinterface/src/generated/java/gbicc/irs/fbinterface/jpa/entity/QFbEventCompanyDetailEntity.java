package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventCompanyDetailEntity is a Querydsl query type for FbEventCompanyDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventCompanyDetailEntity extends EntityPathBase<FbEventCompanyDetailEntity> {

    private static final long serialVersionUID = 1272077430L;

    public static final QFbEventCompanyDetailEntity fbEventCompanyDetailEntity = new QFbEventCompanyDetailEntity("fbEventCompanyDetailEntity");

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

    public QFbEventCompanyDetailEntity(String variable) {
        super(FbEventCompanyDetailEntity.class, forVariable(variable));
    }

    public QFbEventCompanyDetailEntity(Path<? extends FbEventCompanyDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventCompanyDetailEntity(PathMetadata metadata) {
        super(FbEventCompanyDetailEntity.class, metadata);
    }

}

