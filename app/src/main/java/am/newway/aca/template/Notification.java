package am.newway.aca.template;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public
class Notification {
    @Exclude
    private int id;
    private String message;
    private String user;
    private int messageType;
    private int messageSegment;
    @ServerTimestamp
    private Date date;
    private String title;
    private String largeBitmap;

    public
    Notification ( final String message , final String user , final int messageType ,
            final int messageSegment, final Date date ) {
        this.message = message;
        this.user = user;
        this.messageType = messageType;
        this.messageSegment = messageSegment;
        this.date = date;
    }

    public
    Notification () {
    }

    @Exclude
    public
    String getLargeBitmap () {
        return largeBitmap;
    }

    public
    void setLargeBitmap ( final String largeBitmap ) {
        this.largeBitmap = largeBitmap;
    }

    @Exclude
    public
    String getTitle ( final String[] notifSegment ) {
       if(notifSegment != null){
           return notifSegment[getMessageType()];
       }
       return "";
    }

    @Exclude
    public
    String getTitle (  ) {
       return title;
    }

    public
    void setTitle ( final String title ) {
        this.title = title;
    }

    @Exclude
    public
    int getId () {
        return id;
    }

    public
    void setId ( final int id ) {
        this.id = id;
    }

    public
    Date getDate () {
        return date;
    }

    public
    void setDate ( final Date date ) {
        this.date = date;
    }

    public
    String getMessage () {
        return message;
    }

    public
    void setMessage ( final String message ) {
        this.message = message;
    }

    public
    String getUser () {
        return user;
    }

    public
    void setUser ( final String user ) {
        this.user = user;
    }

    public
    int getMessageType () {
        return messageType;
    }

    public
    void setMessageType ( final int messageType ) {
        this.messageType = messageType;
    }

    public
    int getMessageSegment () {
        return messageSegment;
    }

    public
    void setMessageSegment ( final int messageSegment ) {
        switch ( messageSegment ) {
            case 0:
                this.messageSegment = -2;
                break;
            case 1:
                this.messageSegment = 2;
                break;
            case 2:
                this.messageSegment = 3;
                break;
            case 3:
                this.messageSegment = 0;
                break;
        }
    }
}
