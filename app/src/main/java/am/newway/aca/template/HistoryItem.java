package am.newway.aca.template;

public class HistoryItem {

    private String dateTime;
    private String qrCode;
    private String userIdent;
    private boolean confirm;

    public HistoryItem(
            String dateTime,
            String qrCode,
            String userIdent,
            boolean confirm
    ){

        this.dateTime = dateTime;
        this.qrCode = qrCode;
        this.userIdent = userIdent;
        this.confirm = confirm;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUserIdent() {
        return userIdent;
    }

    public void setUserIdent(String userIdent) {
        this.userIdent = userIdent;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
