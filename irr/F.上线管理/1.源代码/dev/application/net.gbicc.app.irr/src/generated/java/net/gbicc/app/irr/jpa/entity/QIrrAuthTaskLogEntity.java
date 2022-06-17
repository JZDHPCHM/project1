package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrAuthTaskLogEntity is a Querydsl query type for IrrAuthTaskLogEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrAuthTaskLogEntity extends EntityPathBase<IrrAuthTaskLogEntity> {

    private static final long serialVersionUID = 1223259206L;

    public static final QIrrAuthTaskLogEntity irrAuthTaskLogEntity = new QIrrAuthTaskLogEntity("irrAuthTaskLogEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath authId = createString("authId");

    public final StringPath authName = createString("authName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dealResult = createString("dealResult");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath taskCode = createString("taskCode");

    public final StringPath taskId = createString("taskId");

    public final StringPath taskName = createString("taskName");

    public final StringPath userId = createString("userId");

    public final StringPath userName = createString("userName");

    public QIrrAuthTaskLogEntity(String variable) {
        super(IrrAuthTaskLogEntity.class, forVariable(variable));
    }

    public QIrrAuthTaskLogEntity(Path<? extends IrrAuthTaskLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrAuthTaskLogEntity(PathMetadata metadata) {
        super(IrrAuthTaskLogEntity.class, metadata);
    }

}

