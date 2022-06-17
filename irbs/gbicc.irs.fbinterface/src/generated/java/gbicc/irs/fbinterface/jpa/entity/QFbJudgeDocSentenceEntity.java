package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocSentenceEntity is a Querydsl query type for FbJudgeDocSentenceEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocSentenceEntity extends EntityPathBase<FbJudgeDocSentenceEntity> {

    private static final long serialVersionUID = 356512166L;

    public static final QFbJudgeDocSentenceEntity fbJudgeDocSentenceEntity = new QFbJudgeDocSentenceEntity("fbJudgeDocSentenceEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath amount = createString("amount");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judgeDocId = createString("judgeDocId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbJudgeDocSentenceEntity(String variable) {
        super(FbJudgeDocSentenceEntity.class, forVariable(variable));
    }

    public QFbJudgeDocSentenceEntity(Path<? extends FbJudgeDocSentenceEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocSentenceEntity(PathMetadata metadata) {
        super(FbJudgeDocSentenceEntity.class, metadata);
    }

}

