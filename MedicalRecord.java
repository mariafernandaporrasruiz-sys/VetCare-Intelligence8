public class MedicalRecord {
    private int id;
    private int petId;
    private String description;
    private String diagnosis;
    private String treatment;
    private String recordDate;

    public MedicalRecord() {
    }

    public MedicalRecord(int id, int petId, String description, String diagnosis, String treatment, String recordDate) {
        this.id = id;
        this.petId = petId;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = recordDate;
    }

    public String getRecordSummary() {
        return "Date: " + recordDate +
               " | Description: " + description +
               " | Diagnosis: " + diagnosis +
               " | Treatment: " + treatment;
    }

    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public String getDescription() {
        return description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getRecordDate() {
        return recordDate;
    }
}