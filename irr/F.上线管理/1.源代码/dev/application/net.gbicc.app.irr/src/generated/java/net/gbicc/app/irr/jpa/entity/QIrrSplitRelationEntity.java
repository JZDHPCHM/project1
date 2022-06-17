package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrSplitRelationEntity is a Querydsl query type for IrrSplitRelationEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrSplitRelationEntity extends EntityPathBase<IrrSplitRelationEntity> {

    private static final long serialVersionUID = -1955185531L;

    public static final QIrrSplitRelationEntity irrSplitRelationEntity = new QIrrSplitRelationEntity("irrSplitRelationEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath relaCode = createString("relaCode");

    public final StringPath relaId = createString("relaId");

    public final StringPath relaName = createString("relaName");

    public final StringPath splitCode = createString("splitCode");

    public final StringPath splitId = createString("splitId");

    public final StringPath splitName = createString("splitName");

    public QIrrSplitRelationEntity(String variable) {
        super(IrrSplitRelationEntity.class, forVariable(variable));
    }

    public QIrrSplitRelationEntity(Path<? extends IrrSplitRelationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrSplitRelationEntity(PathMetadata metadata) {
        super(IrrSplitRelationEntity.class, metadata);
    }

}

