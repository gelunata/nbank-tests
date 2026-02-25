package api.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Join {
    public enum Joins {INNER, LEFT, RIGHT}

    private Joins join;
    private String table1;
    private String column1;
    private String table2;
    private String column2;

    public static Join inner(String table1, String column1, String table2, String column2) {
        return new Join(Joins.INNER, table1, column1, table2, column2);
    }

    public static Join left(String table1, String column1, String table2, String column2) {
        return new Join(Joins.LEFT, table1, column1, table2, column2);
    }

    public static Join right(String table1, String column1, String table2, String column2) {
        return new Join(Joins.RIGHT, table1, column1, table2, column2);
    }
}