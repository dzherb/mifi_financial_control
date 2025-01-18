package users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class BaseUser implements User {
    private String username;
    private String uuid;
    private String passwordHashed;

    private BaseUser(String username, String password) {
        this.username = username;
        this.uuid = generateUUID();
        this.passwordHashed = hashPassword(password);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String hashPassword(String password) {
        // В учебных целях просто хэшируем пароль через md5.
        // В реальных условиях, конечно, нужно куда более безопасное хэширование.
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(password.getBytes());
        return new String(hash);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public boolean checkPassword(String password) {
        return passwordHashed.equals(hashPassword(password));
    }
}
