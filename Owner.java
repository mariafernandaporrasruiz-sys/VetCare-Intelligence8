

public class Owner extends Person {

    public Owner() {
    }

    public Owner(int id, String name, String phone, String email) {
        super(id, name, phone, email);
    }

    @Override
    public String getRole() {
        return "Pet Owner";
    }
}