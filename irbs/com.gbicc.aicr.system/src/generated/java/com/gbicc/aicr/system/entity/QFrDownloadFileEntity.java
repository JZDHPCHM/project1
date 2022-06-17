package com.gbicc.aicr.system.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFrDownloadFileEntity is a Querydsl query type for FrDownloadFileEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFrDownloadFileEntity extends EntityPathBase<FrDownloadFileEntity> {

    private static final long serialVersionUID = -384647671L;

    public static final QFrDownloadFileEntity frDownloadFileEntity = new QFrDownloadFileEntity("frDownloadFileEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath downloadMsg = createString("downloadMsg");

    public final StringPath downloadStatus = createString("downloadStatus");

    public final StringPath exportMsg = createString("exportMsg");

    public final StringPath exportStatus = createString("exportStatus");

    public final DateTimePath<java.util.Date> exportTime = createDateTime("exportTime", java.util.Date.class);

    public final StringPath exportUser = createString("exportUser");

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final StringPath fileType = createString("fileType");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFrDownloadFileEntity(String variable) {
        super(FrDownloadFileEntity.class, forVariable(variable));
    }

    public QFrDownloadFileEntity(Path<? extends FrDownloadFileEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFrDownloadFileEntity(PathMetadata metadata) {
        super(FrDownloadFileEntity.class, metadata);
    }

}

