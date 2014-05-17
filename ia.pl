:-use_module(library(clpq)).
:- use_module(library(random)).
:- use_module(library(lists)).


departJ1([5,9]).
departJ2([5,1]).%[9,5]
arriveeJ1([_,9]).
arriveeJ2([_,1]).%[_,1]

intiPlateau(COL, M, A,[[[1,2],[2,3]]]):-
	COL == 0,
	departJ1(M),
	departJ2(A).
intiPlateau(COL, M, A,[[1,2]]):-
	COL == 1,
	departJ2(M),
	departJ1(A).
	
%()
member_dif(X,[X|L],L).
member_dif(X,[E|L],[E|R]):-
        member_dif(X,L,R).

nouvelleCase([X,Y],[X1,Y]):-
        X1 is X+1.

nouvelleCase([X,Y],[X1,Y]):-
        X1 is X-1.

nouvelleCase([X,Y],[X,Y1]):-
        Y1 is Y+1.

nouvelleCase([X,Y],[X,Y1]):-
        Y1 is Y-1.
%mettre un mur horizontale X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultane  Nb+ nombre de murs   
mettre_murH(X,A,LL):-
        
        case_adjHP(X,Z),
        case_adjVP(X,R),
        case_adjVP(Z,R1),
        case_adjHP(R,R1),
        caseCorrecte(X),
        caseCorrecte(R1),
        caseCorrecte(R),
        caseCorrecte(Z),
        \+ member([X,R],A),
        \+ member([R,X],A),
        append([[X,R]],A,LL1),
        \+ member([R1,Z],A),
        \+ member([Z,R1],A),
        append([[Z,R1]],LL1,LL).
        
%mettre un mur verticale X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultane  Nb+ nombre de murs   
mettre_murV(X,A,LL):-
        
        case_adjHP(X,Z),
        case_adjVP(X,R),
        case_adjVP(Z,R1),
        case_adjHP(R,R1),
        caseCorrecte(X),
        caseCorrecte(R1),
        caseCorrecte(R),
        caseCorrecte(Z),
        \+ member([X,Z],A),
        \+ member([Z,X],A),
        append([[X,Z]],A,LL1),
        \+ member([R1,R],A),
        \+ member([R,R1],A),
        
        append([[R,R1]],LL1,LL).
        
%mettre un mur horizontale ou verticale en comptant le nombre de murs  X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultane  Nb+ nombre de murs   
mettre_mur(MR,MR1,Nb,Nb1):-
        Nb>0,
        Nb1 is Nb-1,
        caseC(X),
        mettre_murH(X,MR,MR1).
mettre_mur(MR,MR1,Nb,Nb1):-
        Nb>0,
        Nb1 is Nb-1,
        caseC(X),
        mettre_murV(X,MR,MR1).

%verifie qu'une case corecte on a pris le choix d'ecrire ce prédicat au lieu d'unifier avec caseC pour des raisons d'optimisations (+)
caseCorrecte([X,Y]):-
        X > 0,
        X < 10,
        Y > 0,
        Y < 10.
% case adjacente a droite  utiliser uniquement pour generer la 2éme case du mur verticalement
case_adjVP([X,Y],[X1,Y]):-X1 is X+1.

case_adjVN([X,Y],[X1,Y]):-X1 is X-1.

% case adjacente verticale 
case_adjV([X,Y],[X1,Y]):-X1 is X-1.
case_adjV([X,Y],[X1,Y]):-X1 is X+1.

% case adjacente horizontale (+,-)
case_adjH([X,Y],[X,Y1]):-Y1 is Y-1.
case_adjH([X,Y],[X,Y1]):-Y1 is Y+1.
% case adjacente a droite  utiliser uniquement pour generer la case du mur verticalement
case_adjHP([X,Y],[X,Y1]):-Y1 is Y+1.

case_adjHN([X,Y],[X,Y1]):-Y1 is Y-1.

case_adj(C,C2):-case_adjH(C,C2).
case_adj(C,C2):-case_adjV(C,C2).
% C+ case actuelle ,C2- nouvelle_case  ,liste des murs  R 
deplacementCorrect(C,C2,R):-
        caseCorrecte(C2),
        caseCorrecte(C)
       ,\+ member([C,C2],R),
        \+ member([C2,C],R).

% liste de case ; liste de case resultante , liste de couple de liste ( mur)
deplacer([C|P],[N,C|P],MUR,CA):-
        nouvelleCase(C,N),
        N\=CA ,
        deplacementCorrect(C,N,MUR),
        \+ member(N,[C|P]).

