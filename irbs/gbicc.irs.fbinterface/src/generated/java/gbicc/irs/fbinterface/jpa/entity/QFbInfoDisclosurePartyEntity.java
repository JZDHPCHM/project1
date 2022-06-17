package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInfoDisclosurePartyEntity is a Querydsl query type for FbInfoDisclosurePartyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInfoDisclosurePartyEntity extends EntityPathBase<FbInfoDisclosurePartyEntity> {

    private static final long serialVersionUID = -458666673L;

    public static final QFbInfoDisclosurePartyEntity fbInfoDisclosurePartyEntity = new QFbInfoDisclosurePartyEntity("fbInfoDisclosurePartyEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath infoDisclosureId = createString("infoDisclosureId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbInfoDisclosurePartyEntity(String variable) {
        super(FbInfoDisclosurePartyEntity.class, forVariable(variable));
    }

    public QFbInfoDisclosurePartyEntity(Path<? extends FbInfoDisclosurePartyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInfoDisclosurePartyEntity(PathMetadata metadata) {
        super(FbInfoDisclosurePartyEntity.class, metadata);
    }

}

