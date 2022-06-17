package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbFaithExecutePersonEntity is a Querydsl query type for FbFaithExecutePersonEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbFaithExecutePersonEntity extends EntityPathBase<FbFaithExecutePersonEntity> {

    private static final long serialVersionUID = -1370083574L;

    public static final QFbFaithExecutePersonEntity fbFaithExecutePersonEntity = new QFbFaithExecutePersonEntity("fbFaithExecutePersonEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath caseDate = createString("caseDate");

    public final StringPath caseNumber = createString("caseNumber");

    public final StringPath chargePerson = createString("chargePerson");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath decisionAuthority = createString("decisionAuthority");

    public final StringPath effectObligations = createString("effectObligations");

    public final StringPath executeDetail = createString("executeDetail");

    public final StringPath executionCourt = createString("executionCourt");

    public final StringPath executionName = createString("executionName");

    public final StringPath executionNumber = createString("executionNumber");

    public final StringPath executionResult = createString("executionResult");

    public final StringPath havePerformed = createString("havePerformed");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath noPerformed = createString("noPerformed");

    public final StringPath province = createString("province");

    public final StringPath publicDate = createString("publicDate");

    public QFbFaithExecutePersonEntity(String variable) {
        super(FbFaithExecutePersonEntity.class, forVariable(variable));
    }

    public QFbFaithExecutePersonEntity(Path<? extends FbFaithExecutePersonEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbFaithExecutePersonEntity(PathMetadata metadata) {
        super(FbFaithExecutePersonEntity.class, metadata);
    }

}

