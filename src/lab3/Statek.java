package lab3;

import java.util.Random;

// Symulator portu
public class Statek extends Thread{
    static int PORT = 1;
    static int ZEGLUGA = 2;
    static int KONIEC_ZEGLUGI = 3;
    static int WYPLYNIECIE = 4;
    static int TANKOWANIE = 1000;
    static int KONIEC_PALIWA = -1;
    static int POMOC = -2;

    private int paliwo;
    private int nr;
    private int stan;
    private Port port;
    Random rand;

    public Statek(int nr, int paliwo, Port port){
        this.nr = nr;
        this.paliwo = paliwo;
        this.port = port;
        this.stan = ZEGLUGA;
        this.rand = new Random();
    }
    @Override
    public void run(){
        while(true){
            if(stan == PORT){
                if(rand.nextInt(2) == 1){
                    stan = WYPLYNIECIE;
                    paliwo = TANKOWANIE;
                    System.out.println("Statek o numerze " + nr + " wyplywa.");
                    stan = port.wyplyn(nr);
                }
                else {
                    System.out.println("Poczekam jeszcze chwile.");
                }
            }
            else if(stan == WYPLYNIECIE){
                System.out.println("Wyplynalem, statek numer " + nr);
                stan = ZEGLUGA;
            }
            else if(stan == ZEGLUGA) {
                paliwo = rand.nextInt(300);
                System.out.println("Statek o numerze " + nr + " zegluje.");
                if(paliwo < 400){
                    stan = KONIEC_ZEGLUGI;
                }
                else {
                    try {
                        sleep(1000);
                    }
                    catch(InterruptedException e){}
                }
            }
            else if(stan == KONIEC_ZEGLUGI) {
                System.out.println("Statek o numerze " + nr + " zbliza sie do portu");
                stan = port.wplynDoPortu();
                if(stan == KONIEC_ZEGLUGI) {
                    paliwo -= rand.nextInt(300);
                    System.out.println("Rezerwa " + paliwo);
                    if (paliwo < 0) stan = KONIEC_PALIWA;
                }
            }
            else if(stan == KONIEC_PALIWA) {
                System.out.println("Statek o numerze " + nr + " wymaga pomocy z portu");
                stan = POMOC;
            }
            else if(stan == POMOC){
                try {
                    sleep(2000);
                }
                catch(InterruptedException e) {}
                port.pomoc(nr);
            }
        }
    }
}