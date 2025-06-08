import java.util.ArrayList;

public class Dono {
    private String nome;
    private ArrayList<Pet> pets;

    public Dono(String nome) {
        this.nome = nome;
        this.pets = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public void removerPetByNome(String nomePet){
        Pet pet = pets.stream()
        .filter(p -> p.getNome().equals(nomePet))
        .findFirst()
        .get();

        if(pet != null){
            removerPet(pet);
        }
    }

    private void removerPet(Pet pet){
        pets.remove(pet);
    }

    @Override
    public String toString(){
        return nome;
    }
}