%saut(+,-,+,+) dans le cas ou notre case actuelle(c) est adjacente(h/v) avec la case actuelle de l'adversaire(ca)
%saut horizontal ca|c--c|ca  
saut([C|P],[N,C|P],MUR,CA):-
        case_adjH(C,CA),
        case_adjH(CA,N),
        deplacementCorrect(C,CA,MUR),
        deplacementCorrect(CA,N,MUR),
        \+ member(N,[C|P]).
%saut vertical c-- ca
%              _   _
%             ca   c
saut([C|P],[N,C|P],MUR,CA):-
        case_adjV(C,CA),
        case_adjV(CA,N),
        deplacementCorrect(C,CA,MUR),
        deplacementCorrect(CA,N,MUR),
        \+ member(N,[C|P]).
% saut lateral vertical L
saut([C|P],[N,C|P],MUR,CA):-
        case_adjV(C,CA),
        case_adjV(CA,N1),
        deplacementCorrect(C,CA,MUR),
        \+deplacementCorrect(CA,N1,MUR),
        case_adjH(CA,N),
        deplacementCorrect(CA,N,MUR),
        \+ member(N,[C|P]).
%saut lateral horizontal
saut([C|P],[N,C|P],MUR,CA):-
        case_adjH(C,CA),
        case_adjH(CA,N1),
        deplacementCorrect(C,CA,MUR),
        \+deplacementCorrect(CA,N1,MUR),
                case_adjV(CA,N),
        deplacementCorrect(CA,N,MUR),
        \+ member(N,[C|P]).


     
%mur_string([Y,X]):-case_adjV(Y,X),!.
%mur_string([Y,X|L]):-case_adjV(Y,X),mur_string(L).
%case_extreme c'est une case qui se trouve dans les bords du plateau
case_extreme([X,_]):-X==1.
case_extreme([X,_]):-X==9.
case_extreme([_,Y]):-Y==1.
case_extreme([_,Y]):-Y==9.
case_coin([X,Y]):-case_extreme([X,_]),case_extreme([_,Y]).
% un mur extréme  est un mur dont l'une de ses case est extréme
mur_extreme([X,_]):-case_extreme(X).
mur_extreme([_,Y]):-case_extreme(Y).

%string(_,0):-!.
string([A|_],1):-mur_extreme(A),!.

string([A|L],2):-mur_extreme(A),mur_string([A|L]),string(L,1).

%string([A|L],2):-mur_string([A|L]).
%
string([X|L],Nb):-string(L,Nb).

%mur_string([[X1,Y1],[X2,Y2]]):-case_adjV(X1,X2),case_adjV(Y1,Y2),!.
%mur_string([[X1,Y1],[X2,Y2]|L]):-case_adjV(X1,X2),case_adjV(Y1,Y2),mur_string(L).
mur_string([[X1,Y1],[X2,Y2]]):-case_adj(X1,X2),case_adj(Y1,Y2),!.
mur_string([[X1,Y1],[X2,Y2]|L]):-case_adj(X1,X2),case_adj(Y1,Y2),mur_string([[X2,Y2]|L]).
mur_string([[X1,Y1],[X2,Y2]|L]):-case_adj(X1,X2),case_adj(Y1,Y2),mur_string([[X1,Y1]|L]).

mur_string([[X1,_],T]):-member(X1,T),!.%coin
mur_string([[X1,_],T|L]):-member(X1,T),mur_string(L).
mur_string([[_,Y1],T]):-member(Y1,T),!.
mur_string([[_,Y1],T|L]):-member(Y1,T),mur_string(L).
%a revoir 
mur_string([Z,_|L]):-mur_string([Z|L]).
%a revoir mur_string([_,Z|L]):-mur_string([Z|L]).

mur_adj([C,_],[S,_]):-case_adj(C,S).
mur_adj([C,_],[_,S2]):-case_adj(C,S2).
mur_adj([_,C2],[S,_]):-case_adj(C2,S).
mur_adj([_,C2],[_,S2]):-case_adj(C2,S2).

%str([A,X|L],):-
% liste de case , liste resultante , CA coup adversaire 
%jouer([case_actuelle|L],liste_resultane,MUR_actuel,Mur_resultat,Nbr_Mur,Case_Actuelle_adversaire):-

jouer([C|P],R1,MR,MR,Nb,Nb,CA):-
        deplacer([C|P],R1,MR,CA).

jouer([C|P],R1,MR,MR,Nb,Nb,CA):-
        saut([C|P],R1,MR,CA).

        

jouer(R,R,MR,MR1,Nb,Nb1,CA):-
     mettre_mur(MR,MR1,Nb,Nb1),
     \+string(MR1,2).
