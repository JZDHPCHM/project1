package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbExecutePersonRelatedEntity is a Querydsl query type for FbExecutePersonRelatedEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbExecutePersonRelatedEntity extends EntityPathBase<FbExecutePersonRelatedEntity> {

    private static final long serialVersionUID = -964040221L;

    public static final QFbExecutePersonRelatedEntity fbExecutePersonRelatedEntity = new QFbExecutePersonRelatedEntity("fbExecutePersonRelatedEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath executePersionId = createString("executePersionId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbExecutePersonRelatedEntity(String variable) {
        super(FbExecutePersonRelatedEntity.class, forVariable(variable));
    }

    public QFbExecutePersonRelatedEntity(Path<? extends FbExecutePersonRelatedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbExecutePersonRelatedEntity(PathMetadata metadata) {
        super(FbExecutePersonRelatedEntity.class, metadata);
    }

}

