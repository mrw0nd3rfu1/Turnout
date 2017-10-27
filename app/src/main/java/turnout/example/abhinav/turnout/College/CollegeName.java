package turnout.example.abhinav.turnout.College;


import android.support.annotation.Keep;

@Keep

public class CollegeName {
    public String collegeID;
    public String collegeName;

    public CollegeName(){

    }


    public CollegeName(String collegeID , String collegeName,String LastPost,String FirstPost) {
        this.collegeID = collegeID;
        this.collegeName = collegeName;
       }
//    public Integer getLastPost(){return Integer.parseInt(LastPost);}

    String getCollegeID() {
        return collegeID;
    }

    String getCollegeName() {
        return collegeName;
    }

//    public Integer getFirstPost(){return Integer.parseInt(FirstPost);}



}