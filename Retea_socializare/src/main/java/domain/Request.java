package domain;

public class Request {
    private String username1;
    private String username2;
    private String first_name;
    private String last_name;

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Request(String username1, String username2, String first_name, String last_name) {
        this.username1 = username1;
        this.username2 = username2;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Request(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
