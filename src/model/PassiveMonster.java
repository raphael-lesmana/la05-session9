package model;

/*
 * PassiveMonster.java
 * Monsters that do not attack. Has an elemental weakness.
 */

public class PassiveMonster extends Mob {
	private Elements element;
	
	public PassiveMonster(String name, int hp, int lvl, int xp, Elements element) {
		super(name, hp, lvl, xp);
		this.element = element;
		this.setMhp(770 + 25 * lvl);
	}
	
	public PassiveMonster(String name, int lvl, Elements element) {
		super(name, 770 + 25 * lvl, lvl, 0);
		this.element = element;
		this.setMhp(770 + 25 * lvl);
	}

	public Elements getElement() {
		return element;
	}

	public void setElement(Elements element) {
		this.element = element;
	}	
}
