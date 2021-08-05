package yagmurdan.sonra.toprakkokusu.Model;

public class Comment {
    private String Comment,Publisher;

    public Comment(String comment, String publisher) {
        Comment = comment;
        Publisher = publisher;
    }

    public Comment() {

    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }
}
