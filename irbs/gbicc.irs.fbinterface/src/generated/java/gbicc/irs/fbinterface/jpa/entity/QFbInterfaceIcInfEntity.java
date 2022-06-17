package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInterfaceIcInfEntity is a Querydsl query type for FbInterfaceIcInfEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInterfaceIcInfEntity extends EntityPathBase<FbInterfaceIcInfEntity> {

    private static final long serialVersionUID = 1112177776L;

    public static final QFbInterfaceIcInfEntity fbInterfaceIcInfEntity = new QFbInterfaceIcInfEntity("fbInterfaceIcInfEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath bizHeadUnit = createString("bizHeadUnit");

    public final StringPath chiefBhaf = createString("chiefBhaf");

    public final StringPath chrgtor = createString("chrgtor");

    public final StringPath cnclDt = createString("cnclDt");

    public final StringPath companyAddress = createString("companyAddress");

    public final StringPath companyId = createString("companyId");

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath ctctTel = createString("ctctTel");

    public final StringPath dispEtpName = createString("dispEtpName");

    public final StringPath econProp = createString("econProp");

    public final StringPath estabDt = createString("estabDt");

    public final StringPath evalLvl = createString("evalLvl");

    public final StringPath execAfrCopsn = createString("execAfrCopsn");

    public final StringPath execAfrCopsnDele = createString("execAfrCopsnDele");

    public final StringPath fadeExec = createString("fadeExec");

    public final StringPath id = createString("id");

    public final StringPath inIndu = createString("inIndu");

    public final StringPath issuctfOrg = createString("issuctfOrg");

    public final StringPath ivstr = createString("ivstr");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalRepr = createString("legalRepr");

    public final StringPath licenDt = createString("licenDt");

    public final StringPath mgDedlnEnd = createString("mgDedlnEnd");

    public final StringPath mgDedlnStart = createString("mgDedlnStart");

    public final StringPath mgScp = createString("mgScp");

    public final StringPath moreOne = createString("moreOne");

    public final StringPath noteRevokeDt = createString("noteRevokeDt");

    public final StringPath operator = createString("operator");

    public final StringPath provCity = createString("provCity");

    public final StringPath regstDt = createString("regstDt");

    public final StringPath regstOrg = createString("regstOrg");

    public final StringPath regstStatus = createString("regstStatus");

    public final StringPath revokeDt = createString("revokeDt");

    public final NumberPath<java.math.BigDecimal> rgstCptl = createNumber("rgstCptl", java.math.BigDecimal.class);

    public final StringPath rgstCptlCcy = createString("rgstCptlCcy");

    public final StringPath rgstNo = createString("rgstNo");

    public final StringPath stdDt = createString("stdDt");

    public final StringPath stdName = createString("stdName");

    public final StringPath tpCode = createString("tpCode");

    public final StringPath tpName = createString("tpName");

    public final StringPath unifSociCredCode = createString("unifSociCredCode");

    public QFbInterfaceIcInfEntity(String variable) {
        super(FbInterfaceIcInfEntity.class, forVariable(variable));
    }

    public QFbInterfaceIcInfEntity(Path<? extends FbInterfaceIcInfEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInterfaceIcInfEntity(PathMetadata metadata) {
        super(FbInterfaceIcInfEntity.class, metadata);
    }

}

