import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class FileHandler <T extends Userable> {
    public static String path = "";
    public String filename = "";

    public FileHandler(@NonNull String filename) {
        this.filename = filename;
    }

    // abstract method to parse one object from JSON format to Java object
    abstract T parseOne(Object o);

    // generic method to parse file contents element by element
    List<T> parseJSON(JSONArray array) {
        List<T> employees = new ArrayList<>();
        for (Object o : array)
            employees.add(parseOne(o));
        return employees;
    }
    // generic method to read and use parse method to readed file
    public List<T> read() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray array = (JSONArray) parser.parse(new FileReader(path + this.filename));
            return parseJSON(array);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
