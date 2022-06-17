package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrIndexOptionEntity is a Querydsl query type for IrrIndexOptionEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrIndexOptionEntity extends EntityPathBase<IrrIndexOptionEntity> {

    private static final long serialVersionUID = 1976414070L;

    public static final QIrrIndexOptionEntity irrIndexOptionEntity = new QIrrIndexOptionEntity("irrIndexOptionEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath optionName = createString("optionName");

    public final NumberPath<java.math.BigDecimal> optionPoints = createNumber("optionPoints", java.math.BigDecimal.class);

    public final StringPath optionRemark = createString("optionRemark");

    public final NumberPath<java.math.BigDecimal> optionScore = createNumber("optionScore", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> optionSort = createNumber("optionSort", java.math.BigDecimal.class);

    public QIrrIndexOptionEntity(String variable) {
        super(IrrIndexOptionEntity.class, forVariable(variable));
    }

    public QIrrIndexOptionEntity(Path<? extends IrrIndexOptionEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrIndexOptionEntity(PathMetadata metadata) {
        super(IrrIndexOptionEntity.class, metadata);
    }

}

