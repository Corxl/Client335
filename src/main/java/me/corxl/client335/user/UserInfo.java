package me.corxl.client335.user;

public class UserInfo {
    private final String email, username, timeOfRegistration;

    public UserInfo(String email, String username, String timeOfRegistration) {
        this.email = email;
        this.username = username;
        this.timeOfRegistration = timeOfRegistration;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getTimeOfRegistration() {
        return timeOfRegistration;
    }
}
