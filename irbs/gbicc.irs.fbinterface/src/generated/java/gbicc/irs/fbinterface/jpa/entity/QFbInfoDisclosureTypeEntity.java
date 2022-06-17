package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInfoDisclosureTypeEntity is a Querydsl query type for FbInfoDisclosureTypeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInfoDisclosureTypeEntity extends EntityPathBase<FbInfoDisclosureTypeEntity> {

    private static final long serialVersionUID = -1190694889L;

    public static final QFbInfoDisclosureTypeEntity fbInfoDisclosureTypeEntity = new QFbInfoDisclosureTypeEntity("fbInfoDisclosureTypeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath infoDisclosureId = createString("infoDisclosureId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath type = createString("type");

    public QFbInfoDisclosureTypeEntity(String variable) {
        super(FbInfoDisclosureTypeEntity.class, forVariable(variable));
    }

    public QFbInfoDisclosureTypeEntity(Path<? extends FbInfoDisclosureTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInfoDisclosureTypeEntity(PathMetadata metadata) {
        super(FbInfoDisclosureTypeEntity.class, metadata);
    }

}

