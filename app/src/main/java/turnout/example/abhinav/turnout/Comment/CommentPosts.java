package turnout.example.abhinav.turnout.Comment;

import android.support.annotation.Keep;

@Keep

public class CommentPosts {
    public String commentID;
    public String userName;
    public String comment;


    public CommentPosts(){
       }

    public CommentPosts(String commentID , String userName, String comment) {
        this.commentID = commentID;
        this.userName = userName;
        this.comment = comment;

       }

    public String getCommentID() {
        return commentID;
    }

    public String getuserName(){
        return userName;
    }

    public String getComment(){ return comment; }



}