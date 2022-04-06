package org.thraex.toolkit.naming;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * TODO: Opt {@code prefix}
 *
 * @author 鬼王
 * @date 2021/07/22 16:11
 */
public class PhysicalPrefixNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    private static final String physicalPrefix = "base";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        String newName = String.format("%s%s", physicalPrefix, name.getText());
        Identifier identifier = Identifier.toIdentifier(newName);

        return super.toPhysicalTableName(identifier, jdbcEnvironment);
    }

}
