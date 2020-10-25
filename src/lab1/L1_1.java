package lab1;

class Watek extends Thread{
	String wyraz;
	int nrProcesu;
	
	public Watek(String str, int numer) {
		wyraz = str;
		nrProcesu = numer;
	}
	
	public void run() {
		System.out.println(wyraz + " nr procesu: " + nrProcesu);
	}
}

public class L1_1 {

	public static void main(String[] args) {
		Watek w1 = new Watek("a", 1);
		Watek w2 = new Watek("b", 2);
		Watek w3 = new Watek("c", 3);
		Watek w4 = new Watek("d", 4);
		
		w1.start();
		w2.start();
		w3.start();
		w4.start();
		
		System.out.println("ostatnia linijka kodu");
	}

}
