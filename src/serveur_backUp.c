/*
 **********************************************************
 *
 *  Programme : serveur.c
 *
 *  ecrit par : Coulibaly Sidiki
 *
 *  resume :    
 *               
 *
 *  date :      03 / 04 / 2014
 *
 ***********************************************************
 */

/* include generaux */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
/* include fonctions TCP */
#include "../includes/fonctionsTCP.h"

#include "../includes/protocolQuoridor.h"
#include "../includes/validation.h"

/* taille du buffer de reception */
#define TAIL_BUF 100

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

//main
main(int argc, char** argv) {
  	int sock_cont, sock,
      	joueurs[2],       /* descripteurs des sockets locales */
      	err;	            /* code d'erreur */

  	struct sockaddr_in nom_transmis;	/* adresse de la socket de */
					                     /* transmission */
  
	char            buffer[TAIL_BUF];	/* buffer de reception */
  
  	socklen_t      size_addr_trans;	/* taille de l'adresse d'une socket */
  	
  	TypPartieReq demande[2],req;		//demande de partie
  	TypPartieRep partiRep;				// reponse d'une demande de partie
  	TypCoupReq	coup;					// requete de coup
  	TypCoupRep repCoup;					// reponse d'un coup
  
	fd_set ens, 
		ensActif,
		parti;
	
	struct timeval tv;
	
	int nbSock = 0,
		fin = 1,
		i,
		nbDemande = 0,
		file[2],
		retval;				//erreur timeval
	
    int test;
	int tailleDem = sizeof(req);
	char chaine[56];
  
  /*
   * verification des arguments
   */
	if (argc != 2) {
    	printf ("usage : serveur no_port\n");
		exit(1);
	}
  
	size_addr_trans = sizeof(struct sockaddr_in);
  
  /* 
   * creation de la socket, protocole TCP 
   */
	printf("serveur : creation de la socket sur %d\n", atoi(argv[1]));
	sock_cont = socketServeur(atoi(argv[1]));
	if (sock_cont < 0) {
    	printf("serveur : erreur socketServeur\n");
		exit(2);
	}
  
 	
	FD_ZERO(&ensActif);
	FD_SET(sock_cont, &ensActif);
  	
  	//while(1){
  		/*while( nbDemande < 2){
  			ens = ensActif;
  			if(select(FD_SETSIZE,&ens, NULL,NULL,NULL) < 0){
				perror("ERROR SELECT()");
  				exit(-1);
  			}
				if(FD_ISSET(sock_cont, &ens)){
					//printf("connexion\n");
						joueurs[nbSock] = accept(sock_cont, 
									  (struct sockaddr *)&nom_transmis, 
									  &size_addr_trans);
						if (joueurs[nbSock] < 0) {
							perror("serveur :  erreur sur accept");
							close(sock_cont);
							exit(0); 
						}
						
						printf("Connexion du joueur %d", nbSock+1);
						FD_SET(joueurs[nbSock], &ensActif);
						nbSock++;
				}
				else{
					for(i = 0; i < nbSock; i++){
					if(FD_ISSET(joueurs[i], &ens)){	
						err = recv(joueurs[i], &demande[nbDemande], tailleDem, 0);
						if (err < 0) {
							perror("serveur : erreur dans la reception 1");
							shutdown(joueurs[i], 2); close(joueurs[i]); close(sock_cont);
							exit(0);
						}
						printf("\ndonnee reçu %s \n", demande[nbDemande].nomJoueur);
						file[nbDemande] = joueurs[i];
						nbDemande++;	
					}
				}			
			}
		}*/
		//FD_ZERO(&parti);
		joueurs[0] = accept(sock_cont, 
									  (struct sockaddr *)&nom_transmis, 
									  &size_addr_trans);
		//FD_SET(joueurs[0], &parti);
		err = recv(joueurs[0], &demande[0], tailleDem, 0);
		if (err < 0) {
			perror("serveur : erreur dans la reception 1");
			shutdown(joueurs[0], 2); close(joueurs[0]); close(sock_cont);
			exit(0);
		}
		
		joueurs[1] = accept(sock_cont, 
									  (struct sockaddr *)&nom_transmis, 
									  &size_addr_trans);
		//FD_SET(joueurs[1], &parti);
		err = recv(joueurs[1], &demande[1], tailleDem, 0);
		if (err < 0) {
			perror("serveur : erreur dans la reception 1");
			shutdown(joueurs[1], 2); close(joueurs[1]); close(sock_cont);
			exit(0);
		}
		partiRep.err = ERR_OK;
		partiRep.couleur = BLANC;
		strcpy(partiRep.nomAdvers, demande[1].nomJoueur);
		partiReponse(joueurs[0], partiRep);//envoi de la reponse de la demande
		
		partiRep.err = ERR_OK;
		partiRep.couleur = NOIR;
		strcpy(partiRep.nomAdvers, demande[0].nomJoueur);
		
		partiReponse(joueurs[1], partiRep);//envoi de la reponse de la demande
		
		
	//}
	/*FD_ZERO(&parti);
	FD_SET(file[0], &parti);
	FD_SET(file[1], &parti);
	tv.tv_sec = 6;
  tv.tv_usec = 0;*/
	//while(fin){
		
		err = recv(joueurs[0], &coup, sizeof(coup), 0);
		if (err < 0) {
			perror("serveur : erreur dans la reception du coup");
			shutdown(joueurs[0], 2); close(joueurs[0]); close(sock_cont);
			exit(0);
		}
	switch(coup.idRequest){
		case COUP:
			if(validationCoup(1,coup)){
				repCoup.err = ERR_COUP;
				repCoup.validCoup = VALID;
				
				validReponse(joueurs[0],repCoup);
				validReponse(joueurs[1],repCoup);
			}
			else{
				repCoup.err = ERR_COUP;
				repCoup.validCoup = TRICHE;
				
				validReponse(joueurs[0],repCoup);
				validReponse(joueurs[1],repCoup);
				fin = 0;
			}
		break;
		default:
					printf("Erreur sur le type de requete\n");
		break;
	}
		/*
		retval = select(FD_SETSIZE,&parti, NULL,NULL,&tv);
		if(retval == - 1){
			perror("erreur select()");
		}
		else if(retval){
			for(i=0;i<2;i++){
				if(FD_ISSET(joueurs[i], &parti)){
					err = recv(joueurs[i], &coup, sizeof(coup), 0);
					if (err < 0) {
						perror("serveur : erreur dans la reception du coup");
						shutdown(joueurs[i], 2); close(joueurs[i]); close(sock_cont);
						exit(0);
					}
					printf("Reception du coup \n");
			
				switch(coup.idRequest){
					case COUP:
						printf("couleur du pion : %d\n",coup.couleurPion);
						if(coup.propCoup == 0){
							printf("Deplacement du pion :\n");
						}
						else{
							printf("Pose de mur ou coup gagnant\n");
						}
						repCoup.err = ERR_OK;
						repCoup.validCoup = VALID;
						err = send(joueurs[0], &repCoup, sizeof(repCoup), 0);
						if (err < 0) {
							perror("serveur : erreur sur le send");
							shutdown(file[0], 2); close(file[1]);
							exit(3);
						}
						err = send(joueurs[1], &repCoup, sizeof(repCoup), 0);
						if (err < 0) {
							perror("serveur : erreur sur le send");
							shutdown(file[1], 2); close(file[1]);
							exit(3);
						}
						fin = 0;
					break;
					default:
					
					break;
				}
			}
		}
		}
		else{
			printf("No data within six secondes \n");
			
			repCoup.err = ERR_COUP;
			repCoup.validCoup = TIMEOUT;
			err = send(joueurs[0], &repCoup, sizeof(repCoup), 0);
			if (err < 0) {
				perror("serveur : erreur sur le send");
				shutdown(joueurs[0], 2); close(file[1]);
				exit(3);
			}
			err = send(joueurs[1], &repCoup, sizeof(repCoup), 0);
			if (err < 0) {
				perror("serveur : erreur sur le send");
				shutdown(joueurs[1], 2); close(file[1]);
				exit(3);
			}
			fin = 0;
		}*/
	//}//fin de tanque partie
  	

	  /* 
   * arret de la connexion et fermeture
   */
  for(i = 0; i < nbSock; i++){
	shutdown(joueurs[i], 2);
	close(joueurs[i]);
  }
  close(sock_cont);
}
