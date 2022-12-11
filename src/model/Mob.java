package model;

/*
 * Mob.java
 * Base abstract class for all enemies, hostile or not.
 * XP only matters for members of the player's party.
 */

public abstract class Mob {
	private String name;
	private int hp;
	private int mhp;
	private int lvl;
	private int xp;
	
	public Mob(String name, int hp, int lvl, int xp) {
		this.name = name;
		this.hp = hp;
		this.lvl = lvl;
		this.xp = xp;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getMhp() {
		return mhp;
	}

	public void setMhp(int mhp) {
		this.mhp = mhp;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
