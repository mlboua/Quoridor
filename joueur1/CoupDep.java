//package joueur;

public class CoupDep {
	public int id;
	public int couleur;
	public int type;
	public Case caseDepart;
	public Case caseArrivee;
	
	public CoupDep(int id, int couleur, int type, Case casD, Case casA){
		this.id = id;
		this.couleur = couleur;
		this.type = type;
		this.caseDepart = casD;
		this.caseArrivee = casA;
	}
	
}

