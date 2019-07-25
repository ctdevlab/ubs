package ubs.v2;

public class Post {
    String title;
    String description;
    String uid;
    String key;

    public Post(){
    }

    public Post(String title, String description, String uid, String key) {
        this.title = title;
        this.description = description;
        this.uid = uid;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return title + "\n" + description;
    }
}
