public class Pet {
    private int id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private int ownerId;

    public Pet() {
    }

    public Pet(int id, String name, String species, String breed, int age, int ownerId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.ownerId = ownerId;
    }

    public String getPetSummary() {
        return name + " is a " + species + " of breed " + breed + " and is " + age + " years old.";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public int getOwnerId() {
        return ownerId;
    }
}