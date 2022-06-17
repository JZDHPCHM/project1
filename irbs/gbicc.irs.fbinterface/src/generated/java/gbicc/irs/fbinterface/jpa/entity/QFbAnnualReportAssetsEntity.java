package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportAssetsEntity is a Querydsl query type for FbAnnualReportAssetsEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportAssetsEntity extends EntityPathBase<FbAnnualReportAssetsEntity> {

    private static final long serialVersionUID = 782782872L;

    public static final QFbAnnualReportAssetsEntity fbAnnualReportAssetsEntity = new QFbAnnualReportAssetsEntity("fbAnnualReportAssetsEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

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

    public final StringPath mainOperateIncome = createString("mainOperateIncome");

    public final StringPath netProfit = createString("netProfit");

    public final StringPath profiTotal = createString("profiTotal");

    public final StringPath totalAssets = createString("totalAssets");

    public final StringPath totalLiabilityAmount = createString("totalLiabilityAmount");

    public final StringPath totalOperateIncome = createString("totalOperateIncome");

    public final StringPath totalOwnerEquity = createString("totalOwnerEquity");

    public final StringPath totalTaxAmount = createString("totalTaxAmount");

    public QFbAnnualReportAssetsEntity(String variable) {
        super(FbAnnualReportAssetsEntity.class, forVariable(variable));
    }

    public QFbAnnualReportAssetsEntity(Path<? extends FbAnnualReportAssetsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportAssetsEntity(PathMetadata metadata) {
        super(FbAnnualReportAssetsEntity.class, metadata);
    }

}

