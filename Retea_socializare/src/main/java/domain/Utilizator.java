package domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Utilizator extends Entity<UUID>{
    private String firstName;
    private String lastName;
    private String username;

    private String password;
    private List<Utilizator> friends;

    public Utilizator(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.setId(UUID.randomUUID());
    }

    public String getFirstName() {
        return firstName;
    }

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() { return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
//        return "Utilizator{" +
//                "firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", username =" + username +'\'' +
//                ", password=" + password + '\'' +
//                ", friends=" + friends +
//                '}';
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}
