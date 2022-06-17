package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishPartyEntity is a Querydsl query type for FbPunishPartyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishPartyEntity extends EntityPathBase<FbPunishPartyEntity> {

    private static final long serialVersionUID = -1111579019L;

    public static final QFbPunishPartyEntity fbPunishPartyEntity = new QFbPunishPartyEntity("fbPunishPartyEntity");

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

    public final StringPath punishId = createString("punishId");

    public QFbPunishPartyEntity(String variable) {
        super(FbPunishPartyEntity.class, forVariable(variable));
    }

    public QFbPunishPartyEntity(Path<? extends FbPunishPartyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishPartyEntity(PathMetadata metadata) {
        super(FbPunishPartyEntity.class, metadata);
    }

}

