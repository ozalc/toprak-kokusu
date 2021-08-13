package yagmurdan.sonra.toprakkokusu.Model;

import java.util.List;

public class User {
    private String image;
    private String Name;


    public User(){

    }

    public User(String image, String name) {
        this.image = image;
        this.Name = name;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
