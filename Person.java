
public abstract class Person {
    private int id;
    private String name;
    private String phone;
    private String email;

    public Person() {
    }

    public Person(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public abstract String getRole();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}