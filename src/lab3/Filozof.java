package lab3;

import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class Filozof extends Thread {
    static final int MAX = 100;
    static Semaphore[] widelec;
    static int wariant;
    static int liczbaF;
    int mojNum;
    Random losuj = new Random();


    public Filozof(int nr){
        mojNum = nr;
    }
    public void run(){
        switch(wariant){
            case 1:
                pierwszyWariant();
                break;
            case 2:
                drugiWariant();
                break;
            case 3:
                trzeciWariant();
                break;
        }
    }
    private void pierwszyWariant(){
        while(true){
            System.out.println("Myślę filozof " + mojNum);
            try{
                Thread.sleep((long)(7000 * Math.random()));
            }
            catch(InterruptedException e){
            }
            widelec[mojNum].acquireUninterruptibly();
            widelec[(mojNum+1)%liczbaF].acquireUninterruptibly();
            System.out.println("Zaczyna jesc filozof " + mojNum);
            try{
                Thread.sleep((long)(5000 * Math.random()));
            }
            catch(InterruptedException e){
            }
            System.out.println("Konczy jesc filozof " + mojNum);

            widelec[mojNum].release();
            widelec[(mojNum + 1)%liczbaF].release();
        }
    }
    private void drugiWariant(){
        while(true) {
            System.out.println("Mysle filozof " + mojNum);
            try {
                Thread.sleep((long)(5000 * Math.random()));

            }
            catch(InterruptedException e){}
            if(mojNum == 0) {
                widelec[(mojNum + 1) % liczbaF].acquireUninterruptibly();
                widelec[mojNum].acquireUninterruptibly();
            }
            else {
                widelec[mojNum].acquireUninterruptibly();
                widelec[(mojNum + 1) % liczbaF].acquireUninterruptibly();
            }
            System.out.println("Zaczyna jesc filozof " + mojNum);
            try {
                Thread.sleep((long) (3000 * Math.random()));
            }
            catch(InterruptedException e){}
            System.out.println("Konczy jesc " + mojNum);

            widelec[mojNum].release();
            widelec[(mojNum + 1) % liczbaF].release();
        }
    }
    private void trzeciWariant(){
        while(true){
            System.out.println("Mysle filozof " + mojNum);
            try {
                Thread.sleep((long) (5000 * Math.random()));
            }
            catch(InterruptedException e){}

            int strona = losuj.nextInt(2);
            boolean podnosiDwaWidelce = false;
            do{
                if(strona == 0) {
                    widelec[mojNum].acquireUninterruptibly();
                    if(!(widelec[(mojNum + 1) % liczbaF]).tryAcquire()) {
                        widelec[mojNum].release();
                    }
                    else {
                        podnosiDwaWidelce = true;
                    }
                }
                else {
                    widelec[(mojNum + 1) % liczbaF].acquireUninterruptibly();
                    if(!(widelec[mojNum].tryAcquire())) {
                        widelec[(mojNum + 1) % liczbaF].release();
                    }
                    else {
                        podnosiDwaWidelce = true;
                    }
                }
            } while(podnosiDwaWidelce == false);

            System.out.println("Zaczyna jesc filozof " + mojNum);
            try {
                Thread.sleep((long) (3000 * Math.random()));
            }
            catch(InterruptedException e){}
            System.out.println("Konczy jesc filozof " + mojNum);

            widelec[mojNum].release();
            widelec[(mojNum + 1) % liczbaF].release();
        }
    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int liczba, wariant2;
        System.out.println("Liczba filozofow od 2 do 100:");
        liczba = in.nextInt();
        liczbaF = liczba;
        System.out.println("Wybierz wariant\n1.Klasyczny problem\n2.Niesymetryczne sieganie po widelce\n3.Rzut monety");
        wariant2 = in.nextInt();
        in.close();
        if((liczba <= 100 && liczba >=2) && (wariant2 >= 1 && wariant2 <= 3)) {
            wariant = wariant2;
            widelec = new Semaphore[liczba];
            for(int i = 0; i < liczba; i++){
                widelec[i] = new Semaphore(1);
            }
            for(int i = 0; i < liczba; i++){
                new Filozof(i).start();
            }
        }
        else System.out.println("Niepoprawne wartosci");
    }
}