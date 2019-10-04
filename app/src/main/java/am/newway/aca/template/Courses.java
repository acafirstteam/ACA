package am.newway.aca.template;

public class Courses {

    private int image;
    private String name;


    public Courses(int image, String name) {
        this.image = image;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

