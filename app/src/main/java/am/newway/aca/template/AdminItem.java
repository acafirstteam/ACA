package am.newway.aca.template;

import java.util.HashMap;
import java.util.Map;

public
class AdminItem {
    private Map<String, String> name;
    private Map<String, String> description;
    private String url;
    private Class cl;

    public
    AdminItem () {
        name = new HashMap<>();
        description = new HashMap<>();
    }

    public
    Class getCl () {
        return cl;
    }

    public
    void setCl ( final Class cl ) {
        this.cl = cl;
    }

    public
    String getName ( String key ) {
        return name.get( key );
    }

    public
    void setName ( final String key , final String name ) {
        this.name.put( key , name );
    }

    public
    String getDescription (String key) {
        return description.get( key );
    }

    public
    void setDescription ( final String key, final String description ) {
        this.description.put( key, description);
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