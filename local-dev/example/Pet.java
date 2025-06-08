public class Pet{
    protected String nome;
    protected int idade;
    protected String raca;

    public Pet(String nome, String raca){
        this.idade = 0;
        this.nome = nome;
        this.raca = raca;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void acao(){

    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
    
    @Override
    public String toString(){
        return nome + "";
    }
}