#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define ROUGE   "\033[1;31m"
#define VERT    "\033[1;32m"
#define JAUNE   "\033[1;33m"
#define BLEU    "\033[1;34m"
#define MAGENTA "\033[1;35m"
#define CYAN    "\033[1;36m"
#define RESET   "\033[0m"

typedef struct {
    char nom[20];
    int miseMin;
    int nbPaquets;
    int niveauIA;
} Casino;

char *nomsValeurs[] = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Valet", "Dame", "Roi"};
char *nomsCouleurs[] = {"Coeur", "Carreau", "Pique", "Trefle"};

int calculerPoints(int valeurBrute) {
    if (valeurBrute >= 10) {
        return 10;
    } else {
        return valeurBrute + 1;
    }
}

int jouerNiveau(Casino leCasino) {
    system("cls");

    printf(JAUNE "\n=== CASINO DE %s ===\n" RESET, leCasino.nom);

    int sabot[416]; 
    int totalCartes = leCasino.nbPaquets * 52;
    int i;
    int score = 0, choix = 0, pioche = 0;

    for (i = 0; i < totalCartes; i++) {
        sabot[i] = i % 52;
    }

    int temp, alea;
    for (i = 0; i < totalCartes; i++) {
        alea = rand() % totalCartes;
        temp = sabot[i];
        sabot[i] = sabot[alea];
        sabot[alea] = temp;
    }

    printf("Croupier : Jeu melange. C'est parti !\n");

    do {
        int idCarte = sabot[pioche];
        pioche++;

        int valeur = idCarte % 13; 
        int couleur = idCarte / 13; 

        char* coulTexte = (couleur <= 1) ? ROUGE : CYAN;
        
        printf("\nTu as tire : %s%s de %s" RESET "\n", coulTexte, nomsValeurs[valeur], nomsCouleurs[couleur]);

        score += calculerPoints(valeur);
        printf("Ton score total : " VERT "%d" RESET "\n", score);

        if (score < 21) {
            printf("Encore ? (1=Oui, 0=Non) : ");
            scanf("%d", &choix);
        } else {
            choix = 0;
        }

    } while(score < 21 && choix == 1);

    if (score > 21) {
        printf(MAGENTA "\nBOOM ! %d. Perdu.\n" RESET, score);
        return 0;
    } 
    else if (score < 21) {
        printf("\n--- Tour de la BANQUE ---\n");
        int scoreia = 0;

        while (scoreia <= score && scoreia < 21) {
            int idCarte = sabot[pioche];
            pioche++;
            
            int valeur = idCarte % 13;
            int couleur = idCarte / 13;
            
            char* coulTexte = (couleur <= 1) ? ROUGE : CYAN;

            printf("Banque tire : %s%s de %s" RESET, coulTexte, nomsValeurs[valeur], nomsCouleurs[couleur]);
            
            scoreia += calculerPoints(valeur);
            printf(" -> Total: " BLEU "%d\n" RESET, scoreia);
        }

        if (scoreia > 21) {
            printf(VERT "\nLa banque explose ! VICTOIRE !\n" RESET);
            return 1;
        } else {
            printf(ROUGE "\nBanque (%d) bat Joueur (%d).\n" RESET, scoreia, score);
            return 0;
        }
    } else {
        printf(VERT "\nBLACKJACK (21) ! Gagne !\n" RESET);
        return 1;
    }
}

int main() {
    srand(time(NULL));

    Casino tourDuMonde[5] = {
        {"Paris", 100, 3, 1},
        {"Marseille", 100, 4, 2},
        {"Lyon", 100, 5, 3},
        {"Brest", 100, 6, 4},
        {"Monaco", 100, 7, 5}
    };

    int i;
    for (i = 0; i < 5; i++) {
        if (jouerNiveau(tourDuMonde[i]) == 0) {
            printf(ROUGE "\nGame Over.\n" RESET);
            break;
        }
        printf(VERT "\nNiveau reussi ! Appuyez sur Entree...\n" RESET);
        fflush(stdin); 
        getchar();
    }
    
    if (i == 5) {
        printf(JAUNE "\nVICTOIRE TOTALE !\n" RESET);
        getchar();
    }

    return 0;
}