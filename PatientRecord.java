public class PatientRecord {
    private Pet pet;
    private Owner owner;
    private Vaccine vaccine;
    private MedicalRecord medicalRecord;
    private String aiRecommendation;

    public PatientRecord(Pet pet, Owner owner, Vaccine vaccine, MedicalRecord medicalRecord, String aiRecommendation) {
        this.pet = pet;
        this.owner = owner;
        this.vaccine = vaccine;
        this.medicalRecord = medicalRecord;
        this.aiRecommendation = aiRecommendation;
    }

    public Pet getPet() {
        return pet;
    }

    public Owner getOwner() {
        return owner;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public String getAiRecommendation() {
        return aiRecommendation;
    }
}