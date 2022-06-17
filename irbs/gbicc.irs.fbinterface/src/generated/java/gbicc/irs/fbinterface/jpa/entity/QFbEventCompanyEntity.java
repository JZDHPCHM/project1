package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventCompanyEntity is a Querydsl query type for FbEventCompanyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventCompanyEntity extends EntityPathBase<FbEventCompanyEntity> {

    private static final long serialVersionUID = 368470597L;

    public static final QFbEventCompanyEntity fbEventCompanyEntity = new QFbEventCompanyEntity("fbEventCompanyEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbEventCompanyEntity(String variable) {
        super(FbEventCompanyEntity.class, forVariable(variable));
    }

    public QFbEventCompanyEntity(Path<? extends FbEventCompanyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventCompanyEntity(PathMetadata metadata) {
        super(FbEventCompanyEntity.class, metadata);
    }

}

