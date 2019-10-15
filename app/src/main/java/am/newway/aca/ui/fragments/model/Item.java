package am.newway.aca.ui.fragments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("login")
    @Expose
    private  String login;
    @SerializedName("avatar")
    @Expose
    private  String avatarurl;
    @SerializedName("html")
    @Expose
    private  String htmlurl;

    public Item(String login, String avatarurl, String htmlurl) {
        this.login = login;
        this.avatarurl = avatarurl;
        this.htmlurl = htmlurl;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public String getHtmlurl() {
        return htmlurl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public void setHtmlurl(String htmlurl) {
        this.htmlurl = htmlurl;
    }
}
