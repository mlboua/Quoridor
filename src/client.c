/*
 **********************************************************
 *
 *  Programme : client.c
 *
 *  ecrit par : LP.
 *
 *  resume :    envoi une chaine de caracteres a un programme serveur
 *               en mode connecte
 *
 *  date :      25 / 01 / 06
 *
 ***********************************************************
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../includes/fonctionsTCP.h"
#include "../includes/protocolQuoridor.h"

#define TAIL_BUF 100
main(int argc, char **argv) {
  char chaine[TAIL_BUF] ;
  int sock,               /* descripteur de la socket locale */
      err;                /* code d'erreur */

	TypPartieReq demande;
  	TypPartieRep rep;
  	TypCoupReq coup;
  	TypCoupRep repCoup;
  	
  	demande.idRequest = PARTIE;
  /*
   * verification des arguments
   */
  if (argc != 3) {
    printf("usage : client nom_machine no_port\n");
    exit(1);
  }
  
  /* 
   * creation d'une socket, domaine AF_INET, protocole TCP 
   */
  printf("client : connect to %s, %d\n", argv[1], atoi(argv[2]));
  sock = socketClient(argv[1], atoi(argv[2]));
  if (sock < 0) { 
    printf("client : erreur socketClient\n");
    exit(2);
  }
  
 
  printf("client : nom du joueur :\t");
  scanf("%s",demande.nomJoueur);
  

  err = send(sock, &demande, sizeof(demande), 0);
  if (err < 0) {
    perror("client : erreur sur le send");
    shutdown(sock, 2); close(sock);
    exit(3);
  }
  printf("client : envoi realise\n");
  
  err = recv(sock, &rep, sizeof(rep), 0);
  if (err < 0) {
    perror("client : erreur dans la reception");
    shutdown(sock, 2);close(sock);
    exit(4);
  }
  if(rep.err != ERR_OK){
  	printf("client : Une erreur est survenue lors de l'operation \n");
  	exit(5);
  }
  printf("client : nom de l'adversiare : %s et couleur du pion %d\n", rep.nomAdvers,rep.couleur);
  
	if(rep.couleur == 0){
		printf("Envoi du coup\n");
		coup.idRequest = COUP;
	  coup.couleurPion = BLANC;
	  coup.propCoup = POS_MUR;
	  coup.posMur.caseMur.axeLettre = AXE_E;
	  coup.posMur.caseMur.axeChiffre = AXE_UN;
	  coup.posMur.orientMur = HOR;
	  //coup.posMur.caseArrPion.axeLettre = AXE_E;
	  err = send(sock, &coup, sizeof(coup), 0);
		if (err < 0) {
			perror("client : erreur sur le send");
			shutdown(sock, 2); close(sock);
			exit(3);
		}
		printf("Coup envoyé\n");
	}
  	err = recv(sock, &repCoup, sizeof(repCoup), 0);
  	if (err < 0) {
    	perror("client : erreur dans la reception");
    	shutdown(sock, 2);close(sock);
    	exit(4);
  	}
  	printf("Coup de l'adversaire : \n");
  	coup.idRequest = COUP;
	  coup.couleurPion = BLANC;
	  coup.propCoup = DEPL_PION;
	  coup.deplPion.caseDepPion.axeLettre = AXE_E;
	  coup.deplPion.caseDepPion.axeChiffre = AXE_UN;
	  coup.deplPion.caseArrPion.axeChiffre = AXE_DEUX;
	  coup.deplPion.caseArrPion.axeLettre = AXE_E;
	  err = send(sock, &coup, sizeof(coup), 0);
		if (err < 0) {
			perror("client : erreur sur le send");
			shutdown(sock, 2); close(sock);
			exit(3);
		}
  
  /* 
   * fermeture de la connexion et de la socket 
   */
  shutdown(sock, 2);
  close(sock);
}
 

