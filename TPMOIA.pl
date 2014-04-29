:- set_prolog_flag(toplevel_print_options, [quoted(true), portray(true), max_depth(0), attributes(portray)]).

:-use_module(library(clpq)).

%generer une case corecte 
caseC([1,1]). caseC([2,1]). caseC([3,1]). caseC([4,1]). caseC([5,1]). caseC([6,1]). caseC([7,1]). caseC([8,1]). caseC([9,1]).
caseC([1,2]). caseC([2,2]). caseC([3,2]). caseC([4,2]). caseC([5,2]). caseC([6,2]). caseC([7,2]). caseC([8,2]). caseC([9,2]). 
caseC([1,3]). caseC([2,3]). caseC([3,3]). caseC([4,3]). caseC([5,3]). caseC([6,3]). caseC([7,3]). caseC([8,3]). caseC([9,3]).
caseC([1,4]). caseC([2,4]). caseC([3,4]). caseC([4,4]). caseC([5,4]). caseC([6,4]). caseC([7,4]). caseC([8,4]). caseC([9,4]).
caseC([1,5]). caseC([2,5]). caseC([3,5]). caseC([4,5]). caseC([5,5]). caseC([6,5]). caseC([7,5]). caseC([8,5]). caseC([9,5]).
caseC([1,6]). caseC([2,6]). caseC([3,6]). caseC([4,6]). caseC([5,6]). caseC([6,6]). caseC([7,6]). caseC([8,6]). caseC([9,6]).
caseC([1,7]). caseC([2,7]). caseC([3,7]). caseC([4,7]). caseC([5,7]). caseC([6,7]). caseC([7,7]). caseC([8,7]). caseC([9,7]).
caseC([1,8]). caseC([2,8]). caseC([3,8]). caseC([4,8]). caseC([5,8]). caseC([6,8]). caseC([7,8]). caseC([8,8]). caseC([9,8]).
caseC([1,9]). caseC([2,9]). caseC([3,9]). caseC([4,9]). caseC([5,9]). caseC([6,9]). caseC([7,9]). caseC([8,9]). caseC([9,9]).



departJ1([1,5]).%[9,5]
departJ2([9,5]).
arriveeJ1([9,_]).
arriveeJ2([1,_]).%[_,1]
%()
member_dif(X,[X|L],L).
member_dif(X,[E|L],[E|R]):-
        member_dif(X,L,R).
nouvelleCase([X,Y],[X1,Y]):-
        X1 is X+1.

nouvelleCase([X,Y],[X,Y1]):-
        Y1 is Y+1.

nouvelleCase([X,Y],[X,Y1]):-
        Y1 is Y-1.


nouvelleCase([X,Y],[X1,Y]):-
        X1 is X-1.

%mettre un mur horizontale X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultante  Nb+ nombre de murs   
mettre_murH(X,A,LL,Nb):-
        Nb>0,
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

%% X|Z
%  R|R1
%mettre un mur verticale X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultane  Nb+ nombre de murs   
mettre_murV(X,A,LL,Nb):-
        Nb > 0,
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

%mettre un mur horizontale ou verticale en comptant le nombre de murs  X+:case de dpart du mur  A+: liste des murs  Ll- liste de murs resultante  Nb+ nombre de murs   
mettre_mur(MR,MR1,Nb):-
        Nb1 is Nb-1,
        caseC(X),
        mettre_murH(X,MR,MR1,Nb1).
mettre_mur(MR,MR1,Nb):-
        Nb1 is Nb-1,
        caseC(X),
        mettre_murV(X,MR,MR1,Nb1).

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
        caseCorrecte(C),
        \+ member([C,C2],R),
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

string(_,0):-!.
string([A|L],Nb):-case_extreme(A),Nb1 is Nb-1,string([A|L],Nb1).

string([A|L],_):-mur_string([A|L]).
%
string([X|L],Nb):-member_dif(X,[X|L],L2),string(L2,Nb).
%string([A]):-mur_extreme(A).

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
canal([],_,R):-!.
canal([[C,_]|L],LMC,[C|R]):-casecanal(C,LMC),canal(L,LMC,[C|R]).
canal([[_,C]|L],LMC,[C|R]):-casecanal(C,LMC),canal(L,LMC,[C|R]).

canal([C|L],LMC,[C|R]):-canal(L,LMC,[C|R]).
%2mur paraléle 
%switché les members !
casecanal(C,LM):- case_adjVP(C,C1),member([C,C1],LM),case_adjVN(C,C2),member([C,C2],LM),case_adjH(C,C3),\+member([C,C3],LM).%,casecanal(C3,LM,[C3,C|L],R).
casecanal(C,LM):- case_adjHP(C,C1),member([C,C1],LM),case_adjHN(C,C2),member([C,C2],LM),case_adjV(C,C3),\+member([C,C3],LM).%,casecanal(C3,LM,[C3,C|L],R).

