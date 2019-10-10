package am.newway.aca.template;

import com.google.firebase.firestore.Exclude;

public class Student {
    private String name;
    private String surname;
    private int age;
    private String email;
    private String phone;
    private String picture;
    private String course;
    private boolean verified;
    private String token;
    @Exclude
    public String id;
    private int type = -1;

    public Student (

            final String email ,
            final String name ,
            final String surname,
            final int age,
            final String phone ,
            final String picture,
            final String token,
            final boolean verified,
            final String course,
            final int type
    ) {
        this.age = age;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.picture = picture;
        this.surname = surname;
        this.verified = verified;
        this.course = course;
        this.token = token;
        this.type = type;
    }
    public Student (
            final String id
    ) {
        this.id = id;
    }

    public Student () {
    }

    public String getToken () {
        return token;
    }

    public void setToken ( final String token ) {
        this.token = token;
    }

    public int getType () {
        return type;
    }

    public void setType ( final int type ) {
        this.type = type;
    }

    @Exclude
    public String getId () {
        return id;
    }

    public void setId ( final String id ) {
        this.id = id;
    }

    public boolean isVerified () {
        return verified;
    }

    public void setVerified ( final boolean verified ) {
        this.verified = verified;
    }

    public int getAge () {
        return age;
    }

    public void setAge ( final int age ) {
        this.age = age;
    }

    public String getCourse () {
        return course;
    }

    public void setCourse ( final String course ) {
        this.course = course;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( final String email ) {
        this.email = email;
    }

    public String getName () {
        return name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    public String getPicture () {
        return picture;
    }

    public void setPicture ( final String picture ) {
        this.picture = picture;
    }

    public String getSurname () {
        return surname;
    }

    public void setSurname ( final String surname ) {
        this.surname = surname;
    }


}
