package model;

import java.util.concurrent.ThreadLocalRandom;

/*
 * Warlock.java
 * Warlocks are magic-focused attackers. They deal more damages against monster-type
 * enemies. Each warlock has a base element which cannot be changed and another
 * weapon-based element.
 */

public class Warlock extends Person {
	private Elements element;
	
	public Warlock(String name, int lvl, Elements element) {
		super(name, lvl);
		this.element = element;
		this.setAtk(40 + lvl * 4);
	}

	@Override
	public int attack(Mob target) {
		int oldHp = target.getHp();
		int newHp = oldHp;
		if (target instanceof Person) {
			if (this.getEquipment() == null) {
				newHp -= 5 + (this.getAtk() / 5) * ThreadLocalRandom.current().nextInt(this.getLvl() + 3, this.getLvl() + 5);
			}
			else {
				newHp -= ((this.getAtk() + this.getEquipment().getAtk()) / 5) * ThreadLocalRandom.current().nextInt(this.getLvl() + 3, this.getLvl() + 5);
			}
		}
		else if (target instanceof HostileMonster) {
			HostileMonster monsterTarget = (HostileMonster) target;
			newHp -= ((this.getAtk() + this.getEquipment().getAtk()) / 3) * ThreadLocalRandom.current().nextInt(this.getLvl() + 5, this.getLvl() + 12);
			if (monsterTarget.getElement() == this.element || monsterTarget.getElement() == this.getEquipment().getElement()) {
				newHp -= (this.getAtk() / 4) * this.getLvl();
			}
		}
		else if (target instanceof PassiveMonster) {
			PassiveMonster monsterTarget = (PassiveMonster) target;
			newHp -= ((this.getAtk() + this.getEquipment().getAtk()) / 2) * ThreadLocalRandom.current().nextInt(this.getLvl() + 8, this.getLvl() + 12);
			if (monsterTarget.getElement() == this.element || monsterTarget.getElement() == this.getEquipment().getElement()) {
				newHp -= (this.getAtk() / 4) * this.getLvl();
			}
		}
		newHp = (newHp < 0) ? 0 : newHp;
		if (ThreadLocalRandom.current().nextInt(0, 100) < 75) {
			target.setHp(newHp);
			return oldHp - newHp;
		}
		else {
			return 0;
		}
	}

	public Elements getElement() {
		return element;
	}

	public void setElement(Elements element) {
		this.element = element;
	}
	
}
