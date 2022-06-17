package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportEquityChangeEntity is a Querydsl query type for FbAnnualReportEquityChangeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportEquityChangeEntity extends EntityPathBase<FbAnnualReportEquityChangeEntity> {

    private static final long serialVersionUID = 1332496874L;

    public static final QFbAnnualReportEquityChangeEntity fbAnnualReportEquityChangeEntity = new QFbAnnualReportEquityChangeEntity("fbAnnualReportEquityChangeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath changeDate = createString("changeDate");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath equityChangeAfter = createString("equityChangeAfter");

    public final StringPath equityChangeBefore = createString("equityChangeBefore");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath serialNumber = createString("serialNumber");

    public final StringPath shareholder = createString("shareholder");

    public QFbAnnualReportEquityChangeEntity(String variable) {
        super(FbAnnualReportEquityChangeEntity.class, forVariable(variable));
    }

    public QFbAnnualReportEquityChangeEntity(Path<? extends FbAnnualReportEquityChangeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportEquityChangeEntity(PathMetadata metadata) {
        super(FbAnnualReportEquityChangeEntity.class, metadata);
    }

}

