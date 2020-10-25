package lab1;

class Watek2 extends Thread{
	String wyraz;
	
	public Watek2(String str, int numer) {
		wyraz = str;
	}
	
	public void run() {
		for(int k=0; k < 100000; k++) {
			System.out.println(k + " " + wyraz);
			try {
				sleep((int) (Math.random()*1000));
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(wyraz + " koniec");
		}
	}
}

public class L1_2 {
	public static void main(String[] args) {
		new Watek2("aa", 1).start();
		new Watek2("bb", 2).start();
		new Watek2("cc", 3).start();
		new Watek2("dd", 4).start();
		System.out.println("ostatnia linjka kodu");
	}
}

