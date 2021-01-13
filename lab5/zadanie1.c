#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>

#define NR_PROC 3

double funkcja(double x) {
    return -2*x + 5;
}

double metoda_trapezow(int a, int b, int n) {
    double dx = (double)(b-a)/n;
    double wynik = (funkcja(a) + funkcja(b))/2;
    for(int i = 1; i < n; i++) {
        wynik += (double)funkcja((a + (i*(b-a))/n));
    }
    wynik *= dx;
    return wynik;
}

int main() {
    pid_t pid;
    int zarodek;
    int a, b, n;
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
            a = rand()%100;
            b = rand()%100;
            n = rand()%100 + 1;
            if (a > b) {
                double temp;
                temp = a;
                a = b;
                b = temp;
            }
            wynik = metoda_trapezow(a, b, n);
            printf("Wynik: %f. Przedział całkownaia <%d, %d>. Dokładność całkowania: %d\n",wynik,a,b,n);
            exit(0);
        }
    }
    for(int i = 1; i <= NR_PROC; i++) wait(&status);
    return 0;
}
