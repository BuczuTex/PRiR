package lab3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class ParallelJulia extends Thread {

    int me;
    static final int N = 4096;
    static final int CUTOFF = 1000;

    static int[][] set = new int[N][N];

    public ParallelJulia(int me){
        this.me = me;
    }

    public void run(){
        int begin = 0, end = 0;
        if (me == 0) {
            begin = 0;
            end = (N / 4);
        }
        else if (me == 1) {
            begin = (N / 4);
            end = (N / 4) * 2;
        }
        else if (me == 2) {
            begin = (N / 4) * 2;
            end = (N / 4) * 3;
        }
        else if (me == 3) {
            begin = (N / 4) * 3;
            end = N;
        }
        for(int i = begin; i < end; i++){
            for(int j = 0; j < N; j++){
                double cr = 1.5 * (i - N/2) / (0.5 * N);
                double ci = (j - N/2) / (0.5 * N);
                double zr = cr, zi = ci;

                int k = 0;
                while(k < CUTOFF && zr * zr + zi * zi < 4.0) {
                    double newr = zr * zr - zi * zi - 0.7;
                    double newi = 2 * zr * zi + 0.27015;

                    zr = newr;
                    zi = newi;
                    k++;
                }
                set[i][j] = k;
            }
        }
    }


    public static void main(String[] args){
        ParallelJulia thread0 = new ParallelJulia(0);
        ParallelJulia thread1 = new ParallelJulia(1);
        ParallelJulia thread2 = new ParallelJulia(2);
        ParallelJulia thread3 = new ParallelJulia(3);

        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread0.join();
            thread1.join();
            thread2.join();
            thread3.join();
        }
        catch(InterruptedException e){}
        BufferedImage img = new BufferedImage(N,N, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                int k = set[i][j];

                float level;
                if(k < CUTOFF) {
                    level = (float) k / CUTOFF;
                }
                else {
                    level = 0;
                }
                Color c = new Color(0, level, 0);
                img.setRGB(i, j, c.getRGB());
            }
        }
        try {
            ImageIO.write(img, "PNG", new File("Julia.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
