package joueur;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class Joueur {
	private static int PARTIE = 0;
	static byte[] coup = new byte[28];
	public static void main(String[] args) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out;
		Scanner sc = new Scanner(System.in);
		try {
			socket = new Socket("localhost", 2014);
			System.out.println("Connexion établie");
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			System.out.print("Nom du joueur : ");
			String nom = sc.nextLine();
						
			demandePartie(out,PARTIE, nom);
			PartiRep rep = reponsePartie(in);
			

			
			System.out.println("Demande de parti Error code : "+rep.err+" et couleur : "+rep.couleur+" nom :"+rep.nom);
			if(rep.couleur == 0){//je commence 
				deplacerPion(out, rep.couleur, new Case(4,0), new Case(4,1));
			}
			CoupRep cp = validiteCoup(in);
			in.read(coup);
			byte[] id = new byte[4];
			byte[] couleur = new byte[4];
			byte[] type = new byte[4];
			byte[] axeN = new byte[4];
			byte[] axeA = new byte[4];
			byte[] axeNA = new byte[4];
			byte[] axeAA = new byte[4];
			
			for(int i = 0; i < 4;i++)id[i] = coup[i];
			for(int i = 0; i < 4;i++)couleur[i] = coup[4+i];
			for(int i = 0; i < 4;i++)type[i] = coup[8+i];
			for(int i = 0; i < 4;i++)axeN[i] = coup[12+i];
			for(int i = 0; i < 4;i++)axeA[i] = coup[16+i];
			int idRequest = getInt(id);
			int pion = getInt(couleur);
			int typeCoup = getInt(type);
			int aDepart = getInt(axeN);
			int oDepart = getInt(axeA);
			if(typeCoup == 0){
				for(int i = 0; i < 4;i++)axeNA[i] = coup[20+i];
				for(int i = 0; i < 4;i++)axeAA[i] = coup[24+i];
				System.out.println("Coup adverse : deplacement");
			}
			else{
				for(int i = 0; i < 4;i++)axeNA[i] = coup[20+i];
				System.out.println("Coup adverse : mur");
			}
			deplacerPion(out, rep.couleur, new Case(4,1), new Case(4,2));
			
			socket.close();
		} catch (UnknownHostException e) {
			 System.out.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
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
	
	public static int getInt(byte[] t ){
		ByteBuffer buffer = ByteBuffer.wrap(t);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.getInt();
	}
	
	//envoi d'une demande de partie
	public static void demandePartie(OutputStream out,int type, String name) throws IOException{
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

	//rec
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
	
	public static void deplacerPion(OutputStream out, int couleur, Case depart, Case arrive) throws IOException{
		byte[] coup = new byte[28];
		
		byte[] id = intToByteArray(1);
		byte[] coleur = intToByteArray(couleur);
		byte[] typeCoup = intToByteArray(0);
		
		byte[] axeN = intToByteArray(depart.axeNum);
		byte[] axeA = intToByteArray(depart.axeAlpha);
		byte[] axeNA = intToByteArray(arrive.axeNum);
		byte[] axeAA = intToByteArray(arrive.axeAlpha);
		
		for(int i = 0; i< 4;i++)coup[i] = id[i];
		for(int i = 0; i< 4;i++)coup[4+i] = coleur[i];
		for(int i = 0; i< 4;i++)coup[8+i] = typeCoup[i];
		for(int i = 0; i< 4;i++)coup[12+i] = axeN[i];
		for(int i = 0; i< 4;i++)coup[16+i] = axeA[i];
		for(int i = 0; i< 4;i++)coup[20+i] = axeNA[i];
		for(int i = 0; i< 4;i++)coup[24+i] = axeAA[i];

		out.write(coup);
		out.flush();
		System.out.println("Coup envoyé !");
	}
	public static void poserMur(OutputStream out,int couleur, Case origine, int horientation) throws IOException{
        byte[] coup = new byte[28];
		
		byte[] id = intToByteArray(1);
		byte[] coleur = intToByteArray(couleur);
		byte[] typeCoup = intToByteArray(0);
		
		byte[] axeN = intToByteArray(origine.axeNum);
		byte[] axeA = intToByteArray(origine.axeAlpha);
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
	public static CoupRep validiteCoup(InputStream in) throws IOException{
		byte[] validCoup = new byte[8];
		int bytesRead = in.read(validCoup);
		byte[] coupRep = new byte[4];
		byte[] coupValide = new byte[4];
		for(int i = 0; i < 4;i++)coupRep[i] = validCoup[i];
		for(int i = 0; i < 4;i++)coupValide[i] = validCoup[4+i];
		
		int errCoup = getInt(coupRep);
		int valCoup = getInt(coupValide);
		System.out.println("Error du coup adv "+errCoup+" validité "+valCoup);
		return new CoupRep(errCoup, valCoup);
	}
	
	/*public static Coup recevoirCoup(InputStream in){
		
	}*/
}