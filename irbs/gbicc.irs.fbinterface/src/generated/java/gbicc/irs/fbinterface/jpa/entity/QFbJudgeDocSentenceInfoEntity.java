package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocSentenceInfoEntity is a Querydsl query type for FbJudgeDocSentenceInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocSentenceInfoEntity extends EntityPathBase<FbJudgeDocSentenceInfoEntity> {

    private static final long serialVersionUID = -1163596172L;

    public static final QFbJudgeDocSentenceInfoEntity fbJudgeDocSentenceInfoEntity = new QFbJudgeDocSentenceInfoEntity("fbJudgeDocSentenceInfoEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judgeDocId = createString("judgeDocId");

    public final StringPath judgeDocSentenceId = createString("judgeDocSentenceId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbJudgeDocSentenceInfoEntity(String variable) {
        super(FbJudgeDocSentenceInfoEntity.class, forVariable(variable));
    }

    public QFbJudgeDocSentenceInfoEntity(Path<? extends FbJudgeDocSentenceInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocSentenceInfoEntity(PathMetadata metadata) {
        super(FbJudgeDocSentenceInfoEntity.class, metadata);
    }

}

