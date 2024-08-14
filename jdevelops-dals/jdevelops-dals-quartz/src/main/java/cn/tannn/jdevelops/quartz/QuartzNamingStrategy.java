package cn.tannn.jdevelops.quartz;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 处理 quartz 表名问题
 */
public class QuartzNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    private static final Logger log = LoggerFactory.getLogger(QuartzNamingStrategy.class);

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }
        if(name.getText().startsWith("QRTZ_") || name.getText().startsWith("qrtz_")){
            return new Identifier(name.getText().toUpperCase(Locale.ROOT), name.isQuoted());
        }else {
            return name;
        }

    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToSnakeCase(name);
    }

    private Identifier convertToSnakeCase(Identifier identifier) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText()
                .replaceAll(regex, replacement)
                .toLowerCase(Locale.ROOT);
        return Identifier.toIdentifier(newName);
    }
}
