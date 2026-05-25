import java.util.ArrayList;

public class OwnerDAO implements CrudRepository<Owner> {
    private ArrayList<Owner> owners;

    public OwnerDAO() {
        owners = new ArrayList<>();
    }

    @Override
    public void create(Owner owner) {
        owners.add(owner);
        System.out.println("Owner registered successfully.");
    }

    @Override
    public Owner read(int id) {
        for (Owner owner : owners) {
            if (owner.getId() == id) {
                return owner;
            }
        }
        return null;
    }

    @Override
    public void update(Owner updatedOwner) {
        for (int i = 0; i < owners.size(); i++) {
            if (owners.get(i).getId() == updatedOwner.getId()) {
                owners.set(i, updatedOwner);
                System.out.println("Owner updated successfully.");
                return;
            }
        }
        System.out.println("Owner not found.");
    }

    @Override
    public void delete(int id) {
        for (Owner owner : owners) {
            if (owner.getId() == id) {
                owners.remove(owner);
                System.out.println("Owner deleted successfully.");
                return;
            }
        }
        System.out.println("Owner not found.");
    }

    public void showAllOwners() {
        if (owners.isEmpty()) {
            System.out.println("No owners registered.");
        } else {
            for (Owner owner : owners) {
                System.out.println("ID: " + owner.getId() + 
                                   " | Name: " + owner.getName() + 
                                   " | Role: " + owner.getRole());
            }
        }
    }
}