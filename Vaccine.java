public class Vaccine {
    private int id;
    private int petId;
    private String vaccineName;
    private String applicationDate;
    private String nextDueDate;

    public Vaccine() {
    }

    public Vaccine(int id, int petId, String vaccineName, String applicationDate, String nextDueDate) {
        this.id = id;
        this.petId = petId;
        this.vaccineName = vaccineName;
        this.applicationDate = applicationDate;
        this.nextDueDate = nextDueDate;
    }

    public String getVaccineSummary() {
        return vaccineName + " | Applied on: " + applicationDate + " | Next due date: " + nextDueDate;
    }

    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getNextDueDate() {
        return nextDueDate;
    }
}