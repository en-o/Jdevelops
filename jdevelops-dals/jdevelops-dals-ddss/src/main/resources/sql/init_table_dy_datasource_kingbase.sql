-- "public"."dy_datasource" definition 数据源

-- Drop table

-- DROP TABLE "dy_datasource";

CREATE TABLE "dy_datasource" (
    "datasource_name" character varying(50 char) NULL,
	"datasource_url" TEXT NULL,
	"datasource_username" character varying(50 char) NULL,
	"datasource_password" character varying(64 char) NULL,
	"remark" character varying(200 char) NULL,
	"driver_class_name" character varying(200 char) NULL,
	"enable" integer DEFAULT 1
);
-- Column comments

COMMENT ON COLUMN "public"."dy_datasource"."datasource_name" IS '数据源名称';
COMMENT ON COLUMN "public"."dy_datasource"."datasource_url" IS '数据源url';
COMMENT ON COLUMN "public"."dy_datasource"."datasource_username" IS '数据源帐号(密文)';
COMMENT ON COLUMN "public"."dy_datasource"."datasource_password" IS '数据源密码(密文)';
COMMENT ON COLUMN "public"."dy_datasource"."remark" IS '备注';
COMMENT ON COLUMN "public"."dy_datasource"."driver_class_name" IS '数据库驱动';
COMMENT ON COLUMN "public"."dy_datasource"."enable" IS '是否启用[0:禁用 1:启用]';

