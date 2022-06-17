drop table E_BDS_FIN_GL cascade constraints;

/*==============================================================*/
/* Table: E_BDS_FIN_GL    ���˻��ܽӿڱ�                        */
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
'���˻��ܱ�';

comment on column E_BDS_FIN_GL.ORG_NO is
'��������';

comment on column E_BDS_FIN_GL.ORG_NAME is
'����˵��';

comment on column E_BDS_FIN_GL.ITEM_CODE_LVL1 is
'һ����Ŀ';

comment on column E_BDS_FIN_GL.ITEM_CODE_LVL2 is
'������Ŀ';

comment on column E_BDS_FIN_GL.ITEM_NAME is
'��Ŀ˵��';

comment on column E_BDS_FIN_GL.CHANNEL_CODE is
'��������';

comment on column E_BDS_FIN_GL.CHANNEL_NAME is
'����˵��';

comment on column E_BDS_FIN_GL.FIN_AMT is
'������';

comment on column E_BDS_FIN_GL.GL_DATE is
'��������(yyyy-mm)';
