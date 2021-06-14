import lombok.*;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Employee implements Userable {
    public int id;
    private String name;
    private String lastname;
    private int experience;
    private int salary;
    private String position;
    private String office;

    public void setName(@NonNull String name) {
        this.name = !name.equals("") ? name : this.name;
    }

    public void setLastname(@NonNull String lastname) {
        this.lastname = !lastname.equals("") ? lastname : this.lastname;
    }

    public void setPosition(@NonNull String position) {
        this.position = !position.equals("") ? position : this.position;
    }

    public void setOffice(@NonNull String office) {
        this.office = !office.equals("") ? office : this.office;
    }

    @Override
    public String toString() {
        return "Employee " + id + "{\n    name=" + name + ",\n    lastname=" + lastname + ",\n    experience="
                + experience + ",\n    salary=" + salary + ",\n    position=" + position + ",\n    office=" + office
                + "\n}";
    }
}
