package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbTrialProcessEntity is a Querydsl query type for FbTrialProcessEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbTrialProcessEntity extends EntityPathBase<FbTrialProcessEntity> {

    private static final long serialVersionUID = 1340939195L;

    public static final QFbTrialProcessEntity fbTrialProcessEntity = new QFbTrialProcessEntity("fbTrialProcessEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseNo = createString("caseNo");

    public final StringPath caseProgress = createString("caseProgress");

    public final StringPath caseReason = createString("caseReason");

    public final StringPath caseType = createString("caseType");

    public final StringPath collegialMember = createString("collegialMember");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath filingDate = createString("filingDate");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath noticePeople = createString("noticePeople");

    public final StringPath noticeType = createString("noticeType");

    public final StringPath presidingJudge = createString("presidingJudge");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath putOnRecordDate = createString("putOnRecordDate");

    public final StringPath putOnRecordTime = createString("putOnRecordTime");

    public final StringPath settlementDate = createString("settlementDate");

    public final StringPath settlementWay = createString("settlementWay");

    public final StringPath trialDate = createString("trialDate");

    public final StringPath trialJudge = createString("trialJudge");

    public final StringPath trialProcedure = createString("trialProcedure");

    public final NumberPath<java.math.BigDecimal> underlyAmount = createNumber("underlyAmount", java.math.BigDecimal.class);

    public final StringPath undertakeDepart = createString("undertakeDepart");

    public final StringPath wordNumber = createString("wordNumber");

    public QFbTrialProcessEntity(String variable) {
        super(FbTrialProcessEntity.class, forVariable(variable));
    }

    public QFbTrialProcessEntity(Path<? extends FbTrialProcessEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbTrialProcessEntity(PathMetadata metadata) {
        super(FbTrialProcessEntity.class, metadata);
    }

}

