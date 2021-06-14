import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class EmployeeHandler {
    // list that contains all employees that need to work
    private List<Employee> employersList;

    // private constructor that block using empty construct of method
    private EmployeeHandler() {
        super();
        employersList = new ArrayList<>();
    }

    // constructor that receive vararg arguments of Employee
    public EmployeeHandler(@NonNull Employee... employers) {
        this();
        this.addEmployees(employers);
    }

    // constructor that receive one employee
    public EmployeeHandler(@NonNull Employee employer) {
        this();
        this.addEmployee(employer);
    }

    // method to add one employee to all list of employees
    public void addEmployee(@NonNull Employee employer) {
        this.employersList.add(employer);
    }

    // method to add vararg employee to all list of employees
    public void addEmployees(@NonNull Employee... employers) {
        Arrays.stream(employers).forEach(this::addEmployee);
    }

    // method to get count of employees in list
    public int getCountEmployees() {
        return this.employersList.size();
    }

    @Override
    public String toString() {
        return "Employees list. It have a " + this.getCountEmployees();
    }

    // method to out employers from list with limited count
    public void outEmployees(int limit) {
        this.employersList.stream().limit(limit).map(Employee::toString).forEach(System.out::println);
    }

    // method to out all employers to console without count limits
    public void outEmployees() {
        if (this.employersList.size() == 0) System.out.println("List of employees is empty");
        this.outEmployees(this.getCountEmployees());
    }

    // sort employees by received comparator
    public void sortEmployees(Comparator<Employee> comparator) {
        this.employersList.sort(comparator);
    }

    public void sortEmployeesByName() {
        this.sortEmployees(Comparator.comparing(Employee::getName).thenComparing(Employee::getLastname)
                .thenComparing(Employee::getOffice));
    }

    public void sortEmployeesByOffice() {
        this.sortEmployees(Comparator.comparing(Employee::getOffice).thenComparing(Employee::getName)
                .thenComparing(Employee::getLastname));
    }

    public void sortEmployeesBySalary() {
        this.sortEmployees(Comparator.comparing(Employee::getSalary).reversed().thenComparing(Employee::getName)
                .thenComparing(Employee::getLastname));
    }

    public void sortEmployeesByExperience() {
        this.sortEmployees(Comparator.comparing(Employee::getExperience).reversed().thenComparing(Employee::getSalary)
                .thenComparing(Employee::getName).thenComparing(Employee::getLastname));
    }

    public void sortEmployeesById() {
        this.sortEmployees(Comparator.comparing(Employee::getId).reversed());
    }

    // getter for one employee
    // receive: int id of needed employee
    // returns: Optional<Employee> with employee that id equals to id that is a parameter
    //      or empty Optional if employee with this id isn`t exist
    public Optional<Employee> getEmployeeById(int id) {
        for (Employee employer : this.employersList)
            if (employer.id == id)
                return Optional.of(employer);

        return Optional.empty();
    }

    // getter of Employees by preset parameter
    // receive: Predicate of Employee need to be getted
    // returns: new EmployeeHandler of getted employees
    private EmployeeHandler getEmployees(Predicate<Employee> predicate) {
        return new EmployeeHandler(this.employersList.parallelStream().filter(predicate).collect(toList()));
    }

    public EmployeeHandler getEmployeesByOffice(String office) {
        return this.getEmployees(e -> e.getOffice().equalsIgnoreCase(office));
    }

    public EmployeeHandler getEmployeesByPosition(String position) {
        return this.getEmployees(e -> e.getPosition().equalsIgnoreCase(position));
    }

    // return all the employees that salary is in Between min and max
    public EmployeeHandler getEmployeesBySalary(int min, int max) {
        if (min > max) {
            System.out.println("Min value is greater than max value!");
            return new EmployeeHandler();
        }
        if (min < 0) {
            System.out.println("Values cannot be negative!");
            return new EmployeeHandler();
        }
        return this.getEmployees(e -> e.getSalary() <= max && e.getSalary() >= min);
    }

    // return all the employees that salary is equals
    public EmployeeHandler getEmployeesBySalary(int salary) {
        if (salary < 0) {
            System.out.println("Values cannot be negative!");
            return new EmployeeHandler();
        }
        return this.getEmployees(e -> e.getSalary() == salary);
    }

    // return all the employees that salary is greater
    public EmployeeHandler getEmployeesByGreaterSalary(int salary) {
        if (salary < 0) {
            System.out.println("Values cannot be negative!");
            return new EmployeeHandler();
        }
        return this.getEmployees(e -> e.getSalary() > salary);
    }

    // return all the employees that salary is lower
    public EmployeeHandler getEmployeesByLowerSalary(int salary) {
        if (salary < 0) {
            System.out.println("Values cannot be negative!");
            return new EmployeeHandler();
        }
        return this.getEmployees(e -> e.getSalary() < salary);
    }

    public EmployeeHandler getEmployeesByExperience(int experience) {
        return this.getEmployees(e -> e.getExperience() == experience);
    }

    public void removeEmployee(int id) {
        this.employersList.remove(this.getEmployeeById(id).orElse(null));
    }

    // method that parse list of Employees to JSON Array need to be writed
    public JSONArray toJSONArray() {
        JSONArray jsonArray = new JSONArray();

        for (Employee employee : this.employersList) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", employee.id);
            jsonObject.put("name", employee.getName());
            jsonObject.put("lastname", employee.getLastname());
            jsonObject.put("experience", employee.getExperience());
            jsonObject.put("salary", employee.getSalary());
            jsonObject.put("position", employee.getPosition());
            jsonObject.put("office", employee.getOffice());

            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }
}
