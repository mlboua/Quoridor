//package joueur;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*import se.sics.jasper.ConversionFailedException;
import se.sics.jasper.IllegalTermException;
import se.sics.jasper.Query;
import se.sics.jasper.SICStus;
import se.sics.jasper.SPException;
import se.sics.jasper.SPTerm;*/

public class Joueur {
	private static int PARTIE = 0;
	public static void main(String[] args) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out;
		//SICStus sicstus = null;
		//HashMap<String, SPTerm> solutions = new HashMap<String, SPTerm>();
		String queryStr;
		String queryPartStr;
		boolean fin = true, premier = true;
		CoupRep coupEnemy;
		Scanner sc = new Scanner(System.in);
		try {
			socket = new Socket("localhost", 2014);
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			System.out.print("Nom du joueur : ");
			String nom = sc.nextLine();
						
			startGame(out,PARTIE, nom);
			PartiRep rep = reponsePartie(in);
			
			/*sicstus = new SICStus();
			sicstus.load("/home/boua/Documents/projets/Quoridor/TPMOIA.pl");
			//sicstus.query("set_prolog_flag(gc, off).", null);
			
			queryStr = "intiPlateau(0,MOI,ADV,WALL).";
			Query qu = sicstus.openQuery(queryStr,solutions);
			qu.nextSolution();
			//SPTerm tor = (SPTerm)solutions.get("MOI");
			//System.out.println(tor.toTermArray().clone()[0].getInteger());
			Plateau plateau = getPlateau(solutions);
			System.out.println(plateau.toString());*/
			System.out.println("[MOI] couleur du pion : "+(rep.couleur == 0 ? "BLANC" : "NOIR"));
			
			if(rep.couleur == 0){
				//deplacerPion(out, rep.couleur, new Case(4,0), new Case(4,1));
				 premier = true;
				 //pion = rep.couleur;
			}
			int i = 8;
			while(fin){
				if(premier){
					System.out.println("[---]");
					//c'est à moi de jouer, je demande un coup à l'IA et l'envoi au serveur
					//queryStr = "demanderCoup(0,MOI,ADV,WALL).";
					//qu = sicstus.openQuery(queryStr,solutions);
					//deplacerPion(out, rep.couleur, new Case(4,0), new Case(4,1));
					//poserMur(out,couleur, origine, horientation)
					deplacerPion(out, rep.couleur, new Case(4,i), new Case(4,i-1));
					i--;
					coupEnemy = validiteCoup(in);
					if(coupEnemy.valid != 0){
						System.out.println("[MOI] Fin de la partie");
						fin = false;
						premier = false;
					}
					
				}
				else{
					premier = true;
				}
				
				if(premier){
					System.out.println("[---]");
					//on attend le coup de l'adversaire
					coupEnemy = validiteCoup(in);
					if(coupEnemy.valid != 0){
						System.out.println("[MOI] Fin de la partie");
						fin = false;
						break;
					}
					attendre(in);
					
				}
			}
			in.close();
			//out.close();
			socket.close();
		} catch (UnknownHostException e) {
			 System.out.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}/* catch (SPException e) {
			System.err.println("Exception SICStus Prolog : " + e);
		    e.printStackTrace();
		    System.exit(-2);
		} catch (NoSuchMethodException e) {
			System.err.println("Exception prolog\n" + e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/ catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * definitions de quelques fonctions
	 */
	
	
	//convertion d'un int en tableau de byte
	public static final byte[] intToByteArray(int value) {
		value = Integer.reverseBytes(value);
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
                };
    }
	
	//Convertir un tableua de byte en int
	public static int getInt(byte[] t ){
		ByteBuffer buffer = ByteBuffer.wrap(t);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.getInt();
	}
	
	//envoi d'une demande de partie
	public static void startGame(OutputStream out,int type, String name) throws IOException{
		byte[] req = new byte[34];
		byte[] t = intToByteArray(type);
		for(int i = 0;i <t.length;i++){
			req[i] = t[i];
		}
		byte[] nom = name.getBytes();
		for(int i = 0; i< nom.length;i++){
			req[4+i] = nom[i];
		}
		out.write(req);
		out.flush();

	}

	//recevoir la reponse d'une demande de partie
	public static PartiRep reponsePartie(InputStream in) throws IOException{
		int bytesRead = -1;
		byte[] partiRep = new byte[40];
		bytesRead = in.read(partiRep);
		byte[] repType = new byte[4];
		byte[] coulB = new byte[4];
		for(int i = 0; i < 4;i++)repType[i] = partiRep[i];
		for(int i = 0; i < 4;i++)coulB[i] = partiRep[4+i];	
		int codeErr = getInt(repType);
		int couleur = getInt(coulB);
		String nomA = new String(partiRep,8,partiRep.length-8);
			
		return new PartiRep(codeErr, couleur, nomA);
	}
	
	//envoyer un coup de type deplacement de pion au serveur
	public static void deplacerPion(OutputStream out, int couleur, Case depart, Case arrive) throws IOException{
		byte[] coup = new byte[28];
		
		byte[] id = intToByteArray(1);
		byte[] coleur = intToByteArray(couleur);
		byte[] typeCoup = intToByteArray(0);
		
		byte[] axeN = intToByteArray(depart.chiffre);
		byte[] axeA = intToByteArray(depart.lettre);
		byte[] axeNA = intToByteArray(arrive.chiffre);
		byte[] axeAA = intToByteArray(arrive.lettre);
		
		for(int i = 0; i< 4;i++)coup[i] = id[i];
		for(int i = 0; i< 4;i++)coup[4+i] = coleur[i];
		for(int i = 0; i< 4;i++)coup[8+i] = typeCoup[i];
		for(int i = 0; i< 4;i++)coup[12+i] = axeA[i];
		for(int i = 0; i< 4;i++)coup[16+i] = axeN[i];
		for(int i = 0; i< 4;i++)coup[20+i] = axeAA[i];
		for(int i = 0; i< 4;i++)coup[24+i] = axeNA[i];

		out.write(coup);
		out.flush();
		//System.out.println("Coup envoyé !");
	}
	
	//Evoyer un coup de type mur au serveur
	public static void poserMur(OutputStream out,int couleur, Case origine, int horientation) throws IOException{
        byte[] coup = new byte[24];
		
		byte[] id = intToByteArray(1);
		byte[] coleur = intToByteArray(couleur);
		byte[] typeCoup = intToByteArray(1);
		
		byte[] axeN = intToByteArray(origine.chiffre);
		byte[] axeA = intToByteArray(origine.lettre);
		byte[] h = intToByteArray(horientation);
		
		for(int i = 0; i< 4;i++)coup[i] = id[i];
		for(int i = 0; i< 4;i++)coup[4+i] = coleur[i];
		for(int i = 0; i< 4;i++)coup[8+i] = typeCoup[i];
		for(int i = 0; i< 4;i++)coup[12+i] = axeN[i];
		for(int i = 0; i< 4;i++)coup[16+i] = axeA[i];
		for(int i = 0; i< 4;i++)coup[20+i] = h[i];

		out.write(coup);
		out.flush();
		System.out.println("Coup envoyé !");
	}
	
	//recevoir la validation d'un coup
	public static CoupRep validiteCoup(InputStream in) throws IOException{
		byte[] validCoup = new byte[8];
		int bytesRead = in.read(validCoup);
		byte[] coupRep = new byte[4];
		byte[] coupValide = new byte[4];
		for(int i = 0; i < 4;i++)coupRep[i] = validCoup[i];
		for(int i = 0; i < 4;i++)coupValide[i] = validCoup[4+i];
		
		int errCoup = getInt(coupRep);
		int valCoup = getInt(coupValide);
		//System.out.println("Error du coup adv "+errCoup+" validité "+valCoup);
		switch(valCoup){
			case 0:
				System.out.println("COUP VALIDE ");
			break;
			case 1:
				System.out.println("TIME OUT ");
			break;
			case 2:
				System.out.println("COUP TRICHE ! ");
			break;
		}
		return new CoupRep(errCoup, valCoup);
	}
	
	
	/*public static Plateau getPlateau(HashMap<String, SPTerm> solutions) {
		Plateau plateau = null;;
		SPTerm[] me;
		SPTerm[] adv;
		SPTerm[] murs;
		SPTerm[] cases;
		SPTerm[] ca;
		ArrayList<Case> couples;
		ArrayList<ArrayList<Case>> listeMurs = new ArrayList<ArrayList<Case>>();
		Case c1;
		Case c2;
		try {
			me = solutions.get("MOI").toTermArray();
			adv = solutions.get("ADV").toTermArray();
			murs = solutions.get("WALL").toTermArray();
			
			c1 = new Case((int)me[0].getInteger(),(int)me[1].getInteger());
			c2 = new Case((int)adv[0].getInteger(),(int)adv[1].getInteger());
			plateau = new Plateau(c1,c2);
			for(SPTerm elt : murs){
				couples = new ArrayList<Case>();
				if(elt.isList() && !elt.isEmptyList()){
					cases = elt.toTermArray();
					for(SPTerm c : cases){
						ca = c.toTermArray();
						couples.add(new Case((int)ca[0].getInteger(),(int)ca[1].getInteger()));
						System.out.println(c.toTermArray()[0].getInteger());
					}
				}
				plateau.addWall(couples);
			}
		} catch (ConversionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return plateau;
	}*/
	
	public static Object attendre(InputStream in) throws IOException{
		byte[] coup = new byte[28];
		in.read(coup);
		byte[] id = new byte[4];
		byte[] couleur = new byte[4];
		byte[] type = new byte[4];
		byte[] axeA = new byte[4];
		byte[] axeN = new byte[4];
		byte[] axeAA = new byte[4];
		byte[] axeNA = new byte[4];
		
		for(int i = 0; i < 4;i++)id[i] = coup[i];
		for(int i = 0; i < 4;i++)couleur[i] = coup[4+i];
		for(int i = 0; i < 4;i++)type[i] = coup[8+i];
		for(int i = 0; i < 4;i++)axeA[i] = coup[12+i];
		for(int i = 0; i < 4;i++)axeN[i] = coup[16+i];
		int idRequest = getInt(id);
		int pion = getInt(couleur);
		int typeCoup = getInt(type);
		int aDepart = getInt(axeA);
		int oDepart = getInt(axeN);
		if(typeCoup == 0){
			for(int i = 0; i < 4;i++)axeAA[i] = coup[20+i];
			for(int i = 0; i < 4;i++)axeNA[i] = coup[24+i];
			System.out.println("Coup adverse : deplacement");
			int aArrive = getInt(axeAA);
			int oArrive = getInt(axeNA);
			return new CoupDep(idRequest, pion, typeCoup, new Case(aDepart,oDepart), new Case(aArrive,oArrive));
		}
		else{
			for(int i = 0; i < 4;i++)axeAA[i] = coup[20+i];
			System.out.println("Coup adverse : mur");
			int hor = getInt(axeAA);
			return new CoupMur(idRequest, pion, typeCoup, new Case(aDepart,oDepart),hor);
		}
		//return new Object();
	}
}
