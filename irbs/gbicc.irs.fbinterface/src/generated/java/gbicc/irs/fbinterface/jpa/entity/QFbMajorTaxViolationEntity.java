package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbMajorTaxViolationEntity is a Querydsl query type for FbMajorTaxViolationEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbMajorTaxViolationEntity extends EntityPathBase<FbMajorTaxViolationEntity> {

    private static final long serialVersionUID = -1323588809L;

    public static final QFbMajorTaxViolationEntity fbMajorTaxViolationEntity = new QFbMajorTaxViolationEntity("fbMajorTaxViolationEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseProperties = createString("caseProperties");

    public final StringPath caseReportPeriod = createString("caseReportPeriod");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath factsDate = createString("factsDate");

    public final StringPath id = createString("id");

    public final StringPath illegalFacts = createString("illegalFacts");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath organizationCode = createString("organizationCode");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath punishDetail = createString("punishDetail");

    public final StringPath taxpayerCode = createString("taxpayerCode");

    public final StringPath taxpayerName = createString("taxpayerName");

    public QFbMajorTaxViolationEntity(String variable) {
        super(FbMajorTaxViolationEntity.class, forVariable(variable));
    }

    public QFbMajorTaxViolationEntity(Path<? extends FbMajorTaxViolationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbMajorTaxViolationEntity(PathMetadata metadata) {
        super(FbMajorTaxViolationEntity.class, metadata);
    }

}

