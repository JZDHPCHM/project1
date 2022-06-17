package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNsPrjProject is a Querydsl query type for NsPrjProject
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNsPrjProject extends EntityPathBase<NsPrjProject> {

    private static final long serialVersionUID = 2021754848L;

    public static final QNsPrjProject nsPrjProject = new QNsPrjProject("nsPrjProject");

    public final StringPath assessedValue = createString("assessedValue");

    public final StringPath assessmentMethods = createString("assessmentMethods");

    public final StringPath assistingPmA = createString("assistingPmA");

    public final StringPath bpIdTenant = createString("bpIdTenant");

    public final StringPath coreleaseProportion = createString("coreleaseProportion");

    public final StringPath creditType = createString("creditType");

    public final StringPath documentType = createString("documentType");

    public final StringPath employeeId = createString("employeeId");

    public final StringPath enterpriseCcale = createString("enterpriseCcale");

    public final StringPath financeAmount = createString("financeAmount");

    public final StringPath iscorelease = createString("iscorelease");

    public final StringPath leaseChannel = createString("leaseChannel");

    public final StringPath leaseItemShortName = createString("leaseItemShortName");

    public final StringPath leaseOrganization = createString("leaseOrganization");

    public final StringPath leaseStartDate = createString("leaseStartDate");

    public final StringPath leaseTerm = createString("leaseTerm");

    public final StringPath margin = createString("margin");

    public final StringPath netValue = createString("netValue");

    public final StringPath originalValue = createString("originalValue");

    public final StringPath productType = createString("productType");

    public final StringPath projectName = createString("projectName");

    public final StringPath projectNumber = createString("projectNumber");

    public final StringPath riskManagerName = createString("riskManagerName");

    public QNsPrjProject(String variable) {
        super(NsPrjProject.class, forVariable(variable));
    }

    public QNsPrjProject(Path<? extends NsPrjProject> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNsPrjProject(PathMetadata metadata) {
        super(NsPrjProject.class, metadata);
    }

}

