package com.pbl.loadtestweb.common.config;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Types;

public class PostgreSqlCustomDialect extends PostgreSQL10Dialect {

  public PostgreSqlCustomDialect() {
    super();
    registerHibernateType(Types.OTHER, JsonNodeStringType.class.getName());
    registerHibernateType(Types.ARRAY, StringArrayType.class.getName());
  }
}
