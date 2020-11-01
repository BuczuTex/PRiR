package lab3;

// Symulator portu
public class Port {
    static int PORT = 1;
    static int ZEGLUGA = 2;
    static int KONIEC_ZEGLUGI = 3;
    static int WYPLYNIECIE = 4;
    static int POMOC = -2;

    private int iloscMiejsc;
    private int iloscMiejscZajetych;
    private int iloscStatkow;

    public Port(int iloscMiejsc, int iloscStatkow) {
        this.iloscMiejsc = iloscMiejsc;
        this.iloscStatkow = iloscStatkow;
        iloscMiejscZajetych = 0;
    }

    public synchronized int wplynDoPortu() {
        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException e) {}
        if (iloscMiejscZajetych < iloscMiejsc) {
            iloscMiejscZajetych++;
            System.out.println("Statek wplywa do portu na miejsce numer " + iloscMiejscZajetych);
            return PORT;
        }
        return KONIEC_ZEGLUGI;
    }

    public synchronized int wyplyn(int numer) {
        iloscMiejscZajetych--;
        System.out.println("Statek o numerze " + numer + " wyplywa z portu");
        return WYPLYNIECIE;
    }

    public synchronized int pomoc(int numer) {
        if(iloscMiejscZajetych < iloscMiejsc) {
            System.out.println("Statek o numerze " + numer + " jest gotowy do wyplyniecia.");
            return PORT;
        }
        else {
            System.out.println("Statek o numerze " + numer + " oczekuje na pomoc.");
            return POMOC;
        }
    }
}
