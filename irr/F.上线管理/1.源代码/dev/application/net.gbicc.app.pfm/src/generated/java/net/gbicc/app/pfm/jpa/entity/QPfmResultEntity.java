package net.gbicc.app.pfm.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPfmResultEntity is a Querydsl query type for PfmResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPfmResultEntity extends EntityPathBase<PfmResultEntity> {

    private static final long serialVersionUID = 2068822100L;

    public static final QPfmResultEntity pfmResultEntity = new QPfmResultEntity("pfmResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath pfmName = createString("pfmName");

    public final NumberPath<Double> pfmValue = createNumber("pfmValue", Double.class);

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QPfmResultEntity(String variable) {
        super(PfmResultEntity.class, forVariable(variable));
    }

    public QPfmResultEntity(Path<? extends PfmResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPfmResultEntity(PathMetadata metadata) {
        super(PfmResultEntity.class, metadata);
    }

}

