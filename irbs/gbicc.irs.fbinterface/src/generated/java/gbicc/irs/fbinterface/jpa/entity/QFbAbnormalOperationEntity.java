package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAbnormalOperationEntity is a Querydsl query type for FbAbnormalOperationEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAbnormalOperationEntity extends EntityPathBase<FbAbnormalOperationEntity> {

    private static final long serialVersionUID = 607579683L;

    public static final QFbAbnormalOperationEntity fbAbnormalOperationEntity = new QFbAbnormalOperationEntity("fbAbnormalOperationEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath abnormalDate = createString("abnormalDate");

    public final StringPath abnormalReason = createString("abnormalReason");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath decisionAuthority = createString("decisionAuthority");

    public final StringPath decisionAuthorityAbnormal = createString("decisionAuthorityAbnormal");

    public final StringPath decisionAuthorityRemove = createString("decisionAuthorityRemove");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath removeDate = createString("removeDate");

    public final StringPath removeReason = createString("removeReason");

    public QFbAbnormalOperationEntity(String variable) {
        super(FbAbnormalOperationEntity.class, forVariable(variable));
    }

    public QFbAbnormalOperationEntity(Path<? extends FbAbnormalOperationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAbnormalOperationEntity(PathMetadata metadata) {
        super(FbAbnormalOperationEntity.class, metadata);
    }

}

