package usertypes;

public class User {
    private String email;
    private String password;
    private String fileDirectory;

    // GETTER METHODS

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    // SETTER METHODS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }
}
