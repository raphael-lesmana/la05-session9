package model;

/*
 * EnemyParty.java
 * A class that contains an enemy party, along with a constructor to create it.
 * Scoundrels, Warlocks and Monsters have each a 1/3 chance of spawning. Monsters are
 * further subdivided to equal-chance hostile and passive monsters. 
 */

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public final class EnemyParty {
	private ArrayList<Mob> members;
	
	public EnemyParty(ArrayList<Mob> members) {
		this.members = members;
	}
	
	public EnemyParty(int playerPartyCount, int playerPartyLvl) {
		this.members = new ArrayList<Mob>();
		int count = ThreadLocalRandom.current().nextInt(1, playerPartyCount + 2);
		int enemyType;
		int maxLvl = 5 * (playerPartyLvl / 4) + 2;
		maxLvl = (maxLvl <= 0) ? 1 : maxLvl;
		int scoundrelCount = 0;
		int warlockCount = 0;
		int monsterCount = 0;
		
		for (int i = 0; i < count; i++) {
			enemyType = ThreadLocalRandom.current().nextInt(0, 12);
			if (enemyType >= 0 && enemyType <= 3) {
				this.addMember(new Scoundrel("Scoundrel " + (++scoundrelCount), ThreadLocalRandom.current().nextInt(1, maxLvl + 1)));
			}
			else if (enemyType >= 4 && enemyType <= 7) {
				Elements element = Elements.values()[ThreadLocalRandom.current().nextInt(1, 5)];
				this.addMember(new Warlock("Warlock " + (++warlockCount), ThreadLocalRandom.current().nextInt(1, maxLvl + 1), element));
			}
			else {
				enemyType = ThreadLocalRandom.current().nextInt(0, 10);
				Elements element = Elements.values()[ThreadLocalRandom.current().nextInt(1, 5)];
				if (enemyType < 8) {
					this.addMember(new HostileMonster("Monster " + (++monsterCount), ThreadLocalRandom.current().nextInt(1, maxLvl + 1), element));
				}
				else {
					this.addMember(new PassiveMonster("Monster " + (++monsterCount), ThreadLocalRandom.current().nextInt(1, maxLvl + 1), element));
				}
			}
		}
	}

	public ArrayList<Mob> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<Mob> members) {
		this.members = members;
	}
	
	public void addMember(Mob member) {
		this.members.add(member);
	}
	
	public int getSize() {
		return this.members.size();
	}
	
	public int getAverageLvl() {
		int total = 0;
		for (Mob member : members) {
			total += member.getLvl();
		}
		return total / members.size();
	}
}
