import java.util.List;
import java.util.Scanner;

public class Dispatcher {
    static String login;

    public static void main(String[] args) {
        // set global path in FileHandler
        FileHandler.path = "src/main/resources/";
        // read all admins from file to list in FileAdminHanler
        new FileAdminHandler("admin.json").read();

        Scanner sc = new Scanner(System.in);
        // logining process while user no have access to programm
        // if login and password equals to field of list in FileAdminHandler then continue programm
        // else try again
        while (!login(sc))
            System.out.println("\nWrong login or password, please try again!");

        System.out.println("\nHello, dear " + login + "!");

        // read employees from file users.json to list of employees
        List<Employee> employees = new FileEmployeeHandler("users.json").read();
        // packaging list of employees into employeeHandler
        EmployeeHandler employeeHandler = new EmployeeHandler(employees);
        // Start new MainMenu
        new MainMenu(employeeHandler).start();

        System.out.println("Good bye, " + login + "!");
    }

    static boolean login(Scanner sc) {
        System.out.println("Enter login: ");
        login = sc.nextLine();
        System.out.println("Enter password: ");
        return Admin.isAccess(login, sc.nextLine());
    }
}
