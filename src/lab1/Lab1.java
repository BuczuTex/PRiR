package lab1;

class Zegarek extends Thread {
	int sekundy, minuty, godziny;
	
	public Zegarek() {
		sekundy = minuty = godziny = 0;
	}
	
	public void run() {
		for(;;) {
			sekundy++;
			if(sekundy == 60) {
				sekundy = 0;
				minuty++;
			}
			if(minuty == 60) {
				minuty = 0;
				godziny++;
			}
			System.out.println(godziny + ": " + minuty + ": " + sekundy);
			try {
				sleep(1000);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Lab1 {

	public static void main(String[] args) {
		Zegarek z = new Zegarek();
		z.start();
	}

}
