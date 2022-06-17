package net.gbicc.app.pfm.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPfmChannelResultEntity is a Querydsl query type for PfmChannelResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPfmChannelResultEntity extends EntityPathBase<PfmChannelResultEntity> {

    private static final long serialVersionUID = -543035761L;

    public static final QPfmChannelResultEntity pfmChannelResultEntity = new QPfmChannelResultEntity("pfmChannelResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath channelFlag = createString("channelFlag");

    public final StringPath channelName = createString("channelName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexScore = createString("indexScore");

    public final NumberPath<java.math.BigDecimal> indexWeight = createNumber("indexWeight", java.math.BigDecimal.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final NumberPath<java.math.BigDecimal> standValue = createNumber("standValue", java.math.BigDecimal.class);

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QPfmChannelResultEntity(String variable) {
        super(PfmChannelResultEntity.class, forVariable(variable));
    }

    public QPfmChannelResultEntity(Path<? extends PfmChannelResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPfmChannelResultEntity(PathMetadata metadata) {
        super(PfmChannelResultEntity.class, metadata);
    }

}

