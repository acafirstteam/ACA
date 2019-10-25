package am.newway.aca.template;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@IgnoreExtraProperties
public
class Course {
    private String name;
    private String link = "";
    private Map<String, Object> description;
    private boolean isdel;
    private int group;
    private Map<String, Object> group_name;
    private String url;
    private String color;
    private String lecturer;

    public
    Course ( final String name ,
             final String link ,
             final boolean isdel ,
             final int group ,
             final String url ) {

        this.name = name;
        this.link = link;
        this.isdel = isdel;
        this.group = group;
        this.url = url;

        description = new HashMap<>();
        group_name = new HashMap<>();

    }

    public
    Course () {
    }

    public
    String getLecturer () {
        return lecturer;
    }

    public
    void setLecturer ( final String lecturer ) {
        this.lecturer = lecturer;
    }

    public
    String getColor () {
        return color;
    }

    public
    void setColor ( final String color ) {
        this.color = color;
    }

    public
    String getName () {
        return name;
    }

    @Exclude
    public
    String getNameFormated () {
        return name.replace( " " , "_" ).replace( "/" , "_" ).toLowerCase( Locale.US );
    }

    public
    void setName ( final String name ) {
        this.name = name;
    }

    public
    String getLink () {
        return link.trim();
    }

    public
    void setLink ( final String link ) {
        this.link = link;
    }

    public
    Map<String, Object> getDescription () {
        return description;
    }

    public
    void setDescription ( final Map<String, Object> description ) {
        this.description = description;
    }

    public
    boolean isIsdel () {
        return isdel;
    }

    public
    void setIsdel ( final boolean isdel ) {
        this.isdel = isdel;
    }

    public
    int getGroup () {
        return group;
    }

    public
    void setGroup ( final int group ) {
        this.group = group;
    }

    public
    Map<String, Object> getGroup_name () {
        return group_name;
    }

    public
    void setGroup_name ( final Map<String, Object> group_name ) {
        this.group_name = group_name;
    }

    public
    String getUrl () {
        return url.trim();
    }

    public
    void setUrl ( final String url ) {
        this.url = url;
    }
}
