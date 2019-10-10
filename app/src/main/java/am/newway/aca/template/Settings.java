package am.newway.aca.template;

public class Settings {

    private boolean loginCheck;
    private boolean checkNotification;
    private String languageCheck;

    public Settings(
            boolean loginCheck,
            boolean checkNotification,
            String languageCheck){

        this.loginCheck = loginCheck;
        this.checkNotification = checkNotification;
        this.languageCheck = languageCheck;
    }

    public boolean isLoginCheck() {
        return loginCheck;
    }

    public void setLoginCheck(boolean loginCheck) {
        this.loginCheck = loginCheck;
    }

    public boolean isCheckNotification() {
        return checkNotification;
    }

    public void setCheckNotification(boolean checkNotification) {
        this.checkNotification = checkNotification;
    }

    public String getLanguageCheck() {
        return languageCheck;
    }

    public void setLanguageCheck(String languageCheck) {
        this.languageCheck = languageCheck;
    }
}
