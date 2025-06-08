public class Anuncio {
    private String produto;
    private double valorVenda;
    private String cidade;
    private String estado;

    public boolean setProduto(String produto) {
        this.produto = produto;
        return true;
    }

    public boolean setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
        return true;
    }

    public boolean setCidade(String cidade) {
        this.cidade = cidade;
        return true;
    }

    public boolean setEstado(String estado) {
        this.estado = estado;
        return true;
    }

    public String toString() {
        return produto;
    }
}