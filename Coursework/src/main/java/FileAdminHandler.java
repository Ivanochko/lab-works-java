import lombok.NonNull;
import org.json.simple.JSONObject;

public class FileAdminHandler extends FileHandler<Admin> {

    public FileAdminHandler(@NonNull String filename) {
        super(filename);
    }

    @Override
    Admin parseOne(Object o) {
        String login;
        String password;
        JSONObject admin = (JSONObject) o;
        login = (String) admin.get("login");
        password = (String) admin.get("password");
        return new Admin(login, password);
    }
}
