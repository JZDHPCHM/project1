package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrSplitIndexEntity is a Querydsl query type for IrrSplitIndexEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrSplitIndexEntity extends EntityPathBase<IrrSplitIndexEntity> {

    private static final long serialVersionUID = 347754895L;

    public static final QIrrSplitIndexEntity irrSplitIndexEntity = new QIrrSplitIndexEntity("irrSplitIndexEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath channelFlag = createString("channelFlag");

    public final StringPath collUserId = createString("collUserId");

    public final StringPath collUserLoginName = createString("collUserLoginName");

    public final StringPath collUserName = createString("collUserName");

    public final StringPath collWay = createString("collWay");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath examUserId = createString("examUserId");

    public final StringPath examUserLoginName = createString("examUserLoginName");

    public final StringPath examUserName = createString("examUserName");

    public final StringPath id = createString("id");

    public final StringPath isUse = createString("isUse");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgId = createString("orgId");

    public final StringPath orgName = createString("orgName");

    public final StringPath resultType = createString("resultType");

    public final StringPath reviewUserId = createString("reviewUserId");

    public final StringPath reviewUserLoginName = createString("reviewUserLoginName");

    public final StringPath reviewUserName = createString("reviewUserName");

    public final StringPath sourceIndexId = createString("sourceIndexId");

    public final StringPath sourceIndexName = createString("sourceIndexName");

    public final StringPath sourceProjId = createString("sourceProjId");

    public final StringPath sourceProjName = createString("sourceProjName");

    public final StringPath splitCode = createString("splitCode");

    public final StringPath splitEvalForm = createString("splitEvalForm");

    public final StringPath splitFormula = createString("splitFormula");

    public final StringPath splitFormulaCode = createString("splitFormulaCode");

    public final StringPath splitLevel = createString("splitLevel");

    public final StringPath splitName = createString("splitName");

    public final StringPath subProcessCode = createString("subProcessCode");

    public QIrrSplitIndexEntity(String variable) {
        super(IrrSplitIndexEntity.class, forVariable(variable));
    }

    public QIrrSplitIndexEntity(Path<? extends IrrSplitIndexEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrSplitIndexEntity(PathMetadata metadata) {
        super(IrrSplitIndexEntity.class, metadata);
    }

}

