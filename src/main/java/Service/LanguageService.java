package Service;

public class LanguageService {

    private boolean english;

    public void useDutch() {
        english = false;
    }

    public void useEnglish() {
        english = true;
    }

    public boolean isEnglish() {
        return english;
    }

    public String getLanguageCode() {
        return english ? "EN" : "NL";
    }

    public String text(String dutchText, String englishText) {
        return english ? englishText : dutchText;
    }
}