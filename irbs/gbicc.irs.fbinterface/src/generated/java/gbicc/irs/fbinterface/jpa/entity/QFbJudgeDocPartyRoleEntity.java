package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbJudgeDocPartyRoleEntity is a Querydsl query type for FbJudgeDocPartyRoleEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbJudgeDocPartyRoleEntity extends EntityPathBase<FbJudgeDocPartyRoleEntity> {

    private static final long serialVersionUID = -1032520193L;

    public static final QFbJudgeDocPartyRoleEntity fbJudgeDocPartyRoleEntity = new QFbJudgeDocPartyRoleEntity("fbJudgeDocPartyRoleEntity");

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

    public final StringPath role = createString("role");

    public QFbJudgeDocPartyRoleEntity(String variable) {
        super(FbJudgeDocPartyRoleEntity.class, forVariable(variable));
    }

    public QFbJudgeDocPartyRoleEntity(Path<? extends FbJudgeDocPartyRoleEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbJudgeDocPartyRoleEntity(PathMetadata metadata) {
        super(FbJudgeDocPartyRoleEntity.class, metadata);
    }

}

