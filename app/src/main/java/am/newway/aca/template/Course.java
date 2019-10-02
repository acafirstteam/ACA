package am.newway.aca.template;

public class Course {
    private String url;

    public Course ( final String url ) {
        this.url = url;
    }

    public Course () {
    }

    public String getUrl () {
        return url;
    }

    public void setUrl ( final String url ) {
        this.url = url;
    }
}
