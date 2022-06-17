package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEquityPledgedEntity is a Querydsl query type for FbEquityPledgedEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEquityPledgedEntity extends EntityPathBase<FbEquityPledgedEntity> {

    private static final long serialVersionUID = -1658325334L;

    public static final QFbEquityPledgedEntity fbEquityPledgedEntity = new QFbEquityPledgedEntity("fbEquityPledgedEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath cancellationDate = createString("cancellationDate");

    public final StringPath cancellationReason = createString("cancellationReason");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath pledgedEquityAmount = createString("pledgedEquityAmount");

    public final StringPath pledgee = createString("pledgee");

    public final StringPath pledgor = createString("pledgor");

    public final StringPath qualityTarget = createString("qualityTarget");

    public final StringPath registrationDate = createString("registrationDate");

    public final StringPath registrationNumber = createString("registrationNumber");

    public final StringPath registrationTypes = createString("registrationTypes");

    public final StringPath releaseTime = createString("releaseTime");

    public final StringPath remark = createString("remark");

    public final StringPath status = createString("status");

    public QFbEquityPledgedEntity(String variable) {
        super(FbEquityPledgedEntity.class, forVariable(variable));
    }

    public QFbEquityPledgedEntity(Path<? extends FbEquityPledgedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEquityPledgedEntity(PathMetadata metadata) {
        super(FbEquityPledgedEntity.class, metadata);
    }

}

