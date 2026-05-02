package api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    private static final double MAX_DEPOSIT = 5000;
    private static final double MAX_TRANSFER = 10000;
    private RandomData() {
    }

    public static String getUsername() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String getPassword() {
        return RandomStringUtils.randomAlphanumeric(3).toUpperCase() +
                RandomStringUtils.randomAlphanumeric(5).toLowerCase() +
                RandomStringUtils.randomNumeric(3) + "%$#";
    }

    public static String getName() {
        return RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
    }

    public static double getDepositAmount() {
        return getMoneyAmount(0, MAX_DEPOSIT);
    }

    public static double getDepositAmount(double min) {
        return getMoneyAmount(min, MAX_DEPOSIT);
    }

    public static double getTransferAmount(double min) {
        return getMoneyAmount(min, MAX_TRANSFER);
    }

    public static double getMoneyAmount(double max) {
        return getMoneyAmount(0, max);
    }


    public static double getMoneyAmount(double min, double max) {
        return (double) Math.round((min + Math.random() * (max - min)) * 100) / 100;
    }

    public static double getIncorrectDepositAmount() {
        return (double) Math.round((Math.random() * 4999.99 + MAX_DEPOSIT + 0.01) * 100.0) / 100.0;
    }
}
