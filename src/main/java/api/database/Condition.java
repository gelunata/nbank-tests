package api.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    private String table;
    private String column;
    private Object value;
    private String operator;

    public static Condition equalTo(String column, Object value) {
        return new Condition(null, column, value, "=");
    }

    public static Condition equalTo(String table, String column, Object value) {
        return new Condition(table, column, value, "=");
    }

    public static Condition notEqualTo(String column, Object value) {
        return new Condition(null, column, value, "!=");
    }

    public static Condition like(String column, String value) {
        return new Condition(null, column, value, "LIKE");
    }
}
