public class Gato extends Pet{
    private int numeroDeVidas;

    public Gato(String nome, String raca, int numeroDeVidas) {
        super(nome, raca);
        this.numeroDeVidas = numeroDeVidas;
    }

    public int getNumeroDeVidas() {
        return numeroDeVidas;
    }

    public void setNumeroDeVidas(int numeroDeVidas) {
        this.numeroDeVidas = numeroDeVidas;
    }
}
