package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocPartyOtherEntity is a Querydsl query type for FbJudgeDocPartyOtherEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocPartyOtherEntity extends EntityPathBase<FbJudgeDocPartyOtherEntity> {

    private static final long serialVersionUID = 1208755661L;

    public static final QFbJudgeDocPartyOtherEntity fbJudgeDocPartyOtherEntity = new QFbJudgeDocPartyOtherEntity("fbJudgeDocPartyOtherEntity");

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

    public QFbJudgeDocPartyOtherEntity(String variable) {
        super(FbJudgeDocPartyOtherEntity.class, forVariable(variable));
    }

    public QFbJudgeDocPartyOtherEntity(Path<? extends FbJudgeDocPartyOtherEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocPartyOtherEntity(PathMetadata metadata) {
        super(FbJudgeDocPartyOtherEntity.class, metadata);
    }

}

