package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbTaxIrrefularAccountEntity is a Querydsl query type for FbTaxIrrefularAccountEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbTaxIrrefularAccountEntity extends EntityPathBase<FbTaxIrrefularAccountEntity> {

    private static final long serialVersionUID = -1364868246L;

    public static final QFbTaxIrrefularAccountEntity fbTaxIrrefularAccountEntity = new QFbTaxIrrefularAccountEntity("fbTaxIrrefularAccountEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath businessAddress = createString("businessAddress");

    public final StringPath companyId = createString("companyId");

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath identificationNumber = createString("identificationNumber");

    public final StringPath identificationTime = createString("identificationTime");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalName = createString("legalName");

    public final StringPath publicPeriod = createString("publicPeriod");

    public final StringPath taxAuthority = createString("taxAuthority");

    public QFbTaxIrrefularAccountEntity(String variable) {
        super(FbTaxIrrefularAccountEntity.class, forVariable(variable));
    }

    public QFbTaxIrrefularAccountEntity(Path<? extends FbTaxIrrefularAccountEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbTaxIrrefularAccountEntity(PathMetadata metadata) {
        super(FbTaxIrrefularAccountEntity.class, metadata);
    }

}

