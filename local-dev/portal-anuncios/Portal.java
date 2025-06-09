import java.util.ArrayList;

public class Portal{
    private String nome;
    private String url;
    private ArrayList<Anuncio> anuncios;

    public Portal(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public String toString() {
        return nome;
    }
}