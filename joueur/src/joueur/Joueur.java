package joueur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
		//BufferedReader in = null;
		InputStream in = null;
		//DataInputStream in = null;
		OutputStream out;
		Scanner sc = new Scanner(System.in);
		byte[] req = new byte[100];
		byte[] coup = new byte[100];
		try {
			socket = new Socket("localhost", 2014);
			
			System.out.println("Connexion établie");
			
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			
			byte[] type = intToByteArray(PARTIE);
			for(int i = 0;i <type.length;i++){
				req[i] = type[i];
			}
			
			System.out.print("Nom du joueur : ");
			String nom = sc.nextLine();
						
			byte[] nomB = nom.getBytes();
			for(int i = 0; i< nomB.length;i++){
				req[4+i] = nomB[i];
			}
			out.write(req);
			out.flush();
			
			//ByteBuffer bb = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			//byte[] content = new byte[ 2048 ];
			int bytesRead = -1;
			System.out.println("Taille data : "+in.available());
			byte[] content = new byte[80];
			//while( ( bytesRead = in.read( content ) ) != -1 ) {  
				bytesRead = in.read(content);
			    //baos.write( content, 0, bytesRead );  
			//}
			 
			//ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
			byte[] repType = new byte[4];
			byte[] coulB = new byte[4];
			//byte[] nomA = new byte[50];
			for(int i = 0; i < 4;i++)repType[i] = content[i];
			for(int i = 0; i < 4;i++)coulB[i] = content[4+i];
			//for(int i = 0; i < 4;i++)nomA[i] = content[8+i];
			
			int codeErr = getInt(repType);
			int couleur = getInt(coulB);
		
			System.out.println("Demande de parti Error code : "+codeErr+" et couleur : "+couleur);
			//out.write(1);
			/*if(couleur == 0){*/
				byte[] id = intToByteArray(1);
				byte[] col = intToByteArray(0);
				byte[] typeCoup = intToByteArray(0);
				byte[] axeN = intToByteArray(0);
				byte[] axeA = intToByteArray(0);
				for(int i = 0; i< id.length;i++)coup[i] = id[i];
				for(int i = 0; i< col.length;i++)coup[id.length+i] = col[i];
				for(int i = 0; i< typeCoup.length;i++)coup[8+i] = typeCoup[i];
				for(int i = 0; i< axeN.length;i++)coup[12+i] = axeN[i];
				for(int i = 0; i< axeA.length;i++)coup[16+i] = axeA[i];
				out = socket.getOutputStream();
				out.write(coup);
				//out.write(1);
				out.flush();
				System.out.println("Coup envoyé !");
			//}
			//content = new byte[in.available()];
			/*content = new byte[80];
			bytesRead = in.read(content);
			byte[] coupRep = new byte[4];
			byte[] coupValide = new byte[4];
			for(int i = 0; i < 4;i++)coupRep[i] = content[i];
			for(int i = 0; i < 4;i++)coupValide[i] = content[4+i];
			
			int errCoup = getInt(coupRep);
			int valCoup = getInt(coupValide);
			
			System.out.println("Error du coup "+errCoup+" validité "+valCoup);*/
			
			socket.close();
		} catch (UnknownHostException e) {
			 System.out.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	//convertion d'un int en tableau de byte
	public static final byte[] intToByteArray(int value) {
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

}