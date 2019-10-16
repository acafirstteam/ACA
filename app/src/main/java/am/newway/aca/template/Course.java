package am.newway.aca.template;

public class Course {
    private String name;
    private String link = "";
    private String description;
    private boolean isdel;
    private String group;
    private String group_name;
    private String url;
    private String color;

    public Course (  ) {
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

    public
    void setName ( final String name ) {
        this.name = name;
    }

    public
    String getLink () {
        return link;
    }

    public
    void setLink ( final String link ) {
        this.link = link;
    }

    public
    String getDescription () {
        return description;
    }

    public
    void setDescription ( final String description ) {
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
    String getGroup () {
        return group;
    }

    public
    void setGroup ( final String group ) {
        this.group = group;
    }

    public
    String getGroup_name () {
        return group_name;
    }

    public
    void setGroup_name ( final String group_name ) {
        this.group_name = group_name;
    }

    public
    String getUrl () {
        return url;
    }

    public
    void setUrl ( final String url ) {
        this.url = url;
    }
}
