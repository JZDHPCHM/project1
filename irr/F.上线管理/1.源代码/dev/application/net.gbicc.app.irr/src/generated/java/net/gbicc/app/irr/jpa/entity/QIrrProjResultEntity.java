package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrProjResultEntity is a Querydsl query type for IrrProjResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrProjResultEntity extends EntityPathBase<IrrProjResultEntity> {

    private static final long serialVersionUID = -1664260079L;

    public static final QIrrProjResultEntity irrProjResultEntity = new QIrrProjResultEntity("irrProjResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<java.math.BigDecimal> branchScore = createNumber("branchScore", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final NumberPath<java.math.BigDecimal> headScore = createNumber("headScore", java.math.BigDecimal.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath resultFlag = createString("resultFlag");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public final NumberPath<java.math.BigDecimal> totalScore = createNumber("totalScore", java.math.BigDecimal.class);

    public final StringPath typeCode = createString("typeCode");

    public final StringPath typeId = createString("typeId");

    public final StringPath typeName = createString("typeName");

    public QIrrProjResultEntity(String variable) {
        super(IrrProjResultEntity.class, forVariable(variable));
    }

    public QIrrProjResultEntity(Path<? extends IrrProjResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrProjResultEntity(PathMetadata metadata) {
        super(IrrProjResultEntity.class, metadata);
    }

}

