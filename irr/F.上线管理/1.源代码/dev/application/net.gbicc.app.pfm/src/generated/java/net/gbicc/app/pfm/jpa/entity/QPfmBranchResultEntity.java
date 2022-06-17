package net.gbicc.app.pfm.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPfmBranchResultEntity is a Querydsl query type for PfmBranchResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPfmBranchResultEntity extends EntityPathBase<PfmBranchResultEntity> {

    private static final long serialVersionUID = 350390294L;

    public static final QPfmBranchResultEntity pfmBranchResultEntity = new QPfmBranchResultEntity("pfmBranchResultEntity");

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

    public final StringPath indexScore = createString("indexScore");

    public final NumberPath<java.math.BigDecimal> indexWeight = createNumber("indexWeight", java.math.BigDecimal.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgId = createString("orgId");

    public final StringPath orgName = createString("orgName");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final NumberPath<java.math.BigDecimal> standValue = createNumber("standValue", java.math.BigDecimal.class);

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QPfmBranchResultEntity(String variable) {
        super(PfmBranchResultEntity.class, forVariable(variable));
    }

    public QPfmBranchResultEntity(Path<? extends PfmBranchResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPfmBranchResultEntity(PathMetadata metadata) {
        super(PfmBranchResultEntity.class, metadata);
    }

}

