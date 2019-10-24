package am.newway.aca.template;

public
class AdminItem {
    private String name;
    private String description;
    private String url;
    private Class cl;

    public
    Class getCl () {
        return cl;
    }

    public
    void setCl ( final Class cl ) {
        this.cl = cl;
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
    String getDescription () {
        return description;
    }

    public
    void setDescription ( final String description ) {
        this.description = description;
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