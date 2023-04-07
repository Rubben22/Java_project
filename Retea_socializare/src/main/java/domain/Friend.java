package domain;

public class Friend {
    private String username2;
    private String username1;
    private String firstname;
    private String lastname;

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Friend(String username1, String usernname2, String firstname, String lastname) {
        this.username2 = usernname2;
        this.username1 = username1;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}


