/*
 **********************************************************
 *
 *  Programme : validation.h
 *
 *  ecrit par : FB / LP / VF
 *
 *  resume : entete pour la validation des coups
 *
 *  date :      05 / 04 / 14
 *  modifie : 
 ***********************************************************
 */

#ifndef _validation_h
#define _validation_h

/* Validation d'un coup on doit passer :
 * le numero du joueur 1 pour A et 2 pour B
 * le type de coup (TypPropCoup)
 Elle renvoie TypBooleen */
extern TypBooleen validationCoup( int joueur, TypCoupReq coup);

#endif 
