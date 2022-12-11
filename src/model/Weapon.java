package model;

/*
 * Weapons.java
 * Items usable as weapons.
 */

public class Weapon extends Item {
	private int atk;
	private Elements element;
	
	public Weapon(String name, int atk, Elements element) {
		super(name);
		this.atk = atk;
		this.element = element;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public Elements getElement() {
		return element;
	}

	public void setElement(Elements element) {
		this.element = element;
	}
}
