package api.requests.steps;

import api.config.Config;
import api.dao.CountDao;
import api.dao.DepositDao;
import api.dao.TransactionDao;
import api.dao.UserDao;
import api.dao.comparison.AccountDao;
import api.database.Condition;
import api.database.DBRequest;
import api.database.Join;
import common.helps.StepLogger;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseSteps {
    @Getter
    public enum Table {
        CUSTOMERS("customers"),
        ACCOUNTS("accounts"),
        TRANSACTIONS("transactions");

        Table(String name) {
            this.name = name;
        }

        private String name;
    }

    public enum TransferType {
        TRANSFER_OUT,
        TRANSFER_IN
    }

    public static CountDao countRowsOfTable(Table table) {
        return DBRequest.builder()
                .requestType(DBRequest.RequestType.SELECT)
                .table(table.getName())
                .count(true)
                .extractAs(CountDao.class);
    }

    public static UserDao getUserByUsername(String username) {
        return StepLogger.log("Get user from database by username: " + username, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.CUSTOMERS.getName())
                    .where(Condition.equalTo("username", username))
                    .extractAs(UserDao.class);
        });
    }

    public static UserDao getUserById(Long id) {
        return StepLogger.log("Get user from database by ID: " + id, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.CUSTOMERS.getName())
                    .where(Condition.equalTo("id", id))
                    .extractAs(UserDao.class);
        });
    }

    public static UserDao getUserByRole(String role) {
        return StepLogger.log("Get user from database by role: " + role, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.CUSTOMERS.getName())
                    .where(Condition.equalTo("role", role))
                    .extractAs(UserDao.class);
        });
    }

    public static AccountDao getAccountByAccountNumber(String accountNumber) {
        return StepLogger.log("Get account from database by account number: " + accountNumber, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.ACCOUNTS.getName())
                    .where(Condition.equalTo("account_number", accountNumber))
                    .extractAs(AccountDao.class);
        });
    }

    public static AccountDao getAccountById(Long id) {
        return StepLogger.log("Get account from database by ID: " + id, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.ACCOUNTS.getName())
                    .where(Condition.equalTo("id", id))
                    .extractAs(AccountDao.class);
        });
    }

    public static AccountDao getAccountByCustomerId(Long customerId) {
        return StepLogger.log("Get account from database by customer ID: " + customerId, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.CUSTOMERS.getName())
                    .where(Condition.equalTo("customer_id", customerId))
                    .extractAs(AccountDao.class);
        });
    }


    public static DepositDao getTransactionById(Long transactionId) {
        return StepLogger.log("Get transaction from database by transactionId: " + transactionId, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.ACCOUNTS.getName())
                    .join(Join.inner(Table.TRANSACTIONS.getName(), "account_id", Table.ACCOUNTS.getName(), "id"))
                    .where(Condition.equalTo(Table.TRANSACTIONS.getName(), "id", transactionId))
                    .extractAs(DepositDao.class);
        });
    }

    public static TransactionDao getTransactionByAccountIdAndTransferType(Long accountId, TransferType type) {
        return StepLogger.log("Get transaction from database by accountId: " + accountId, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.TRANSACTIONS.getName())
                    .where(Condition.equalTo("account_id", accountId))
                    .where(Condition.equalTo("type", type.name()))
                    .extractAs(TransactionDao.class);
        });
    }

    public static CountDao countTransactionByAccountId(Long accountId) {
        return StepLogger.log("Get count transaction from database by accountId: " + accountId, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table(Table.TRANSACTIONS.getName())
                    .where(Condition.equalTo("account_id", accountId))
                    .count(true)
                    .extractAs(CountDao.class);
        });
    }

    public static void updateAccountBalance(Long accountId, Double newBalance) {
        StepLogger.log("Update account balance in database for account ID: " + accountId + " to: " + newBalance, () -> {
            try (Connection connection = DriverManager.getConnection(
                    Config.getProperty("db.url"),
                    Config.getProperty("db.username"),
                    Config.getProperty("db.password"))) {

                String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDouble(1, newBalance);
                    statement.setLong(2, accountId);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new RuntimeException("No account found with ID: " + accountId);
                    }

                    return rowsAffected;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update account balance", e);
            }
        });
    }
}
