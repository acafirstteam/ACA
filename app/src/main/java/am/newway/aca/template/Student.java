package am.newway.aca.template;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

public
class Student {
    private String name;
    private String surname;
    private int age;
    private String email;
    private String phone;
    private String picture;
    private String course;
    @Exclude
    private boolean verified;
    private String token;
    @Exclude
    public String id;
    @Exclude
    private int type = -1;
    private OnStudentChangeListener listener;
    private final String TAG = getClass().getSimpleName();

    public
    Student (
            final String email , final String name , final String surname , final int age ,
            final String phone , final String picture , final String token ,
            final boolean verified , final String course , final int type ) {
        this.age = age;
        this.email = email == null ? "" : email;
        this.name = name == null ? "" : name;
        this.phone = phone == null ? "" : phone;
        this.picture = picture == null ? "" : picture;
        this.surname = surname == null ? "" : surname;
        this.verified = verified;
        this.course = course == null ? "" : course;
        this.token = token == null ? "" : token;
        this.type = type;
    }

    public
    Student () {
    }

    public
    void addOnStudentChangeListener ( OnStudentChangeListener listener ) {
        this.listener = listener;
    }

    public
    String getToken () {
        return token;
    }

    public
    void setToken ( final String token ) {
        Log.e( TAG , "setToken: "   );
        this.token = token;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    int getType () {
        return type;
    }

    public
    void setType ( final int type ) {
        Log.e( TAG , "setType: "   );
        this.type = type;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    @Exclude
    public
    String getId () {
        return id;
    }

    public
    void setId ( final String id ) {
        Log.e( TAG , "setId: "   );
        this.id = id;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    boolean isVerified () {
        return verified;
    }

    public
    void setVerified ( final boolean verified ) {
        Log.e( TAG , "setVerified: "   );
        this.verified = verified;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    int getAge () {
        return age;
    }

    public
    void setAge ( final int age ) {
        Log.e( TAG , "setAge: "   );
        this.age = age;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getCourse () {
        return course;
    }

    public
    void setCourse ( final String course ) {
        Log.e( TAG , "setCourse: "   );
        this.course = course;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getEmail () {
        return email == null ? "" : email;
    }

    public
    void setEmail ( final String email ) {
        Log.e( TAG , "setEmail: "   );
        this.email = email;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getName () {
        return name;
    }

    public
    void setName ( final String name ) {
        Log.e( TAG , "setName: "   );
        this.name = name;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getPhone () {
        return phone;
    }

    public
    void setPhone ( final String phone ) {
        Log.e( TAG , "setPhone: "   );
        this.phone = phone;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getPicture () {
        return picture == null ? "" : picture;
    }

    public
    void setPicture ( final String picture ) {
        Log.e( TAG , "setPicture: "   );
        this.picture = picture;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    String getSurname () {
        return surname;
    }

    public
    void setSurname ( final String surname ) {
        Log.e( TAG , "setSurname: "   );
        this.surname = surname;
        if ( listener != null )
            listener.OnStudentChanged();
    }

    public
    interface OnStudentChangeListener {
        void OnStudentChanged ();
    }
}
