/*
 * Copyright 2020 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.it.plugin.jdbc.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author Woonduk Kang(emeroad)
 */
public class PostgreSql_9_4_1207_JDBCDriverClass extends PostgreSqlJDBCDriverClass {

    @Override
    public Class<Driver> getDriver() {
        return forName("org.postgresql.Driver");
    }

    @Override
    public Class<Connection> getConnection() {
        return forName("org.postgresql.jdbc.PgConnection");
    }

    @Override
    public Class<Statement> getStatement() {
        return forName("org.postgresql.jdbc.PgStatement");
    }

    @Override
    public Class<PreparedStatement> getPreparedStatement() {
        return forName("org.postgresql.jdbc.PgStatement");
    }

    @Override
    public Class<CallableStatement> getCallableStatement() {
        return null;
    }

}
