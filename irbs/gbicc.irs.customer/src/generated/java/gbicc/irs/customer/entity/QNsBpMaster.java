package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNsBpMaster is a Querydsl query type for NsBpMaster
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNsBpMaster extends EntityPathBase<NsBpMaster> {

    private static final long serialVersionUID = 1978363988L;

    public static final QNsBpMaster nsBpMaster = new QNsBpMaster("nsBpMaster");

    public final StringPath actualLessee = createString("actualLessee");

    public final StringPath bpCategory = createString("bpCategory");

    public final StringPath bpCode = createString("bpCode");

    public final StringPath bpName = createString("bpName");

    public final StringPath bpType = createString("bpType");

    public final StringPath economicType = createString("economicType");

    public final StringPath employeeId = createString("employeeId");

    public final StringPath enterpriseHonor = createString("enterpriseHonor");

    public final StringPath enterpriseScale = createString("enterpriseScale");

    public final StringPath fdid = createString("fdid");

    public final StringPath financingScale = createString("financingScale");

    public final StringPath foundedDate = createString("foundedDate");

    public final StringPath highPrecision = createString("highPrecision");

    public final StringPath leaseOrganization = createString("leaseOrganization");

    public final StringPath legalPerson = createString("legalPerson");

    public final StringPath mainProducts = createString("mainProducts");

    public final StringPath marketSize = createString("marketSize");

    public final StringPath orgSubType = createString("orgSubType");

    public final StringPath orgType = createString("orgType");

    public final StringPath parkAddress = createString("parkAddress");

    public final StringPath registeredCapital = createString("registeredCapital");

    public final StringPath regNumber = createString("regNumber");

    public final StringPath regNumberType = createString("regNumberType");

    public final StringPath scoreTemplateId = createString("scoreTemplateId");

    public final StringPath segmentIndustry = createString("segmentIndustry");

    public QNsBpMaster(String variable) {
        super(NsBpMaster.class, forVariable(variable));
    }

    public QNsBpMaster(Path<? extends NsBpMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNsBpMaster(PathMetadata metadata) {
        super(NsBpMaster.class, metadata);
    }

}

