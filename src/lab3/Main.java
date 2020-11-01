package lab3;

// Symulator portu
public class Main {
    static int iloscMiejsc = 10;
    static int iloscStatkow = 100;

    public static void main(String[] args) {
        Port port = new Port(iloscMiejsc, iloscStatkow);
        for(int i = 0; i < iloscStatkow; i++){
            new Statek(i, 1000, port).start();
        }
    }
}
