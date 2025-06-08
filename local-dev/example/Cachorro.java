public class Cachorro extends Pet{
    private int nivelAdestramento;

    public Cachorro(String nome, String raca, int nivelAdestramento) {
        super(nome, raca);
        this.nivelAdestramento = nivelAdestramento;
    }

    public int getNivelAdestramento() {
        return nivelAdestramento;
    }

    public void setNivelAdestramento(int nivelAdestramento) {
        this.nivelAdestramento = nivelAdestramento;
    }
}
