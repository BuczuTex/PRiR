#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "mpi.h"
#define REZERWA 500
#define PORT 1
#define WYPLYNIECIE 2
#define ZEGLUGA 3
#define KONIEC_ZEGLUGI 4
#define POMOC -2
#define TANKOWANIE 2000
int WPLYN = 1, NIE_WPLYWAJ = 0;
int paliwo = 2000;
int liczba_procesow;
int nr_procesu;
int ilosc_statkow;
int ilosc_miejsc = 4;
int ilosc_miejsc_zajetych = 0;
int tag = 1;
int wyslij[2];
int odbierz[2];
MPI_Status mpi_status;
void Wyslij(int nr_statku, int stan) {
    wyslij[0] = nr_statku;
    wyslij[1] = stan;
    MPI_Send(&wyslij, 2, MPI_INT, 0, tag, MPI_COMM_WORLD);
    sleep(1);
}

void Port(int liczba_procesow) {
    int nr_statku, status;
    ilosc_statkow = liczba_procesow - 1;
    sleep(2);
    while(ilosc_miejsc <= ilosc_statkow) {
        MPI_Recv(&odbierz, 2, MPI_INT, MPI_ANY_SOURCE, tag, MPI_COMM_WORLD, &mpi_status);
        nr_statku = odbierz[0];
        status = odbierz[1];
        if(status == 1) {
            printf("Statek %d jest teraz w porcie\n", nr_statku);
        }
        else if(status == 2) {
            printf("Statek %d wypływa z portu z miejsca %d\n", nr_statku, ilosc_miejsc_zajetych);
            ilosc_miejsc_zajetych--;
        }
        else if(status == 3) {
            printf("Statek %d zegluje\n", nr_statku);
        }
        else if(status == 4 || status == -2) {
            if(ilosc_miejsc_zajetych < ilosc_miejsc) {
                ilosc_miejsc_zajetych++;
                printf("Statek wplynal\n");
                MPI_Send(&WPLYN, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
            }
            else {
                MPI_Send(&NIE_WPLYWAJ, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
            }      
        }
    }
}

void Statek() {
    int stan, i;
    stan = WYPLYNIECIE;
    while(1) {
        if(stan == 1) {
            if(rand()%2 == 1) {
                stan = WYPLYNIECIE;
                paliwo = TANKOWANIE;
                printf("Statek %d prosi o pozwolenie na wyplyniecie\n", nr_procesu);
                Wyslij(nr_procesu, stan);
            }
            else {
                Wyslij(nr_procesu, stan);
            }
        }
        else if(stan == 2) {
            printf("Wyplynalem, statek %d\n", nr_procesu);
            stan = ZEGLUGA;
            Wyslij(nr_procesu, stan);
        }
        else if(stan == 3) {
            paliwo -= rand()%500;
            if(paliwo <= REZERWA) {
                stan = KONIEC_ZEGLUGI;
                printf("Statek chce wplynac do portu\n");
                Wyslij(nr_procesu, stan);
            }
            else {
                for(i = 0; rand()%10000; i++);
            }            
        }
        else if(stan == 4) {
            int temp;
            MPI_Recv(&temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, &mpi_status);
            if(temp == WPLYN) {
                stan = PORT;
                printf("Statek o numerze %d wplywa do portu\n", nr_procesu);
            }
            else {
                paliwo -= rand()%500;
                if(paliwo > 0) {
                    Wyslij(nr_procesu, stan);
                }
                else {
                    stan = POMOC;
                    printf("Statkowi %d skonczylo się paliwo! Oczekuje na pomoc z portu.\n", nr_procesu);
                    Wyslij(nr_procesu, stan);
                }
            }
        }
        else if(stan == -2) {
            int temp;
            MPI_Recv(&temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, &mpi_status);
            if(temp == WPLYN) {
                stan = PORT;
                printf("Statek o numerze %d otrzymał pomoc i wplywa do portu\n", nr_procesu);
            }
            else {
                printf("Poczekam sobie jeszcze na pomoc. Statek %d\n", nr_procesu);
            }
            Wyslij(nr_procesu, stan);
        }
    }       
}

int main(int argc, char *argv[]) {
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &nr_procesu);
    MPI_Comm_size(MPI_COMM_WORLD, &liczba_procesow);
    srand(time(NULL));
    if(nr_procesu == 0)
        Port(liczba_procesow);
    else
        Statek();
    MPI_Finalize();
    return 0;
}
