package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrUploadResultEntity is a Querydsl query type for IrrUploadResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrUploadResultEntity extends EntityPathBase<IrrUploadResultEntity> {

    private static final long serialVersionUID = -175939659L;

    public static final QIrrUploadResultEntity irrUploadResultEntity = new QIrrUploadResultEntity("irrUploadResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath collOrgId = createString("collOrgId");

    public final StringPath collOrgName = createString("collOrgName");

    public final StringPath collUserName = createString("collUserName");

    public final StringPath collWay = createString("collWay");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath dataDesc = createString("dataDesc");

    public final NumberPath<java.math.BigDecimal> dataVali = createNumber("dataVali", java.math.BigDecimal.class);

    public final StringPath id = createString("id");

    public final StringPath isCommit = createString("isCommit");

    public final StringPath isFillReason = createString("isFillReason");

    public final StringPath isHandChange = createString("isHandChange");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath remark = createString("remark");

    public final StringPath resultTypeName = createString("resultTypeName");

    public final StringPath splitCode = createString("splitCode");

    public final StringPath splitId = createString("splitId");

    public final StringPath splitName = createString("splitName");

    public final StringPath splitResultQ1 = createString("splitResultQ1");

    public final StringPath splitResultQ2 = createString("splitResultQ2");

    public final StringPath splitScoreQ1 = createString("splitScoreQ1");

    public final StringPath splitScoreQ2 = createString("splitScoreQ2");

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QIrrUploadResultEntity(String variable) {
        super(IrrUploadResultEntity.class, forVariable(variable));
    }

    public QIrrUploadResultEntity(Path<? extends IrrUploadResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrUploadResultEntity(PathMetadata metadata) {
        super(IrrUploadResultEntity.class, metadata);
    }

}

