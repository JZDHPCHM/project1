package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrTaskEntity is a Querydsl query type for IrrTaskEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrTaskEntity extends EntityPathBase<IrrTaskEntity> {

    private static final long serialVersionUID = -1983993220L;

    public static final QIrrTaskEntity irrTaskEntity = new QIrrTaskEntity("irrTaskEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final DateTimePath<java.util.Date> deadPlanDate = createDateTime("deadPlanDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskDesc = createString("taskDesc");

    public final StringPath taskName = createString("taskName");

    public final StringPath taskStatus = createString("taskStatus");

    public QIrrTaskEntity(String variable) {
        super(IrrTaskEntity.class, forVariable(variable));
    }

    public QIrrTaskEntity(Path<? extends IrrTaskEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrTaskEntity(PathMetadata metadata) {
        super(IrrTaskEntity.class, metadata);
    }

}

