public class Veterinarian extends Person {
    private String specialty;

    public Veterinarian() {
    }

    public Veterinarian(int id, String name, String phone, String email, String specialty) {
        super(id, name, phone, email);
        this.specialty = specialty;
    }

    @Override
    public String getRole() {
        return "Veterinarian";
    }

    public String getSpecialty() {
        return specialty;
    }
}