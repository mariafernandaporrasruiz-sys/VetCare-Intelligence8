public class PetCareService {

    public void showGeneralRecommendation(Pet pet, Vaccine vaccine, MedicalRecord medicalRecord) {
        System.out.println("===== PET CARE RECOMMENDATION =====");
        System.out.println("Pet: " + pet.getName());
        System.out.println("Species: " + pet.getSpecies());
        System.out.println("Last vaccine: " + vaccine.getVaccineName());
        System.out.println("Next vaccine date: " + vaccine.getNextDueDate());
        System.out.println("Last medical diagnosis: " + medicalRecord.getDiagnosis());

        System.out.println();

        if (pet.getAge() >= 7) {
            System.out.println("Recommendation: This pet is considered senior. Regular veterinary check-ups are recommended.");
        } else {
            System.out.println("Recommendation: Continue with regular check-ups, vaccines, and preventive care.");
        }
    }

    public void validateAppointment(Appointment appointment) {
        if (appointment.getStatus().equalsIgnoreCase("Scheduled")) {
            System.out.println("The appointment is correctly scheduled.");
        } else {
            System.out.println("The appointment is not currently scheduled.");
        }
    }
}