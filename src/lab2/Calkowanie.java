package lab2;

import java.util.Scanner;

class M_trapezow extends Thread {
	
	private double a, b;
	private double wynik;
	
	public M_trapezow(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	public void run() {
		this.wynik = (function(a) + function(b))/2;
	}
	
	public static double function(double x) {
		return x*x+3;
	}
	public double getWynik() {
		return wynik;
	}
}
class M_prostokatow extends Thread{
	
	private double a;
	private double wynik;
	
	public M_prostokatow(double a) {
		this.a = a;
	}
	public void run() {
		this.wynik = function(a);
	}
	public static double function(double x) {
		return x*x+3;
	}
	public double getWynik() {
		return wynik;
	}
}
class M_Simpsona extends Thread{
	private double a,b;
	private double wynik;
	
	public M_Simpsona(double a, double b) {
		this.a = a;
		this.b = b;
	}
	public static double function(double x) {
		return x*x+3;
	}
	public void run() {
		wynik = function(a) + function(b);
	}
	public double getWynik() {
		return wynik;
	}
}
public class Calkowanie {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Podaj dok³adnoœæ ca³kowania: ");
		int dokladnosc = in.nextInt();
		in.nextLine();
		System.out.println("Podaj przedzia³ ca³kowania w formie: lewyKoniec:prawyKoniec ");
		String temp = in.nextLine();
		double a = Double.parseDouble(temp.split(":")[0]);
		double b = Double.parseDouble(temp.split(":")[1]);
		double wynik = 0;
		double wynikPr = 0;
		in.close();
		
		//Metoda prostok¹tów i trapezów
		double h = (b-a)/dokladnosc;
		M_trapezow[] trapezy = new M_trapezow[dokladnosc];
		M_prostokatow[] prostokaty = new M_prostokatow[dokladnosc];
		for(int i = 0; i < dokladnosc; i++) {
			trapezy[i] = new M_trapezow(a + (i/(double)dokladnosc)*(b-a), a + ((i+1)/(double)dokladnosc)*(b-a));
			prostokaty[i] = new M_prostokatow(a + i * h);
			trapezy[i].start();
			prostokaty[i].start();
			try {
				trapezy[i].join();
				prostokaty[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wynik += trapezy[i].getWynik() * h;
			wynikPr += prostokaty[i].getWynik()*h;
		}
		System.out.println("Wynik w metodzie trapezów: "+ wynik);
		wynikPr += M_prostokatow.function(a + dokladnosc * h) * h;
		System.out.println("Wynik w metodzie prostok¹tów: "+ wynikPr);
		//Metoda Simpsona
		M_Simpsona[] parabole = new M_Simpsona[dokladnosc];
		double wynikSimpson = 0;
		double x0, x1;
		double hSimpson = 0;
		
		for(int i = 0; i < dokladnosc; i++) {
			x0 = a+(i*(b-a))/dokladnosc;
			x1 = a+((i+1)*(b-a))/dokladnosc;
			wynikSimpson += 4 * M_Simpsona.function((x1 + x0)/2);
			if(i == 0) {
				hSimpson = (x1 - x0)/2;
			}
			parabole[i] = new M_Simpsona(x0, x1);
			parabole[i].start();
			try {
				parabole[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			wynikSimpson += parabole[i].getWynik();
		}
		wynikSimpson *= hSimpson/3;
		System.out.println("Wynik w metodzie Simpsona: " + wynikSimpson);
	}

}
