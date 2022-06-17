package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishResultEntity is a Querydsl query type for FbPunishResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishResultEntity extends EntityPathBase<FbPunishResultEntity> {

    private static final long serialVersionUID = 254365652L;

    public static final QFbPunishResultEntity fbPunishResultEntity = new QFbPunishResultEntity("fbPunishResultEntity");

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

    public final StringPath result = createString("result");

    public QFbPunishResultEntity(String variable) {
        super(FbPunishResultEntity.class, forVariable(variable));
    }

    public QFbPunishResultEntity(Path<? extends FbPunishResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishResultEntity(PathMetadata metadata) {
        super(FbPunishResultEntity.class, metadata);
    }

}

