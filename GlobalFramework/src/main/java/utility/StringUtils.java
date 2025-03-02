package utility;

import org.apache.commons.text.RandomStringGenerator;

public class StringUtils {

//    public static String generateRandomStringOfGivenLength(int length) {
//        return RandomStringUtils.randomAlphabetic(length);
//    }

    public static String generateRandomStringOfGivenLength(int length) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z')
                .build();
        return generator.generate(length);
    }

    public static String getAPIFormattedHeaderName(String headerName) {
        String split[] = headerName.split(" ");
        String finalString = "";
        for (int i = 0; i < split.length; i++) {
            finalString += split[i] + "_";
        }
        return finalString.substring(0, finalString.lastIndexOf("_")).toUpperCase().replace("_/", "");
    }
}
