/**************************************
 *
 * Programme : protocolQuoridor.h
 *
 * Synopsis : entete du protocole d'acces a l'arbitre
 *            pour Quoridor
 *
 * Ecrit par : VF, LP
 * Date :  07 / 03 / 14
 * 
/***************************************/

//#ifndef _protocolQuoridor_h
//#define _protocolQuoridor_h

/* Taille des chaines */
#define TAIL_CHAIN 30

/* Identificateurs des requetes */
typedef enum { PARTIE, COUP } TypIdRequest;

/* Types d'erreur */
typedef enum { ERR_OK,      /* Validation de la requete */
	       ERR_PARTIE,  /* Erreur sur la demande de partie */
	       ERR_COUP,    /* Erreur sur le coup joue */
	       ERR_TYP      /* Erreur sur le type de requete */
} TypErreur;

/* 
 * Structures demande de partie
 */
typedef struct{

  TypIdRequest idRequest;      /* Identificateur de la requete */
  char nomJoueur[TAIL_CHAIN];  /* Nom du joueur */

} TypPartieReq;

typedef enum { BLANC, NOIR } TypPion;

typedef struct {

  TypErreur  err;               /* Code d'erreur */
  TypPion    couleur;           /* Couleur du pion */
  char nomAdvers[TAIL_CHAIN];   /* Nom du joueur */

} TypPartieRep;

/*
 * Definition des types pour les deux axes
 */
typedef enum { AXE_A, AXE_B, AXE_C, AXE_D, 
	       AXE_E, AXE_F, AXE_G, AXE_H, AXE_I } TypAxeLettre;

typedef enum { AXE_UN, AXE_DEUX, AXE_TROIS, AXE_QUATRE, 
	       AXE_CINQ, AXE_SIX, AXE_SEPT, AXE_HUIT, AXE_NEUF } TypAxeChiffre;

/* 
 * Definition d'une case
 */
typedef struct {

  TypAxeLettre  axeLettre;   /* Numerotation abscisse = lettre */
  TypAxeChiffre axeChiffre;  /* Numerotation ordonnee = chiffre */

} TypCase ;

/* 
 * Definition d'un deplacement de pion
 */
typedef struct {

  TypCase  caseDepPion;  /* Position de depart du pion */
  TypCase  caseArrPion;  /* Position d'arrivee du pion */

} TypDeplPion;

/* 
 * Definition d'un positionnement de mur
 */
typedef enum {HOR, VER} TypOrient;

typedef struct {

  TypCase   caseMur;     /* Premiere case du mur */
  TypOrient orientMur;   /* Orientation du mur */

} TypPosMur;

/* 
 * Structures coup du joueur 
 */

/* Propriete des coups */
typedef enum { DEPL_PION, POS_MUR, GAGNANT } TypCoup;

typedef struct {

  TypIdRequest idRequest;     /* Identificateur de la requete */
  TypPion      couleurPion;   /* Couleur du pion */
  TypCoup      propCoup;      /* Type de coup */
  union{
    TypDeplPion deplPion;     /* Deplacement du pion */
    TypPosMur   posMur;       /* Position du mur */
  };

} TypCoupReq;

/* Validite du coup */
typedef enum { VALID, TIMEOUT, TRICHE } TypValCoup;

/* Reponse a un coup */
typedef struct {

  TypErreur err;                  /* Code d'erreur */
  TypValCoup validCoup;           /* Validite du coup */

} TypCoupRep;

//#endif;
