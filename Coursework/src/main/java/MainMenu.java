import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public final class MainMenu {
    static private boolean work = true;
    static private Scanner sc = new Scanner(System.in);
    EmployeeHandler employeeHandler = new EmployeeHandler();

    public MainMenu(EmployeeHandler employeeHandler) {
        this.employeeHandler = employeeHandler;
    }

    private static int choose(int max) {
        while (true) {
            try {
                int value = sc.nextInt();
                if (value < max && value > 0) {
                    return value;
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
            }
            System.out.println("Wrong input, please, try again!");
        }
    }

    public void start() {
        work = true;

        while (work) {
            System.out.println();
            System.out.println("=~=~=~=~=~=~=~=~=~ M E N U =~=~=~=~=~=~=~=~=~");
            System.out.println("=====    1 - Out Employees              =====");
            System.out.println("====     2 - Get Employee by id          ====");
            System.out.println("===      3 - Get Employees by any option  ===");
            System.out.println("==       4 - Add new Employee              ==");
            System.out.println("=        5 - Remove Employee                =");
            System.out.println("==       6 - Modify data about employee    ==");
            System.out.println("===      7 - Sort Employees               ===");
            System.out.println("====     8 - Write to file               ====");
            System.out.println("=====    9 - Exit                      ======");
            System.out.println("=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=");
            System.out.println("Do your choice: ");

            action(choose(10));
            System.out.println();
        }
        sc.close();
    }

    private void action(int choice) {
        while (true) {
            switch (choice) {
                case 1:
                    this.outEmployees();
                    break;
                case 2:
                    this.getEmployeeById();
                    break;
                case 3:
                    this.getEmployeesByOption();
                    break;
                case 4:
                    this.addNewEmployee();
                    break;
                case 5:
                    this.removeEmployee();
                    break;
                case 6:
                    this.modifyEmployee();
                    break;
                case 7:
                    this.sortEmployees();
                    break;
                case 8:
                    this.writeToFile();
                    break;
                case 9:
                    work = false;
                    break;
                default:
                    continue;
            }
            break;
        }
    }

    private void writeToFile() {
        if (new FileEmployeeHandler("users.json").writeToFile(this.employeeHandler)) {
            System.out.println("The file is written!");
        }
    }

    private void outEmployees() {
        System.out.println("\nPlease choose:");
        System.out.println("1 - All list");
        System.out.println("2 - limited list");
        if (choose(3) == 1) {
            this.employeeHandler.outEmployees();
        } else {
            System.out.println("\nPlease, input the limit");
            System.out.println("> 0  and  < " + this.employeeHandler.getCountEmployees());
            this.employeeHandler.outEmployees(choose(this.employeeHandler.getCountEmployees() + 1));
        }
    }

    private Employee getEmployeeById() {
        System.out.println("\nPlease, input the id of searched employee");
        int id = choose(1000);
        Optional<Employee> employee = this.employeeHandler.getEmployeeById(id);
        if (employee.isPresent()) {
            System.out.println(employee.get());
            return employee.get();
        } else {
            System.out.println("Employee with id " + id + " not found!");
        }
        return null;
    }

    private void getEmployeesByOption() {
        System.out.println("\nEnter option by that you want to search employees");
        System.out.println("1 - Office");
        System.out.println("2 - Position");
        System.out.println("3 - Salary");
        System.out.println("4 - Experience");
        switch (choose(5)) {
            case 1:
                this.getEmployeesByOffice();
                break;
            case 2:
                this.getEmployeesByPosition();
                break;
            case 3:
                this.getEmployeesBySalary();
                break;
            case 4:
                this.getEmployeesByExperience();
                break;
            default:
                break;
        }
    }

    private void addNewEmployee() {
        System.out.println("Enter the id of new Employee");
        int id = choose(1000000);
        if (this.employeeHandler.getEmployeeById(id).isPresent()) {
            System.out.println("Employee with this id is already exist!");
            return;
        }
        System.out.println("Enter first name of new Employee");
        sc.nextLine(); // to delete \n after entering int
        String name = sc.nextLine();
        System.out.println("Enter last name of new Employee");
        String lastname = sc.nextLine();
        System.out.println("Enter count year of experience of new Employee");
        int experience = choose(100);
        System.out.println("Enter position of new Employee");
        sc.nextLine(); // to delete \n after entering int
        String position = sc.nextLine();
        System.out.println("Enter value of salary of new Employee");
        int salary = choose(1000000);
        System.out.println("Enter office of new Employee");
        sc.nextLine(); // to delete \n after entering int
        String office = sc.nextLine();

        this.employeeHandler.addEmployee(Employee.builder()
                .id(id).name(name).lastname(lastname).experience(experience)
                .salary(salary).position(position).office(office).build());

        System.out.println(this.employeeHandler.getEmployeeById(id).get());
    }

    private void modifyEmployee() {
        System.out.println("Employee to modify:");
        Employee employee = this.getEmployeeById();
        if (employee != null) {
            System.out.println("What field need to be modified:");
            System.out.println("1 - first name");
            System.out.println("2 - last name");
            System.out.println("3 - experience");
            System.out.println("4 - position");
            System.out.println("5 - salary");
            System.out.println("6 - office");

            switch (choose(7)) {
                case 1:
                    this.modifyEmployeeName(employee);
                    break;
                case 2:
                    this.modifyEmployeeLastname(employee);
                    break;
                case 3:
                    this.modifyEmployeeExperience(employee);
                    break;
                case 4:
                    this.modifyEmployeePosition(employee);
                    break;
                case 5:
                    this.modifyEmployeeSalary(employee);
                    break;
                case 6:
                    this.modifyEmployeeOffice(employee);
                    break;
                default:
                    break;
            }
            System.out.println(employee);
        }
    }

    private void modifyEmployeeName(Employee employee) {
        System.out.println("Enter new name:");
        sc.nextLine(); // to delete \n after entering int
        String name = sc.nextLine();
        employee.setName(name);
    }

    private void modifyEmployeeLastname(Employee employee) {
        System.out.println("Enter new last name:");
        sc.nextLine(); // to delete \n after entering int
        String lastname = sc.nextLine();
        employee.setLastname(lastname);
    }

    private void modifyEmployeeExperience(Employee employee) {
        System.out.println("Enter new count years of experience:");
        int experience = choose(100);
        employee.setExperience(experience);
    }

    private void modifyEmployeePosition(Employee employee) {
        System.out.println("Enter new position:");
        sc.nextLine(); // to delete \n after entering int
        String position = sc.nextLine();
        employee.setPosition(position);
    }

    private void modifyEmployeeSalary(Employee employee) {
        System.out.println("Enter new salary:");
        int salary = choose(1000000);
        employee.setSalary(salary);
    }

    private void modifyEmployeeOffice(Employee employee) {
        System.out.println("Enter new office:");
        sc.nextLine(); // to delete \n after entering int
        String office = sc.nextLine();
        employee.setOffice(office);
    }

    private void removeEmployee() {
        System.out.println("Employee to remove:");
        System.out.println("Enter id");
        int id = choose(1000);
        Optional<Employee> employee = this.employeeHandler.getEmployeeById(id);
        if (employee.isPresent()) {
            System.out.println(employee.get());
            System.out.println("Remove?\n1 - yes\n2 - no");
            int temp = choose(3);
            if (temp == 1) {
                this.employeeHandler.removeEmployee(id);
            }
        } else {
            System.out.println("Employee with id " + id + " not found!");
        }
    }

    private void sortEmployees() {
        System.out.println("By what field you want to sort? ");
        System.out.println("1 - sort by name");
        System.out.println("2 - sort by office");
        System.out.println("3 - sort by salary");
        System.out.println("4 - sort by experience");
        System.out.println("5 - sort by id");
        switch (choose(6)) {
            case 1:
                this.employeeHandler.sortEmployeesByName();
                break;
            case 2:
                this.employeeHandler.sortEmployeesByOffice();
                break;
            case 3:
                this.employeeHandler.sortEmployeesBySalary();
                break;
            case 4:
                this.employeeHandler.sortEmployeesByExperience();
                break;
            case 5:
                this.employeeHandler.sortEmployeesById();
                break;
            default:
                break;
        }
        this.employeeHandler.outEmployees();
    }

    private void getEmployeesByOffice() {
        System.out.println("Enter the office from what you need to get employees:");
        sc.nextLine(); // to delete \n after entering int
        String office = sc.nextLine();
        this.employeeHandler.getEmployeesByOffice(office).outEmployees();
    }

    private void getEmployeesByPosition() {
        System.out.println("Enter the position that may bee employees:");
        sc.nextLine(); // to delete \n after entering int6

        String position = sc.nextLine();
        this.employeeHandler.getEmployeesByPosition(position).outEmployees();
    }

    private void getEmployeesBySalary() {
        System.out.println("Please choose:");
        System.out.println("1 - Employees that have equals salary");
        System.out.println("2 - Employees that have greater salary");
        System.out.println("3 - Employees that have lower salary");
        System.out.println("4 - Employees that have salary in limits");
        switch (choose(5)) {
            case 1:
                this.getEmployeesByEqualsSalary();
                break;
            case 2:
                this.getEmployeesByGreaterSalary();
                break;
            case 3:
                this.getEmployeesByLowerSalary();
                break;
            case 4:
                this.getEmployeesByLimitedSalary();
                break;
            default:
                break;
        }
    }

    private void getEmployeesByEqualsSalary() {
        System.out.println("Enter the value of salary:");
        int salary = choose(1000000);
        this.employeeHandler.getEmployeesBySalary(salary).outEmployees();
    }

    private void getEmployeesByGreaterSalary() {
        System.out.println("Enter the value of salary:");
        int salary = choose(1000000);
        this.employeeHandler.getEmployeesByGreaterSalary(salary).outEmployees();
    }

    private void getEmployeesByLowerSalary() {
        System.out.println("Enter the value of salary:");
        int salary = choose(1000000);
        this.employeeHandler.getEmployeesByLowerSalary(salary).outEmployees();
    }

    private void getEmployeesByLimitedSalary() {
        System.out.println("Enter the top value of salary:");
        int topSalary = choose(1000000);
        System.out.println("Enter the bottom value of salary:");
        int bottomSalary = choose(1000000);
        this.employeeHandler.getEmployeesBySalary(bottomSalary, topSalary).outEmployees();
    }

    private void getEmployeesByExperience() {
        System.out.println("Enter the experience:");
        int experience = choose(100);
        this.employeeHandler.getEmployeesByExperience(experience).outEmployees();
    }
}
