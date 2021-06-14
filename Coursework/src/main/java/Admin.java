import java.util.ArrayList;
import java.util.List;

public class Admin implements Userable{
    // static list of all admins to check if admin has access to programm
    static List<Admin> listAdmins = new ArrayList<>();
    private String login;
    private String password;

    public Admin(String login, String password) {
        this.login = login;
        this.password = password;
        // automatic add to static listAdmins
        Admin.listAdmins.add(this);
    }
    // method that receive one pair: login and password
    // and check is it contains int listAdmins
    // returns: boolean if contains true, else false
    static boolean isAccess(String login, String password) {
        for (Admin admin : listAdmins)
            if (admin.password.equals(password) && admin.login.equals(login))
                return true;

        return false;
    }
}
