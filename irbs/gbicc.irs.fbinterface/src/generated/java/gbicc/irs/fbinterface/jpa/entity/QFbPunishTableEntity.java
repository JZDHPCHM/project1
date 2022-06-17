package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishTableEntity is a Querydsl query type for FbPunishTableEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishTableEntity extends EntityPathBase<FbPunishTableEntity> {

    private static final long serialVersionUID = 1812232541L;

    public static final QFbPunishTableEntity fbPunishTableEntity = new QFbPunishTableEntity("fbPunishTableEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath areaCode = createString("areaCode");

    public final StringPath companyId = createString("companyId");

    public final StringPath counterPartCode = createString("counterPartCode");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath legalPerson = createString("legalPerson");

    public final StringPath modifyDate = createString("modifyDate");

    public final StringPath party = createString("party");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath punishAreaCode = createString("punishAreaCode");

    public final StringPath punishBasis = createString("punishBasis");

    public final StringPath punishContent = createString("punishContent");

    public final StringPath punishDecisionDate = createString("punishDecisionDate");

    public final StringPath punishDepart = createString("punishDepart");

    public final StringPath punishId = createString("punishId");

    public final StringPath punishName = createString("punishName");

    public final StringPath punishNo = createString("punishNo");

    public final StringPath punishReason = createString("punishReason");

    public final StringPath punishStatus = createString("punishStatus");

    public final StringPath punishTypeOne = createString("punishTypeOne");

    public final StringPath punishTypeTwo = createString("punishTypeTwo");

    public QFbPunishTableEntity(String variable) {
        super(FbPunishTableEntity.class, forVariable(variable));
    }

    public QFbPunishTableEntity(Path<? extends FbPunishTableEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishTableEntity(PathMetadata metadata) {
        super(FbPunishTableEntity.class, metadata);
    }

}

