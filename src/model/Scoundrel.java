package model;

import java.util.concurrent.ThreadLocalRandom;

/*
 * Scoundrel.java
 * Scoundrels are primarily melee-based attackers.
 * Scoundrels are derived from the People class, which means that
 * they can be recruited from the enemy party.
 * 
 * Game balancing mechanics may be wrong.
 */

public class Scoundrel extends Person {	
	public Scoundrel(String name, int lvl) {
		super(name, lvl);
		this.setAtk(50 + lvl * 6);
	}

	@Override
	public int attack(Mob target) {
		int oldHp = target.getHp();
		int newHp = oldHp;
		if (this.getEquipment() == null) {
			newHp -= (this.getAtk() / 4) * ThreadLocalRandom.current().nextInt(this.getLvl() + 5, this.getLvl() + 10);
		}
		else {
			newHp -= ((this.getAtk() + this.getEquipment().getAtk()) / 4) * ThreadLocalRandom.current().nextInt(this.getLvl() + 5, this.getLvl() + 10);
		}
		newHp = (newHp < 0) ? 0 : newHp;
		
		if (ThreadLocalRandom.current().nextInt(0, 100) < 95) {
			target.setHp(newHp);
			return oldHp - newHp;
		}
		else {
			return 0;
		}
	}

}
