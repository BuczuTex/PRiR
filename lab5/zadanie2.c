#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>
#include <math.h>

#define NR_PROC 3

double przyblizenie_pi(int n) {
    double wynik = 0;
    for(int i = 1; i <= n; i++) {
        wynik += pow(-1, i - 1)/(2 * i - 1);
    }
    wynik *= 4;
    return wynik;
}

int main() {
    pid_t pid;
    int zarodek;
    int n;
    double wynik;
    time_t tt;
    int status;

    for(int i = 1; i <= NR_PROC; i++) {
        pid = fork();
        zarodek = time(&tt) ^ getpid();
        srand(zarodek);
        if(pid < 0) {
            printf("Fork failed");
        }
        if(pid == 0) {
            n = rand()%4900 + 100;
            wynik = przyblizenie_pi(n);
            printf("Wynik przybliżenia liczby pi: %f. n = %d\n", wynik, n);
            exit(0);
        }
    }
    for(int i = 1; i <= NR_PROC; i++) wait(&status);
    return 0;
}
