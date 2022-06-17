package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocParagraphEntity is a Querydsl query type for FbJudgeDocParagraphEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocParagraphEntity extends EntityPathBase<FbJudgeDocParagraphEntity> {

    private static final long serialVersionUID = 1675140465L;

    public static final QFbJudgeDocParagraphEntity fbJudgeDocParagraphEntity = new QFbJudgeDocParagraphEntity("fbJudgeDocParagraphEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judgeDocId = createString("judgeDocId");

    public final StringPath label = createString("label");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbJudgeDocParagraphEntity(String variable) {
        super(FbJudgeDocParagraphEntity.class, forVariable(variable));
    }

    public QFbJudgeDocParagraphEntity(Path<? extends FbJudgeDocParagraphEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocParagraphEntity(PathMetadata metadata) {
        super(FbJudgeDocParagraphEntity.class, metadata);
    }

}

