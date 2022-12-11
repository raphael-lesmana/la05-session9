package model;

/*
 * HostileMob.java
 * An abstract class for anything that attacks.
 */

public abstract class HostileMob extends Mob implements Attacking {
	private int atk;
	public HostileMob(String name, int hp, int lvl, int xp, int atk) {
		super(name, hp, lvl, xp);
		this.atk = atk;
	}
	
	public HostileMob(String name, int hp, int lvl, int xp) {
		super(name, hp, lvl, xp);
	}
	
	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}
}
