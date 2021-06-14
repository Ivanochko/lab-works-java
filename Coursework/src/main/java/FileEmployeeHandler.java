import lombok.NonNull;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class FileEmployeeHandler extends FileHandler<Employee> {

    public FileEmployeeHandler(@NonNull String filename) {
        super(filename);
    }

    @Override
    Employee parseOne(Object o) {
        JSONObject employee = (JSONObject) o;
        int id = (int) (long) employee.get("id");
        String name = (String) employee.get("name");
        String lastname = (String) employee.get("lastname");
        int experience = (int) (long) employee.get("experience");
        int salary = (int) (long) employee.get("salary");
        String position = (String) employee.get("position");
        String office = (String) employee.get("office");

        return new Employee(id, name, lastname, experience, salary, position, office);
    }

    // write list from employeeHandler to JSONAArray and than to JSON file
    public boolean writeToFile(EmployeeHandler employeeHandler) {
        try (FileWriter file = new FileWriter(path + this.filename);) {
            file.write(employeeHandler.toJSONArray().toJSONString());
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
