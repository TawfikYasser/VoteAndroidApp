package com.khaled.vote.AppClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    // User parameters ******* START ******
    private String UserName,UserId,UserEmail,UserImage,UserAge,UserSsn,UserType;
    // USER parameters ******* END ********


    //Empty Constructor
    public User(){}
    //END of User empty constructor

    //Parametrize constructor - to set the data

    public User(String userName, String userId, String userEmail, String userImage, String userAge, String userSsn, String userType) {
        UserName = userName;
        UserId = userId;
        UserEmail = userEmail;
        UserImage = userImage;
        UserAge = userAge;
        UserSsn = userSsn;
        UserType = userType;
    }


    //END of user parametrize constructor


    // All setters and getters *** START *************************

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getUserAge() {
        return UserAge;
    }

    public void setUserAge(String userAge) {
        UserAge = userAge;
    }

    public String getUserSsn() {
        return UserSsn;
    }

    public void setUserSsn(String userSsn) {
        UserSsn = userSsn;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }


    // All setters and getters *** END *************************
}
