//package joueur;

import java.util.ArrayList;

public class Plateau {
	private Case me;
	private Case enemy;
	ArrayList<ArrayList<Case>> walls;
	
	public Plateau(int pion){
		super();
	}
	public Plateau(Case c1, Case c2 ){
		me = c1;
		enemy = c2;
		walls = new ArrayList<ArrayList<Case>>();
	}
	
	public void initialize(int pion){
		if(pion == 0){
			this.me = new Case(5,9);
			this.enemy = new Case(5,1);
			this.walls = new ArrayList<ArrayList<Case>>();
		}
		else{
			this.me = new Case(5,1);
			this.enemy = new Case(5,9);
			this.walls = new ArrayList<ArrayList<Case>>();
		}
	}
	public void setMe(Case m){
		this.me = m;
	}
	public void setEnemy(Case e){
		this.enemy = e;
	}
	public void addWall(ArrayList<Case> wall){
		this.walls.add(wall);
	}
	
	public String toString() {
		String txt = "["+me.lettre+","+me.chiffre+"]"+"["+enemy.lettre+","+enemy.chiffre+"]";
		for(ArrayList<Case> c : walls){
			txt = "("+c.get(0).lettre+","+c.get(0).chiffre+") & "+"("+c.get(1).lettre+","+c.get(1).chiffre+")";
		}
		return txt;
	}
}

