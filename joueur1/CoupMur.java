//package joueur;

public class CoupMur {
	public int id;
	public int couleur;
	public int type;
	public Case cas;
	public int horientation;
	
	public CoupMur(int id, int couleur, int type, Case cas, int h){
		this.id = id;
		this.couleur = couleur;
		this.type = type;
		this.cas = cas;
		this.horientation = h;
	}
	
}

