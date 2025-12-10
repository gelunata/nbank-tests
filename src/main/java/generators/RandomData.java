package generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
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

    public static double getDepositAmount(){
        return (double) Math.round(Math.random() * 5000 * 100) / 100;
    }
}
