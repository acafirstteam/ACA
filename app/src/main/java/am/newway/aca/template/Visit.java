package am.newway.aca.template;

import com.google.firebase.firestore.Exclude;

public class Visit {
    @Exclude
    private String id;
    private String dateTime;
    private String completeTime;
    private String qrCode;
    private String userIdent;
    private boolean open = true;
    private boolean confirm;

    public Visit (  ) {
    }

    public
    String getId () {
        return id;
    }

    public
    void setId ( final String id ) {
        this.id = id;
    }

    public
    String getCompleteTime () {
        return completeTime;
    }

    public
    void setCompleteTime ( final String completeTime ) {
        this.completeTime = completeTime;
    }

    public
    boolean isOpen () {
        return open;
    }

    public
    void setOpen ( final boolean open ) {
        this.open = open;
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
