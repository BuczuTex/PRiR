package lab1;

class ThreadN extends Thread {
	public void run() {
		try {
			for(int i = 0; i < 10; i++) {
				Thread.sleep(1000);
				System.out.println("ThreadN");
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ThreadM extends Thread {
	public void run() {
		try {
			for(int i = 0; i < 10; i++) {
				Thread.sleep(1000);
				System.out.println("ThreadM");
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class L1_3 {

	public static void main(String[] args) {
		ThreadM tm = new ThreadM();
		tm.start();
		ThreadN tn = new ThreadN();
		tn.start();
		try {
			tm.join();
			tn.join();
			System.out.println("Watki zakonczone");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
