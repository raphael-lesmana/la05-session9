package model;

/*
 * HostileMonster.java
 * A class for monsters that attack. Every monster has an
 * elemental weakness.
 */

public class HostileMonster extends HostileMob {
	private Elements element;
	
	public HostileMonster(String name, int hp, int lvl, int xp, int atk, Elements element) {
		super(name, hp, lvl, xp, atk);
		this.element = element;
		this.setMhp(800 + 15 * lvl);
	}
	
	public HostileMonster(String name, int lvl, Elements element) {
		super(name, 800 + 15 * lvl, lvl, 0, 25 + lvl * 3);
		this.element = element;
		this.setMhp(800 + 15 * lvl);
	}

	public Elements getElement() {
		return element;
	}

	public void setElement(Elements element) {
		this.element = element;
	}
	
	@Override
	public int attack(Mob target) {
		// TODO Auto-generated method stub
		return 0;
	}

}
