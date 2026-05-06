package Service;

public class LanguageService {

    private static boolean english = false;

    public static void useDutch() {
        english = false;
    }

    public static void useEnglish() {
        english = true;
    }

    public static boolean isEnglish() {
        return english;
    }

    public static String getLanguageCode() {
        return english ? "EN" : "NL";
    }

    public static String text(String dutchText, String englishText) {
        return english ? englishText : dutchText;
    }
}