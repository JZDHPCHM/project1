package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocEntity is a Querydsl query type for FbJudgeDocEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocEntity extends EntityPathBase<FbJudgeDocEntity> {

    private static final long serialVersionUID = 1174422883L;

    public static final QFbJudgeDocEntity fbJudgeDocEntity = new QFbJudgeDocEntity("fbJudgeDocEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseNo = createString("caseNo");

    public final StringPath caseReason = createString("caseReason");

    public final StringPath caseType = createString("caseType");

    public final StringPath companyId = createString("companyId");

    public final StringPath court = createString("court");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath documentType = createString("documentType");

    public final StringPath id = createString("id");

    public final StringPath judge = createString("judge");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalFee = createString("legalFee");

    public final StringPath notOpenReason = createString("notOpenReason");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath sentenceDate = createString("sentenceDate");

    public final StringPath sentenceTotalAmount = createString("sentenceTotalAmount");

    public final StringPath settlementDate = createString("settlementDate");

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QFbJudgeDocEntity(String variable) {
        super(FbJudgeDocEntity.class, forVariable(variable));
    }

    public QFbJudgeDocEntity(Path<? extends FbJudgeDocEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocEntity(PathMetadata metadata) {
        super(FbJudgeDocEntity.class, metadata);
    }

}

