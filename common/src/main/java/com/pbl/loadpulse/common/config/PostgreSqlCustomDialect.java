package com.pbl.loadpulse.common.config;

import org.hibernate.dialect.PostgreSQLDialect;

public class PostgreSqlCustomDialect extends PostgreSQLDialect {

  public PostgreSqlCustomDialect() {
    super();
    //    registerHibernateType(Types.OTHER, JsonNodeStringType.class.getName());
    //    registerHibernateType(Types.ARRAY, StringArrayType.class.getName());
  }
}
