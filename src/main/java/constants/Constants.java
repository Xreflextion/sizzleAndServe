package constants;

public class Constants {
    private static final String DIR_PATH = "src/main/resources/images/";
    private static final String REGEX_CHARACTERS = "[^a-zA-Z0-9-_]";
    private static final String REPLACEMENT_CHARACTER = "_";
    private static final String FILE_TYPE = ".jpg";

    public static String getPath(){
        return DIR_PATH;
    }

    public static String getRegex(){
        return REGEX_CHARACTERS;
    }

    public static String getReplacement(){
        return REPLACEMENT_CHARACTER;
    }

    public static String getFileType(){
        return FILE_TYPE;
    }
}
