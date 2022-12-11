package model;

/*
 * Person.java
 * A class for person-type enemies, including the player.
 * Can be recruited.
 */

public abstract class Person extends HostileMob {
	private Weapon equipment = null;
	
	public Person(String name, int hp, int lvl, int xp, int atk) {
		super(name, hp, lvl, xp, atk);
		this.setMhp(800 + 8 * lvl);
	}
	
	public Person(String name, int lvl, int xp, int atk) {
		super(name, 800 + 8 * lvl, lvl, xp, atk);
		this.setMhp(800 + 8 * lvl);
	}
	
	public Person(String name, int lvl) {
		super(name, 800 + 8 * lvl, lvl, 0);
		this.setMhp(800 + 8 * lvl);
	}

	public Weapon getEquipment() {
		return equipment;
	}

	public void setEquipment(Weapon equipment) {
		this.equipment = equipment;
	}
}
