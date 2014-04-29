#########
#
# Quoridor 
#
#########

# For Solaris
#LD_FLAGS = -lsocket -lnsl

# For Linux
#LD_FLAGS =
CC=gcc -g
SRC = ./src
OBJ = ./obj
BIN = ./bin
LIB = ./lib
INC = ./includes

all: $(BIN)/client  $(BIN)/serveur

$(OBJ)/fonctionsTCP.o: $(SRC)/fonctionsTCP.c $(INC)/fonctionsTCP.h
	$(CC) -c -o $(OBJ)/fonctionsTCP.o $(SRC)/fonctionsTCP.c

$(BIN)/client: $(SRC)/client.c $(OBJ)/fonctionsTCP.o
	$(CC) $(SRC)/client.c -o $(BIN)/client $(OBJ)/fonctionsTCP.o  $(LD_FLAGS)

$(BIN)/serveur: $(SRC)/serveur.c $(OBJ)/fonctionsTCP.o
	$(CC) $(SRC)/serveur.c -o $(BIN)/serveur $(OBJ)/fonctionsTCP.o  $(LD_FLAGS)

clean:
	rm *~ ; rm -i \#* ; rm *.o; \
        rm client ; rm serveur
