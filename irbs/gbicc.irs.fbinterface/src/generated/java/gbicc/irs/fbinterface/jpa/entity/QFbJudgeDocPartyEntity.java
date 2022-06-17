package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocPartyEntity is a Querydsl query type for FbJudgeDocPartyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocPartyEntity extends EntityPathBase<FbJudgeDocPartyEntity> {

    private static final long serialVersionUID = -476629783L;

    public static final QFbJudgeDocPartyEntity fbJudgeDocPartyEntity = new QFbJudgeDocPartyEntity("fbJudgeDocPartyEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judgeDocId = createString("judgeDocId");

    public final StringPath judgeResult = createString("judgeResult");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public final StringPath organizationName = createString("organizationName");

    public final StringPath type = createString("type");

    public QFbJudgeDocPartyEntity(String variable) {
        super(FbJudgeDocPartyEntity.class, forVariable(variable));
    }

    public QFbJudgeDocPartyEntity(Path<? extends FbJudgeDocPartyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocPartyEntity(PathMetadata metadata) {
        super(FbJudgeDocPartyEntity.class, metadata);
    }

}

