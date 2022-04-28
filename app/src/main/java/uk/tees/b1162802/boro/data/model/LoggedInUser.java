package uk.tees.b1162802.boro.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String password;
    private String mobile;
    private String dob;
    private String gender;
    private String age;
    private String fullname;



    public LoggedInUser(String userId, String displayName,
                        String password, String mobile,
                        String dob, String gender,
                        String age,
                        String fullname) {
        this.userId = userId;
        this.displayName = displayName;
        this.password = password;
        this.mobile = mobile;
        this.dob = dob;
        this.gender = gender;
        this.fullname = fullname;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getName(){
        return fullname;
    }

    public String getAge(){
        return age;
    }
}