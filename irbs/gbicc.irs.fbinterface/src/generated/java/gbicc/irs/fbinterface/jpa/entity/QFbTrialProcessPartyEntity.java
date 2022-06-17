package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbTrialProcessPartyEntity is a Querydsl query type for FbTrialProcessPartyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbTrialProcessPartyEntity extends EntityPathBase<FbTrialProcessPartyEntity> {

    private static final long serialVersionUID = -261450351L;

    public static final QFbTrialProcessPartyEntity fbTrialProcessPartyEntity = new QFbTrialProcessPartyEntity("fbTrialProcessPartyEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalPerson = createString("legalPerson");

    public final StringPath name = createString("name");

    public final StringPath organizationName = createString("organizationName");

    public final StringPath role = createString("role");

    public final StringPath trialProcessId = createString("trialProcessId");

    public final StringPath type = createString("type");

    public QFbTrialProcessPartyEntity(String variable) {
        super(FbTrialProcessPartyEntity.class, forVariable(variable));
    }

    public QFbTrialProcessPartyEntity(Path<? extends FbTrialProcessPartyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbTrialProcessPartyEntity(PathMetadata metadata) {
        super(FbTrialProcessPartyEntity.class, metadata);
    }

}

