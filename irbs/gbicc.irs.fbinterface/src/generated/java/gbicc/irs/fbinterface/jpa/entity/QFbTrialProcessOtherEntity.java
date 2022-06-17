package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbTrialProcessOtherEntity is a Querydsl query type for FbTrialProcessOtherEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbTrialProcessOtherEntity extends EntityPathBase<FbTrialProcessOtherEntity> {

    private static final long serialVersionUID = 701477435L;

    public static final QFbTrialProcessOtherEntity fbTrialProcessOtherEntity = new QFbTrialProcessOtherEntity("fbTrialProcessOtherEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public final StringPath trialProcessId = createString("trialProcessId");

    public final StringPath trialProcessPartyId = createString("trialProcessPartyId");

    public QFbTrialProcessOtherEntity(String variable) {
        super(FbTrialProcessOtherEntity.class, forVariable(variable));
    }

    public QFbTrialProcessOtherEntity(Path<? extends FbTrialProcessOtherEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbTrialProcessOtherEntity(PathMetadata metadata) {
        super(FbTrialProcessOtherEntity.class, metadata);
    }

}

