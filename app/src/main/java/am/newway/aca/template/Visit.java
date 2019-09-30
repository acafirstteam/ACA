package am.newway.aca.template;

public class Visit {
    private boolean confirm;
    private String dateTime;
    private String qrCode;
    private String userIdent;

    public Visit (  ) {
    }

    public boolean isConfirm () {
        return confirm;
    }

    public void setConfirm ( final boolean confirm ) {
        this.confirm = confirm;
    }

    public String getDateTime () {
        return dateTime;
    }

    public void setDateTime ( final String dateTime ) {
        this.dateTime = dateTime;
    }

    public String getQrCode () {
        return qrCode;
    }

    public void setQrCode ( final String qrCode ) {
        this.qrCode = qrCode;
    }

    public String getUserIdent () {
        return userIdent;
    }

    public void setUserIdent ( final String userIdent ) {
        this.userIdent = userIdent;
    }
}
