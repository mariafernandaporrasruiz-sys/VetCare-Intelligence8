public class Appointment {
    private int id;
    private int petId;
    private int veterinarianId;
    private String appointmentDate;
    private String reason;
    private String status;

    public Appointment() {
    }

    public Appointment(int id, int petId, int veterinarianId, String appointmentDate, String reason, String status) {
        this.id = id;
        this.petId = petId;
        this.veterinarianId = veterinarianId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.status = status;
    }

    public String getAppointmentSummary() {
        return "Date: " + appointmentDate + 
               " | Reason: " + reason + 
               " | Status: " + status;
    }

    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public int getVeterinarianId() {
        return veterinarianId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}