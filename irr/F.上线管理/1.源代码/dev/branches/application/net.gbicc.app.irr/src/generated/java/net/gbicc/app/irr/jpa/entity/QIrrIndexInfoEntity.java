package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrIndexInfoEntity is a Querydsl query type for IrrIndexInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrIndexInfoEntity extends EntityPathBase<IrrIndexInfoEntity> {

    private static final long serialVersionUID = -1959766929L;

    public static final QIrrIndexInfoEntity irrIndexInfoEntity = new QIrrIndexInfoEntity("irrIndexInfoEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath collectionWay = createString("collectionWay");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final NumberPath<java.math.BigDecimal> decimalPlace = createNumber("decimalPlace", java.math.BigDecimal.class);

    public final StringPath evalCrit = createString("evalCrit");

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexDesc = createString("indexDesc");

    public final StringPath indexEvalForm = createString("indexEvalForm");

    public final StringPath indexFormula = createString("indexFormula");

    public final StringPath indexFormulaCode = createString("indexFormulaCode");

    public final StringPath indexLevel = createString("indexLevel");

    public final StringPath indexLine = createString("indexLine");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexResultType = createString("indexResultType");

    public final StringPath indexStatus = createString("indexStatus");

    public final StringPath indexUnit = createString("indexUnit");

    public final NumberPath<java.math.BigDecimal> indexValue = createNumber("indexValue", java.math.BigDecimal.class);

    public final StringPath isApplicabie = createString("isApplicabie");

    public final StringPath isPfm = createString("isPfm");

    public final StringPath isScoreIndex = createString("isScoreIndex");

    public final StringPath isXbrl = createString("isXbrl");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath pfmDesc = createString("pfmDesc");

    public final StringPath pfmEvalForm = createString("pfmEvalForm");

    public final StringPath pfmFormula = createString("pfmFormula");

    public final StringPath pfmFormulaCode = createString("pfmFormulaCode");

    public final StringPath pfmLevel = createString("pfmLevel");

    public final StringPath projTypeId = createString("projTypeId");

    public final StringPath projTypeName = createString("projTypeName");

    public final StringPath scorType = createString("scorType");

    public final StringPath xbrlIndexElement = createString("xbrlIndexElement");

    public final StringPath xbrlIndexName = createString("xbrlIndexName");

    public QIrrIndexInfoEntity(String variable) {
        super(IrrIndexInfoEntity.class, forVariable(variable));
    }

    public QIrrIndexInfoEntity(Path<? extends IrrIndexInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrIndexInfoEntity(PathMetadata metadata) {
        super(IrrIndexInfoEntity.class, metadata);
    }

}

