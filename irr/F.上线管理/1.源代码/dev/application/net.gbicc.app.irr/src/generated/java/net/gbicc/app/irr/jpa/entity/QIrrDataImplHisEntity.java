package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrDataImplHisEntity is a Querydsl query type for IrrDataImplHisEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrDataImplHisEntity extends EntityPathBase<IrrDataImplHisEntity> {

    private static final long serialVersionUID = 2066810135L;

    public static final QIrrDataImplHisEntity irrDataImplHisEntity = new QIrrDataImplHisEntity("irrDataImplHisEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final NumberPath<Double> indexLine = createNumber("indexLine", Double.class);

    public final StringPath indexName = createString("indexName");

    public final StringPath indexResult = createString("indexResult");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgName = createString("orgName");

    public final StringPath projTypeCode = createString("projTypeCode");

    public final StringPath projTypeName = createString("projTypeName");

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath xbrlCode = createString("xbrlCode");

    public final StringPath xbrlName = createString("xbrlName");

    public QIrrDataImplHisEntity(String variable) {
        super(IrrDataImplHisEntity.class, forVariable(variable));
    }

    public QIrrDataImplHisEntity(Path<? extends IrrDataImplHisEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrDataImplHisEntity(PathMetadata metadata) {
        super(IrrDataImplHisEntity.class, metadata);
    }

}

