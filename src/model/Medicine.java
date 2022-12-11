package model;

/*
 * Medicine.java
 * Items that heal a party member.
 */

public class Medicine extends Item implements Curing {
	private int lvl;
	
	public Medicine(String name, int lvl) {
		super(name);
		this.lvl = lvl;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	@Override
	public void cure(Mob target) {
		int newHp = target.getHp();
		newHp += lvl * 3 - (2 * lvl/10);
		newHp = (newHp > target.getMhp()) ? target.getMhp() : newHp;
		target.setHp(newHp);
	}
}
