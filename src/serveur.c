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


/* include fonctions TCP */
#include "../includes/fonctionsTCP.h"

#include "../includes/fonctionsServeur.h"
#include "../includes/validation.h"


//main
main(int argc, char** argv) {
  	int sock_cont, sock,
      	joueurs[2],       /* descripteurs des sockets locales */
      	err;	            /* code d'erreur */

  	struct sockaddr_in nom_transmis;	/* adresse de la socket de */
					                     /* transmission */
  
  
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
  
 	
	joueurs[0] = accept(sock_cont, 
									  (struct sockaddr *)&nom_transmis, 
									  &size_addr_trans);
									  
	err = recv(joueurs[0], &demande[0], tailleDem, 0);
	if (err < 0) {
		perror("serveur : erreur dans la reception 1");
		shutdown(joueurs[0], 2); close(joueurs[0]); close(sock_cont);
		exit(0);
	}
		
	joueurs[1] = accept(sock_cont, 
									  (struct sockaddr *)&nom_transmis, 
									  &size_addr_trans);
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
		
		
	/*err = recv(joueurs[0], &coup, sizeof(coup), 0);
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
	}*/
	
	FD_ZERO(&parti);
	FD_SET(joueurs[0], &parti);
	FD_SET(joueurs[1], &parti);
	tv.tv_sec = 6;
    tv.tv_usec = 0;
	while(fin){
	FD_ZERO(&parti);
	FD_SET(joueurs[0], &parti);
	FD_SET(joueurs[1], &parti);
	tv.tv_sec = 6;
  	tv.tv_usec = 0;
  
		retval = select(FD_SETSIZE,&parti, NULL,NULL,&tv);
		if(retval == - 1){
			perror("erreur select()");
		}
		else if(retval){
			for(i=0;i<2;i++){
				if(FD_ISSET(joueurs[i], &parti)){
					if(joueurs[i] == joueurs[0]){
						//printf("Reception du coup du jouer 1\n");
						err = recv(joueurs[0], &coup, sizeof(coup), 0);
						if (err < 0) {
							perror("serveur : erreur dans la reception du coup");
							shutdown(joueurs[0], 2); close(joueurs[0]); close(sock_cont);
							exit(0);
						}
						//traitement d'un coup
						switch(coup.idRequest){
							case COUP:
								if(validationCoup(1,coup)){
									repCoup.err = ERR_COUP;
									repCoup.validCoup = VALID;
				
									validReponse(joueurs[0],repCoup);
									validReponse(joueurs[1],repCoup);
									envoyerCoup(joueurs[1], coup);
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
								fin = 0;
							break;
						}//fin de switch
					}//fin de traitement du joueur 1
					else{
						//printf("Reception du coup du jouer 2\n");
						err = recv(joueurs[1], &coup, sizeof(coup), 0);
						if (err < 0) {
							perror("serveur : erreur dans la reception du coup");
							shutdown(joueurs[1], 2); close(joueurs[1]); close(sock_cont);
							exit(0);
						}
						//traitement d'un coup
						switch(coup.idRequest){
							case COUP:
								if(validationCoup(2,coup)){
									repCoup.err = ERR_COUP;
									repCoup.validCoup = VALID;
				
									validReponse(joueurs[1],repCoup);
									validReponse(joueurs[0],repCoup);
									envoyerCoup(joueurs[0], coup);
								}
								else{
									repCoup.err = ERR_COUP;
									repCoup.validCoup = TRICHE;
				
									validReponse(joueurs[1],repCoup);
									validReponse(joueurs[0],repCoup);
									fin = 0;
								}
							break;
							default:
										printf("Erreur sur le type de requete\n");
							break;
						}//fin de switch
					}
				}
			}
		}
		else{
			printf("No data within six secondes \n");
			
			repCoup.err = ERR_COUP;
			repCoup.validCoup = TIMEOUT;
			validReponse(joueurs[0],repCoup);
			validReponse(joueurs[1],repCoup);
			fin = 0;
		}
	}//fin de tanque partie
  	

	  /* 
   * arret de la connexion et fermeture
   */
  for(i = 0; i < nbSock; i++){
	shutdown(joueurs[i], 2);
	close(joueurs[i]);
  }
  close(sock_cont);
}
