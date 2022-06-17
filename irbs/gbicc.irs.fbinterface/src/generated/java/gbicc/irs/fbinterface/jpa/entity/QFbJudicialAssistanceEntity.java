package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudicialAssistanceEntity is a Querydsl query type for FbJudicialAssistanceEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudicialAssistanceEntity extends EntityPathBase<FbJudicialAssistanceEntity> {

    private static final long serialVersionUID = 1297006139L;

    public static final QFbJudicialAssistanceEntity fbJudicialAssistanceEntity = new QFbJudicialAssistanceEntity("fbJudicialAssistanceEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath certificateType = createString("certificateType");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath enforcementNoticeNumber = createString("enforcementNoticeNumber");

    public final StringPath equityAmount = createString("equityAmount");

    public final StringPath equityEnterpriseName = createString("equityEnterpriseName");

    public final StringPath executePerson = createString("executePerson");

    public final StringPath executionCourt = createString("executionCourt");

    public final StringPath executionMatters = createString("executionMatters");

    public final StringPath executionOrderNumber = createString("executionOrderNumber");

    public final StringPath freezePeriodFrom = createString("freezePeriodFrom");

    public final StringPath freezePeriodTo = createString("freezePeriodTo");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath liftFreezePeriod = createString("liftFreezePeriod");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath receiveParty = createString("receiveParty");

    public final StringPath renewFreezePeriodFrom = createString("renewFreezePeriodFrom");

    public final StringPath renewFreezePeriodTo = createString("renewFreezePeriodTo");

    public QFbJudicialAssistanceEntity(String variable) {
        super(FbJudicialAssistanceEntity.class, forVariable(variable));
    }

    public QFbJudicialAssistanceEntity(Path<? extends FbJudicialAssistanceEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudicialAssistanceEntity(PathMetadata metadata) {
        super(FbJudicialAssistanceEntity.class, metadata);
    }

}

