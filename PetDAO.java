import java.util.ArrayList;

public class PetDAO implements CrudRepository<Pet> {
    private ArrayList<Pet> pets;

    public PetDAO() {
        pets = new ArrayList<>();
    }

    @Override
    public void create(Pet pet) {
        pets.add(pet);
        System.out.println("Pet registered successfully.");
    }

    @Override
    public Pet read(int id) {
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                return pet;
            }
        }
        return null;
    }

    @Override
    public void update(Pet updatedPet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == updatedPet.getId()) {
                pets.set(i, updatedPet);
                System.out.println("Pet updated successfully.");
                return;
            }
        }
        System.out.println("Pet not found.");
    }

    @Override
    public void delete(int id) {
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                pets.remove(pet);
                System.out.println("Pet deleted successfully.");
                return;
            }
        }
        System.out.println("Pet not found.");
    }

    public void showAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
        } else {
            for (Pet pet : pets) {
                System.out.println(pet.getPetSummary());
            }
        }
    }
}