package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrTotalScoreResultEntity is a Querydsl query type for IrrTotalScoreResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrTotalScoreResultEntity extends EntityPathBase<IrrTotalScoreResultEntity> {

    private static final long serialVersionUID = 493132258L;

    public static final QIrrTotalScoreResultEntity irrTotalScoreResultEntity = new QIrrTotalScoreResultEntity("irrTotalScoreResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgId = createString("orgId");

    public final StringPath projCode = createString("projCode");

    public final StringPath projId = createString("projId");

    public final StringPath projName = createString("projName");

    public final StringPath projResult = createString("projResult");

    public final StringPath resultFlag = createString("resultFlag");

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QIrrTotalScoreResultEntity(String variable) {
        super(IrrTotalScoreResultEntity.class, forVariable(variable));
    }

    public QIrrTotalScoreResultEntity(Path<? extends IrrTotalScoreResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrTotalScoreResultEntity(PathMetadata metadata) {
        super(IrrTotalScoreResultEntity.class, metadata);
    }

}

