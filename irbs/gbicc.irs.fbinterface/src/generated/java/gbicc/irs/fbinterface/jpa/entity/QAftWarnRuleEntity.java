package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAftWarnRuleEntity is a Querydsl query type for AftWarnRuleEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAftWarnRuleEntity extends EntityPathBase<AftWarnRuleEntity> {

    private static final long serialVersionUID = 1261307057L;

    public static final QAftWarnRuleEntity aftWarnRuleEntity = new QAftWarnRuleEntity("aftWarnRuleEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath frequency = createString("frequency");

    public final StringPath id = createString("id");

    public final StringPath isDelete = createString("isDelete");

    public final StringPath isValid = createString("isValid");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath releType = createString("releType");

    public final StringPath ruleCode = createString("ruleCode");

    public final StringPath ruleName = createString("ruleName");

    public final StringPath ruleSubType = createString("ruleSubType");

    public final StringPath ruleType = createString("ruleType");

    public final StringPath signalSource = createString("signalSource");

    public final StringPath trigDesc = createString("trigDesc");

    public final StringPath warnLevel = createString("warnLevel");

    public QAftWarnRuleEntity(String variable) {
        super(AftWarnRuleEntity.class, forVariable(variable));
    }

    public QAftWarnRuleEntity(Path<? extends AftWarnRuleEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAftWarnRuleEntity(PathMetadata metadata) {
        super(AftWarnRuleEntity.class, metadata);
    }

}

