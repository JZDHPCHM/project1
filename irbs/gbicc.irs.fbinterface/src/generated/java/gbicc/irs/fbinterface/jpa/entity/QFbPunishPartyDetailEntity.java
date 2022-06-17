package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishPartyDetailEntity is a Querydsl query type for FbPunishPartyDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishPartyDetailEntity extends EntityPathBase<FbPunishPartyDetailEntity> {

    private static final long serialVersionUID = -559369050L;

    public static final QFbPunishPartyDetailEntity fbPunishPartyDetailEntity = new QFbPunishPartyDetailEntity("fbPunishPartyDetailEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath amount = createString("amount");

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

    public final StringPath punishDate = createString("punishDate");

    public final StringPath punishId = createString("punishId");

    public final StringPath punishParty = createString("punishParty");

    public final StringPath type = createString("type");

    public QFbPunishPartyDetailEntity(String variable) {
        super(FbPunishPartyDetailEntity.class, forVariable(variable));
    }

    public QFbPunishPartyDetailEntity(Path<? extends FbPunishPartyDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishPartyDetailEntity(PathMetadata metadata) {
        super(FbPunishPartyDetailEntity.class, metadata);
    }

}

