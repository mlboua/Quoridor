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
	private static int COUP = 1;
	public static void main(String[] args) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out;
		Scanner sc = new Scanner(System.in);
		//byte[] req = new byte[34];
		byte[] coup = new byte[28];
		try {
			socket = new Socket("localhost", 2014);
			System.out.println("Connexion établie");
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			System.out.print("Nom du joueur : ");
			String nom = sc.nextLine();
						
			demandePartie(out,PARTIE, nom);
			PartiRep rep = startParti(in);
			

			
			System.out.println("Demande de parti Error code : "+rep.err+" et couleur : "+rep.couleur+" nom :"+rep.nom);
			if(rep.couleur == 0){
				byte[] id = intToByteArray(5);
				byte[] col = intToByteArray(Integer.reverseBytes(0));
				byte[] typeCoup = intToByteArray(0);
				byte[] axeN = intToByteArray(2);
				byte[] axeA = intToByteArray(0);
				for(int i = 0; i< id.length;i++)coup[i] = id[i];
				for(int i = 0; i< col.length;i++)coup[id.length+i] = col[i];
				for(int i = 0; i< typeCoup.length;i++)coup[8+i] = typeCoup[i];
				for(int i = 0; i< axeN.length;i++)coup[12+i] = axeN[i];
				for(int i = 0; i< axeA.length;i++)coup[16+i] = axeA[i];

				out.write(coup);
				out.flush();
				System.out.println("Coup envoyé !");
			}
			
			byte[] validCoup = new byte[8];
			int bytesRead = in.read(validCoup);
			byte[] coupRep = new byte[4];
			byte[] coupValide = new byte[4];
			for(int i = 0; i < 4;i++)coupRep[i] = validCoup[i];
			for(int i = 0; i < 4;i++)coupValide[i] = validCoup[4+i];
			
			int errCoup = getInt(coupRep);
			int valCoup = getInt(coupValide);
			System.out.println("Error du coup adv "+errCoup+" validité "+valCoup);
			
			socket.close();
		} catch (UnknownHostException e) {
			 System.out.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
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
	public static void demandePartie(OutputStream out,int type, String name){
		byte[] req = new byte[34];
		byte[] t = intToByteArray(type);
		for(int i = 0;i <t.length;i++){
			req[i] = t[i];
		}
		byte[] nom = name.getBytes();
		for(int i = 0; i< nom.length;i++){
			req[4+i] = nom[i];
		}
		try {
			out.write(req);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PartiRep startParti(InputStream in) throws IOException{
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
}