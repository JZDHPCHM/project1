package net.gbicc.app.xbrl.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QXbrlRegulatorMappingEntity is a Querydsl query type for XbrlRegulatorMappingEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QXbrlRegulatorMappingEntity extends EntityPathBase<XbrlRegulatorMappingEntity> {

    private static final long serialVersionUID = 1581220160L;

    public static final QXbrlRegulatorMappingEntity xbrlRegulatorMappingEntity = new QXbrlRegulatorMappingEntity("xbrlRegulatorMappingEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyCode = createString("companyCode");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath regulatorCode = createString("regulatorCode");

    public QXbrlRegulatorMappingEntity(String variable) {
        super(XbrlRegulatorMappingEntity.class, forVariable(variable));
    }

    public QXbrlRegulatorMappingEntity(Path<? extends XbrlRegulatorMappingEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QXbrlRegulatorMappingEntity(PathMetadata metadata) {
        super(XbrlRegulatorMappingEntity.class, metadata);
    }

}

