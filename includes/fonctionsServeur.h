/******************************************************************************
 *
 * Programme : coup.h
 *
 * Synopsis  : déclaration des fonctions de gestion des coups.
 *
 * Ecrit par :COULIBALY Sidiki et Hammani abdelhamid (17/04/14)
 *
 *****************************************************************************/
#ifndef FONCTIONSERVEURS_H
#define FONCTIONSERVEURS_H
/* include generaux */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>

#include "../includes/protocolQuoridor.h"

/*
 * Fonction    : partiReponse
 *
 * Parametres  : int joueur, la socket de communication avec le joueur qui fait la demande.
 * 				 TypePartiRep rep, la réponse d'une demande de partie envoyée par l'arbitre à
 * 				                     envoyée au joueur.
 *
 * Retour      : rien.
 *
 * Description : renvoie la reponse d'une demande de partie.
 */
void partiReponse(int joueur, TypPartieRep partiRep);

/*
 * Fonction    : validReponse
 *
 * Parametres  : int joueur, la socket de communication avec le joueur.
 * 				 TypCoupRep rep, la réponse de validite du coup envoyée par l'arbitre à
 * 				                     l'adversaire et aussi envoyée au joueur.
 *
 * Retour      : rien.
 *
 * Description : attend les informations sur le coup de l'adversaire.
 */
void validReponse(int joueur, TypCoupRep rep);


/*
 * Fonction    : envoyerCoup
 *
 * Parametres  : int joueur, la socket de communication avec le joueur.
 * 				 TypCoupReq coup, le coup envoyée par l'arbitre à
 * 				                     l'adversaire.
 *
 * Retour      : rien.
 *
 * Description : envoie un coup valide à l'adversaire.
 */
void envoyerCoup(int joueur, TypCoupReq coup);

#endif
