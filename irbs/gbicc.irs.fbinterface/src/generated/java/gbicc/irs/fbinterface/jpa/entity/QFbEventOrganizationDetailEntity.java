package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventOrganizationDetailEntity is a Querydsl query type for FbEventOrganizationDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventOrganizationDetailEntity extends EntityPathBase<FbEventOrganizationDetailEntity> {

    private static final long serialVersionUID = 92524482L;

    public static final QFbEventOrganizationDetailEntity fbEventOrganizationDetailEntity = new QFbEventOrganizationDetailEntity("fbEventOrganizationDetailEntity");

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

    public QFbEventOrganizationDetailEntity(String variable) {
        super(FbEventOrganizationDetailEntity.class, forVariable(variable));
    }

    public QFbEventOrganizationDetailEntity(Path<? extends FbEventOrganizationDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventOrganizationDetailEntity(PathMetadata metadata) {
        super(FbEventOrganizationDetailEntity.class, metadata);
    }

}