% 1 mur paralél a un extréme 

casecanal(C,LM):- case_adjV(C,C1),member([C,C1],LM),case_extreme(C),case_adjH(C,C3),\+member([C,C3],LM).%,casecanal(C3,LM,[C3,C|L]).
casecanal(C,LM):- case_adjH(C,C1),member([C,C1],LM),case_extreme(C),case_adjV(C,C3),\+member([C,C3],LM).%,casecanal(C3,LM,[C3,C|L]).

% mur croisé 
casecanal(C,LM):- case_adjH(C,C1),caseCorrecte(C1),case_adjV(C,C2),caseCorrecte(C2),case_adjH(C2,C3),caseCorrecte(C3),member([C2,C3],LM),member([C1,C3],LM).%,casecanal(C3,LM,[C3,C|L],R).
casecanal(C,LM):- case_adjV(C,C1),caseCorrecte(C1),case_adjH(C,C2),caseCorrecte(C2),case_adjV(C2,C3),caseCorrecte(C3),member([C2,C3],LM),member([C1,C3],LM).
casecanal(C,LM):-case_coin(C).%,case_adjH(C,C3),\+member([C,C3],LM).%,casecanal(C3,LM,[C3,C|L],R).
casecanal(C,LM):-case_coin(C).%,case_adjV(C,C3),\+member([C,C3],LM).%, casecanal(C3,LM,[C3,C|L],R).
%casecanal(C,LM,R):-!.


mur_adj([C,_],[S,_]):-case_adj(C,S).
mur_adj([C,_],[_,S2]):-case_adj(C,S2).
mur_adj([_,C2],[S,_]):-case_adj(C2,S).
mur_adj([_,C2],[_,S2]):-case_adj(C2,S2).

%str([A,X|L],):-
% liste de case , liste resultante , CA coup adversaire 
jouer([C|P],R1,MR,_,CA):-
        deplacer([C|P],R1,MR,CA).
        

jouer(_,_,MR,MR1,_,Nb):-
    
     mettre_mur(MR,MR1,Nb),
     \+string(MR1,2).
     

parcours([C|L],[C|L],_,_):- arriveeJ1(C).
parcours(Ch, Sol,MR,MR1):-
                jouer(Ch, NvCh,MR,MR2,_),
                parcours(NvCh, Sol,MR2,MR1).

%distance(Ca+,Mur+,Res-)
distance([X,Y],Mur,R):-case_adjVP([X,Y],Succ),deplacementCorrect([X,Y],Succ,Mur),arriveeJ1([X1,_]),R is X-X1,!. % mettre valeur absolue 
distance(Ca,Mur,_):-case_adjVP(Ca,Succ),
        %deplacementCorrect(Ca,Succ,Mur),
        distance(Succ,Mur,_)  .


avancer([C|P],[N,C|P],MUR):-
         nouvelleCase(C,N),
        
        deplacementCorrect(C,N,MUR),
        \+ member(N,[C|P]).

%trouver un chemin  vers le canal
%éléminer tous les element entre un doublons 
%Opt_esquive([],R).
%% X1 Y1
%  _ _





%  X2 Y
%deplacer vers une case dont cette case peut faire un string avec 
%deplacer([C|P],[N,C|P],MUR,CA):-


esquive([CA|L],LMC,LMC,But,[But,CA|L]):-avancer([CA|L],[But,CA|L],LMC),!.
%partie une a enlver c juste pour tester 
esquive([CA|L],LM,LMC,But,R):-
       avancer([CA|L],[N,CA|L],LMC),
       
       esquive([N,CA|L],LM,LMC,But,R).


esquive([CA|L],[[C1,P]|LM],LMC,But,R):-
        avancer([CA|L],[C1,CA|L],LMC),
        member_dif([C1,P],[[C1,P]|LM],LM1),
        esquive([C1,CA|L],LM1,LMC,But,R).
esquive([CA|L],[[P,C2]|LM],LMC,But,R):-
        avancer([CA|L],[C2,CA|L],LMC),
                member_dif([P,C2],[[P,C2]|LM],LM1),

        esquive([C2,CA|L],LM1,LMC,But,R).




esquivate([[X1,Y1]|L2],[[X2,Y2]|L]):-case_adjV(X1,X2),case_adjV(X1,X2),esquivate([[X1,Y1]|L2],[[X2,Y2]|L]).
labyrinthe(Sol):-departJ1(C),
                 parcours([C],Sol,R,R2).
%comter les murs // done 
%verifier placer murs et les forcer d'un coté
% mettre h pour horizontal et v pour vertical 
%revoir string en entier  et tester avec un jeux test complexe 
