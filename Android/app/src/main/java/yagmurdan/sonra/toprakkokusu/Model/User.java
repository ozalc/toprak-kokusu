package yagmurdan.sonra.toprakkokusu.Model;

public class User {
    private String image;
    private String Name;

    public User(){

    }

    public User(String image, String name) {
        this.image = image;
        this.Name = name;
    }

    public String getProfilResmi() {
        return image;
    }

    public void setProfilResmi(String image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
