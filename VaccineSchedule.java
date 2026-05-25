public class VaccineSchedule {
    private String petName;
    private String vaccineName;
    private String nextDate;
    private String status;

    public VaccineSchedule(String petName, String vaccineName, String nextDate, String status) {
        this.petName = petName;
        this.vaccineName = vaccineName;
        this.nextDate = nextDate;
        this.status = status;
    }

    public String getPetName() {
        return petName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getNextDate() {
        return nextDate;
    }

    public String getStatus() {
        return status;
    }
}