package api.database;

import api.config.Config;
import lombok.Builder;
import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DBRequest {
    private RequestType requestType;
    private String table;
    private List<Join> joins;
    private List<Condition> conditions;
    private Class<?> extractAsClass;
    private boolean count;

    public enum RequestType {
        SELECT, INSERT, UPDATE, DELETE
    }

    public <T> T extractAs(Class<T> clazz) {
        this.extractAsClass = clazz;
        return executeQuery(clazz);
    }

    private <T> T executeQuery(Class<T> clazz) {
        String sql = buildSQL();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        // Создаём PreparedStatementSetter для подстановки параметров
        PreparedStatementSetter pss = ps -> {
            if (conditions != null) {
                for (int i = 0; i < conditions.size(); i++) {
                    ps.setObject(i + 1, conditions.get(i).getValue());
                }
            }
        };

        try {
            List<T> results = jdbcTemplate.query(
                    sql,
                    pss,
                    new BeanPropertyRowMapper<>(clazz)
            );

            return results.isEmpty() ? null : results.get(0);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    private DataSource getDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(Config.getProperty("db.url"));
        ds.setUsername(Config.getProperty("db.username"));
        ds.setPassword(Config.getProperty("db.password"));
        return ds;
    }

    private String buildSQL() {
        StringBuilder sql = new StringBuilder();

        switch (requestType) {
            case SELECT:
                if (count) sql.append("SELECT COUNT(*) AS COUNT FROM ");
                else sql.append("SELECT * FROM ");

                sql.append(table);

                if (joins != null && !joins.isEmpty()) {
                    for (Join join : joins) {
                        switch (join.getJoin()) {
                            case Join.Joins.INNER:
                                sql.append(" INNER JOIN ");
                                break;
                            case Join.Joins.LEFT:
                                sql.append(" LEFT JOIN ");
                                break;
                            case Join.Joins.RIGHT:
                                sql.append(" RIGHT JOIN ");
                                break;
                            default:
                                throw new UnsupportedOperationException("Join type " + join + " not implemented");
                        }
                        sql.append(join.getTable1()).append(" ON ")
                                .append(join.getTable1()).append(".").append(join.getColumn1()).append(" = ")
                                .append(join.getTable2()).append(".").append(join.getColumn2());
                    }
                }

                if (conditions != null && !conditions.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        Condition c = conditions.get(i);
                        if (c.getTable() != null) sql.append(c.getTable()).append(".");
                        sql.append(c.getColumn()).append(" ").append(c.getOperator()).append(" ?");
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Request type " + requestType + " not implemented");
        }

        return sql.toString();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getProperty("db.url"),
                Config.getProperty("db.username"),
                Config.getProperty("db.password")
        );
    }

    public static DBRequestBuilder builder() {
        return new DBRequestBuilder();
    }

    public static class DBRequestBuilder {
        private RequestType requestType;
        private String table;
        private List<Join> joins = new ArrayList<>();
        private List<Condition> conditions = new ArrayList<>();
        private Class<?> extractAsClass;
        private boolean count = false;

        public DBRequestBuilder requestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public DBRequestBuilder join(Join join) {
            this.joins.add(join);
            return this;
        }

        public DBRequestBuilder where(Condition condition) {
            this.conditions.add(condition);
            return this;
        }

        public DBRequestBuilder table(String table) {
            this.table = table;
            return this;
        }

        public DBRequestBuilder count(boolean count) {
            this.count = count;
            return this;
        }

        public <T> T extractAs(Class<T> clazz) {
            this.extractAsClass = clazz;
            DBRequest request = DBRequest.builder()
                    .requestType(requestType)
                    .table(table)
                    .joins(joins)
                    .conditions(conditions)
                    .extractAsClass(extractAsClass)
                    .count(count)
                    .build();
            return request.extractAs(clazz);
        }
    }
}
