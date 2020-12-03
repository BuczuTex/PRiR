#include <stdio.h>
#include "mpi.h"

#define tag 1
#define N 1000
#define CUTOFF 300

int main(int argc, char *argv[]) {
    int proces, liczba_procesow;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &proces);
    MPI_Comm_size(MPI_COMM_WORLD, &liczba_procesow);
    MPI_Status status;
    
    int array[N][N];
    if((proces > 0) && (proces <= liczba_procesow - 1)) {
        if(proces != liczba_procesow - 1) {
            MPI_Recv(&array, N * N, MPI_INT, proces + 1, tag, MPI_COMM_WORLD, &status);
        }
        int begin = (N / liczba_procesow) * proces;
        int end = (N / liczba_procesow) * (proces + 1);
        //Obliczanie zbioru mandelbrota w zakresie <begin, end>
        for(int i = begin; i < end; i++) {
            for(int j = 0; j < N; j++) {
                double cr = (4.0 * i - 2 * N) / N;
                double ci = (4.0 * j - 2 * N) / N;
                double zr = cr, zi = ci;
                
                int k = 0;
                while(k < CUTOFF && zr * zr + zi * zi < 4.0) {
                    double newr = cr + zr * zr - zi * zi;
                    double newi = ci + 2 * zr * zi;

                    zr = newr;
                    zi = newi;

                    k++;
                }
                array[i][j] = k;
            }
        }
        MPI_Send(&array, N * N, MPI_INT, proces - 1, tag, MPI_COMM_WORLD);
    }
    if(proces == 0) {
        MPI_Recv(&array, N * N, MPI_INT, proces + 1, tag, MPI_COMM_WORLD, &status);
        FILE* pgmimg;
        pgmimg = fopen("mandelbrot.ppm", "wb");
        fprintf(pgmimg, "P3\n");
        fprintf(pgmimg, "%d %d\n", N, N);
        fprintf(pgmimg, "255\n");
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                fprintf(pgmimg, "0 %d 0 ", (array[i][j]/CUTOFF) * 255);
            }
            fprintf(pgmimg, "\n");         
        }
        fclose(pgmimg);    
    }
    MPI_Finalize();
    return 0;
}
