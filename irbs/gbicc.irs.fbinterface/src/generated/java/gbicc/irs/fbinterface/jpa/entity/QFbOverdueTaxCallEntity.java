package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbOverdueTaxCallEntity is a Querydsl query type for FbOverdueTaxCallEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbOverdueTaxCallEntity extends EntityPathBase<FbOverdueTaxCallEntity> {

    private static final long serialVersionUID = 792526187L;

    public static final QFbOverdueTaxCallEntity fbOverdueTaxCallEntity = new QFbOverdueTaxCallEntity("fbOverdueTaxCallEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath businessAddress = createString("businessAddress");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath highTaxAuthority = createString("highTaxAuthority");

    public final StringPath id = createString("id");

    public final StringPath identificationNumber = createString("identificationNumber");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalName = createString("legalName");

    public final StringPath name = createString("name");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath taxAuthority = createString("taxAuthority");

    public final StringPath taxType = createString("taxType");

    public QFbOverdueTaxCallEntity(String variable) {
        super(FbOverdueTaxCallEntity.class, forVariable(variable));
    }

    public QFbOverdueTaxCallEntity(Path<? extends FbOverdueTaxCallEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbOverdueTaxCallEntity(PathMetadata metadata) {
        super(FbOverdueTaxCallEntity.class, metadata);
    }

}

