package users;

public interface User {
    String getUsername();
    String getUUID();
    boolean checkPassword(String password);
}
