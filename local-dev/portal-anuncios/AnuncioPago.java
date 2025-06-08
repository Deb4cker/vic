public class AnuncioPago extends Anuncio{
    private int dias;
    private double valorPago;

    public boolean setDias(int dias) {
        this.dias = dias;
        return true;
    }

    public boolean setValorPago(double valorPago){
        this.valorPago = valorPago;
        return true;
    }

    public String toString(){
        return "Dias: " + dias + "\nValor: " + valorPago + "\n";
    }
}