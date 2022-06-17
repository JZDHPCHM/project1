package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocPartyLegalEntity is a Querydsl query type for FbJudgeDocPartyLegalEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocPartyLegalEntity extends EntityPathBase<FbJudgeDocPartyLegalEntity> {

    private static final long serialVersionUID = -941496650L;

    public static final QFbJudgeDocPartyLegalEntity fbJudgeDocPartyLegalEntity = new QFbJudgeDocPartyLegalEntity("fbJudgeDocPartyLegalEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judgeDocId = createString("judgeDocId");

    public final StringPath judgeDocPartyId = createString("judgeDocPartyId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbJudgeDocPartyLegalEntity(String variable) {
        super(FbJudgeDocPartyLegalEntity.class, forVariable(variable));
    }

    public QFbJudgeDocPartyLegalEntity(Path<? extends FbJudgeDocPartyLegalEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocPartyLegalEntity(PathMetadata metadata) {
        super(FbJudgeDocPartyLegalEntity.class, metadata);
    }

}

