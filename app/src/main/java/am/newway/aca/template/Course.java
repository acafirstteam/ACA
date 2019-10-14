package am.newway.aca.template;

public class Course {
    private String url;
    private boolean isdel;
    private String group;

    public Course ( final String url , final boolean isdel , final String group ) {
        this.url = url;
        this.isdel = isdel;
        this.group = group;
    }

    public Course () {
    }

    public boolean isIsdel () {
        return isdel;
    }

    public void setIsdel ( final boolean isdel ) {
        this.isdel = isdel;
    }

    public String getGroup () {
        return group;
    }

    public void setGroup ( final String group ) {
        this.group = group;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl ( final String url ) {
        this.url = url;
    }
}
