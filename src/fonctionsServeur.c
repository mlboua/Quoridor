/******************************************************************************
 *
 * Programme : coup.h
 *
 * Synopsis  : déclaration des fonctions de gestion des coups.
 *
 * Ecrit par :COULIBALY Sidiki et Hammani abdelhamid (17/04/14)
 *
 *****************************************************************************/
 
#include "../includes/fonctionsServeur.h"

void validReponse(int joueur, TypCoupRep rep){
	//int err;
	//err = send(joueur, &rep, sizeof(rep), 0);
	if (send(joueur, &rep, sizeof(rep), 0) < 0) {
		perror("serveur : erreur sur le send de la validite du coup");
		shutdown(joueur, 2); close(joueur);
		exit(3);
	}
}

void partiReponse(int joueur, TypPartieRep partiRep){
	//err = send(joueur, &partiRep, sizeof(partiRep), 0);
	if (send(joueur, &partiRep, sizeof(partiRep), 0) < 0) {
		perror("serveur : erreur sur le send de la reponse de partie");
		shutdown(joueur, 2); close(joueur);
		exit(3);
	}
}

void envoyerCoup(int joueur, TypCoupReq coup){
	//err = send(sock, &coup, sizeof(coup), 0);
	if (send(joueur, &coup, sizeof(coup), 0) < 0) {
		perror("client : erreur sur le send d'envoi du coup");
		shutdown(joueur, 2); close(joueur);
		exit(3);
	}
	//printf("Coup envoyé\n");
}
