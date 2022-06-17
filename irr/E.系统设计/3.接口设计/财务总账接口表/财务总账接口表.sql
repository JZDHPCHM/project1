drop table E_BDS_FIN_GL cascade constraints;

/*==============================================================*/
/* Table: E_BDS_FIN_GL    总账汇总接口表                        */
/*==============================================================*/
create table E_BDS_FIN_GL 
(
   ORG_NO               VARCHAR2(20 BYTE),
   ORG_NAME             VARCHAR2(50 BYTE),
   ITEM_CODE_LVL1       VARCHAR2(20 BYTE),
   ITEM_CODE_LVL2       VARCHAR2(20 BYTE),
   ITEM_NAME            VARCHAR2(50 BYTE),
   CHANNEL_CODE         VARCHAR2(20 BYTE),
   CHANNEL_NAME         VARCHAR2(50 BYTE),
   FIN_AMT              NUMBER(24,2),
   GL_DATE              VARCHAR2(7 BYTE)
);

comment on table E_BDS_FIN_GL is
'总账汇总表';

comment on column E_BDS_FIN_GL.ORG_NO is
'机构代码';

comment on column E_BDS_FIN_GL.ORG_NAME is
'机构说明';

comment on column E_BDS_FIN_GL.ITEM_CODE_LVL1 is
'一级科目';

comment on column E_BDS_FIN_GL.ITEM_CODE_LVL2 is
'二级科目';

comment on column E_BDS_FIN_GL.ITEM_NAME is
'科目说明';

comment on column E_BDS_FIN_GL.CHANNEL_CODE is
'渠道代码';

comment on column E_BDS_FIN_GL.CHANNEL_NAME is
'渠道说明';

comment on column E_BDS_FIN_GL.FIN_AMT is
'财务金额';

comment on column E_BDS_FIN_GL.GL_DATE is
'总账日期(yyyy-mm)';
