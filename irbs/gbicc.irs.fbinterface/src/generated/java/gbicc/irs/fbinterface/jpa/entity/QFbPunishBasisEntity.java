package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishBasisEntity is a Querydsl query type for FbPunishBasisEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishBasisEntity extends EntityPathBase<FbPunishBasisEntity> {

    private static final long serialVersionUID = 2037317069L;

    public static final QFbPunishBasisEntity fbPunishBasisEntity = new QFbPunishBasisEntity("fbPunishBasisEntity");

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

    public final StringPath punishId = createString("punishId");

    public final StringPath reason = createString("reason");

    public QFbPunishBasisEntity(String variable) {
        super(FbPunishBasisEntity.class, forVariable(variable));
    }

    public QFbPunishBasisEntity(Path<? extends FbPunishBasisEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishBasisEntity(PathMetadata metadata) {
        super(FbPunishBasisEntity.class, metadata);
    }

}

