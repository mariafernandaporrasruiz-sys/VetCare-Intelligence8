import java.util.ArrayList;

public class AppointmentDAO implements CrudRepository<Appointment> {
    private ArrayList<Appointment> appointments;

    public AppointmentDAO() {
        appointments = new ArrayList<>();
    }

    @Override
    public void create(Appointment appointment) {
        appointments.add(appointment);
        System.out.println("Appointment registered successfully.");
    }

    @Override
    public Appointment read(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    @Override
    public void update(Appointment updatedAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == updatedAppointment.getId()) {
                appointments.set(i, updatedAppointment);
                System.out.println("Appointment updated successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    @Override
    public void delete(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                appointments.remove(appointment);
                System.out.println("Appointment deleted successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    public void showAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments registered.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println("ID: " + appointment.getId() + " | " + appointment.getAppointmentSummary());
            }
        }
    }
}