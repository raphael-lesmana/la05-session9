package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import model.Elements;
import model.EnemyParty;
import model.HostileMob;
import model.Item;
import model.Medicine;
import model.Mob;
import model.Person;
import model.PlayerParty;
import model.Scoundrel;
import model.Warlock;
import model.Weapon;

/*
 * Main.java
 * Main driving class for the entire game
 */

public class Main {
	private Scanner scanf = new Scanner(System.in);
	
	public Main() {
		setup();
		mainMenu();
	}

	public static void main(String[] args) {
		new Main();
	}
	
	/*
	 * setup()
	 * Set up the player's character, and initialize party
	 */
	public void setup() {
		Console.cls();
		String name = "";
		String type = "";
		
		System.out.print("Your name? ");
		name = scanf.nextLine();
		
		// setup player's name and type
		do {
			System.out.print("Your type [Scoundrel|Warlock]? ");
			type = scanf.nextLine();
		} while (!type.equalsIgnoreCase("scoundrel") && !type.equalsIgnoreCase("warlock"));
		
		if (type.equalsIgnoreCase("scoundrel")) {
			PlayerParty.addMember(new Scoundrel(name, 1));
			PlayerParty.getMembers().get(0).setEquipment(new Weapon("kortaszis", 20, Elements.NONE));
		}
		else {
			String elementString;
			Elements element;
			do {
				System.out.print("Your element? [Fire|Water|Earth|Wind]? ");
				elementString = scanf.nextLine();
			} while(!elementString.equalsIgnoreCase("fire") && !elementString.equalsIgnoreCase("water")
					&& !elementString.equalsIgnoreCase("earth") && !elementString.equalsIgnoreCase("wind"));
			element = Elements.valueOf(elementString.toUpperCase());
			PlayerParty.addMember(new Warlock(name, 1, element));
			PlayerParty.getMembers().get(0).setEquipment(new Weapon("kasiechida", 20, Elements.FIRE));
		}
		
		// set up inventory
		for (int i = 0; i < 3; i++)
			PlayerParty.getInventory().add(new Medicine("rami-čañelay", 20));
		PlayerParty.getInventory().add(new Medicine("hačaytamban", 40));
	}
	
	/*
	 * mainMenu()
	 * Main driver of the game
	 * 
	 * 1. Battles happen every 4-15 steps.
	 * 2. When the user enters battle, they can either engage, recruit the enemy, or escape.
	 * 3. When the entire party dies, the game ends.
	 * 4. Players can also buy items in shops.
	 * Note that game balancing may be really off. These formulas are experimental.
	 */
	
	public void mainMenu() {
		int opt = 0;
		int nextEvent = ThreadLocalRandom.current().nextInt(4, 15);
		do {
			opt = 0;
			Console.cls();
			System.out.println("1. Walk 1 mile");
			System.out.println("2. Walk 5 miles");
			System.out.println("3. Walk 10 miles");
			System.out.println("4. Item shop");
			System.out.println("5. Weapon shop");
			System.out.println("6. Party info");
			System.out.println("7. Quit");
			do {
				System.out.print(">> ");
				try {
					opt = scanf.nextInt();
					scanf.nextLine();
				}
				catch (Exception e) {
					opt = 0;
				}
			} while (opt < 1 || opt > 7);
			switch (opt) {
			case 1:
				System.out.println("Walking... 1");
				nextEvent--;
				if (nextEvent == 0) {
					if (!battle())
						return;
					nextEvent = ThreadLocalRandom.current().nextInt(4, 15);
				}
				break;
			case 2:
				for (int i = 0; i < 5; i++, nextEvent--) {
					System.out.println("Walking... " + (i+1));
					if (nextEvent == 0) {
						if (!battle())
							return;
						nextEvent = ThreadLocalRandom.current().nextInt(4, 15);
					}
				}
				break;
			case 3:
				for (int i = 0; i < 10; i++, nextEvent--) {
					System.out.println("Walking... " + (i+1));
					if (nextEvent == 0) {
						if (!battle())
							return;
						nextEvent = ThreadLocalRandom.current().nextInt(4, 15);
					}
				}
				break;
			case 4:
				itemShop();
				break;
			case 5:
				weaponShop();
				break;
			case 6:
				partyInfo();
			}
		} while (opt != 7);
	}
	
	public void partyInfo() {
		Console.cls();
		ArrayList<Person> members = PlayerParty.getMembers();
		for (Person member : members) {
			System.out.println("Name: " + member);
			if (member instanceof Scoundrel) {
				System.out.println("Type: Scoundrel");
			}
			else {
				System.out.println("Type: Warlock");
				System.out.println("Element: " + ((Warlock) member).getElement().toString());
			}
			System.out.println("HP: " + member.getHp() + "/" + member.getMhp());
			System.out.println("Level: " + member.getLvl());
			System.out.println("EXP: " + member.getXp());
			System.out.println("Attack: " + member.getAtk());
			if (member.getEquipment() == null) {
				System.out.println("Equipment: None" );
			}
			else {
				System.out.println("Equipment: " + member.getEquipment());
			}
			System.out.println("================================================");
		}
		Console.pause(scanf);
	}
	
	/*
	 * battle()
	 * Driver function for battles
	 * 
	 * 1. The player's party attacks first
	 * 2. Each party member can do one of the following actions
	 * 	a. Attack
	 *     Attack the enemy. Scoundrels do not have elemental attacks, and deal less damage to monster-type enemies.
	 *     Warlocks meanwhile have elemental attacks and deal more damage to monster-type enemies.
	 *  b. Use an item
	 *     Consume an item.
	 *  c. Recruit
	 *     Persuade an enemy to join your party. Only works on person-type enemies. Will fail if the enemy has a
	 *     higher level than the party member.
	 *  d. Escape
	 *     Attempt to escape the battle. When a party member uses this, every subsequent party member's turn is cancelled,
	 *     if the escape is unsuccessful.
	 */
	public boolean battle() {
		System.out.println("You are attacked!");
		Console.pause(scanf);
		
		EnemyParty enemyParty = new EnemyParty(PlayerParty.getSize(), PlayerParty.getAverageLvl());
		ArrayList<Mob> enemyPartyMembers = enemyParty.getMembers();
		ArrayList<Person> playerPartyMembers = PlayerParty.getMembers();
		int opt = 0;
		int i = 1;
		int dmg = 0;
		int enemyDeathCount = 0;
		int partyDeathCount = 0;
		int playerPartySize = PlayerParty.getSize();
		int chance;
		boolean skip = false;
		
		// do the turns until the minimal condition of an entire party being eliminated is achieved.
		while (enemyDeathCount < enemyParty.getSize() && partyDeathCount < PlayerParty.getSize()) {
			Console.cls();
			skip = false;
			i = 1;
			partyDeathCount = 0;
			playerPartySize = PlayerParty.getSize();
			System.out.println("Party members:");
			for (Mob member : playerPartyMembers) {
				System.out.print((i++) + ": ");
				System.out.print(member + " [HP: " + member.getHp() + "/" + member.getMhp() + "]\n");
				if (member.getHp() == 0) {
					partyDeathCount++;
				}
			}
			
			i = 1;
			System.out.println("Enemies: ");
			for (Mob member : enemyPartyMembers) {
				System.out.print((i++) + ": ");
				System.out.print(member + " [HP: " + member.getHp() + "/" + member.getMhp() + "]\n");
			}
			
			if (partyDeathCount == playerPartySize) {
				System.out.println("Your party has fallen...");
				Console.pause(scanf);
				return false;
			}
			
			System.out.println();
			
			for (int j = 0; j < playerPartySize; j++) {
				Person member = playerPartyMembers.get(j);
				if (member.getHp() == 0) {
					continue;
				}
				
				System.out.println("What will " + member + " do?");
				System.out.println("1. Attack");
				System.out.println("2. Use item");
				System.out.println("3. Recruit");
				System.out.println("4. Flee");
				do {
					System.out.print(">> ");
					try {
						opt = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						opt = 0;
					}
				} while (opt < 1 || opt > 4);
				
				switch (opt) {
				case 1:
					do {
						System.out.printf("Who will " + member + " attack? [1-" + enemyParty.getSize() + "] ");
						try {
							opt = scanf.nextInt();
							scanf.nextLine();
						}
						catch (Exception e) {
							opt = 0;
						}
					} while (opt < 1 || opt > enemyParty.getSize());
					
					System.out.println(member + " attacks " + enemyPartyMembers.get(opt - 1));
					dmg = member.attack(enemyPartyMembers.get(opt - 1));
					
					if (dmg == 0) {
						System.out.println("It missed...");
					}
					else {
						System.out.println("It dealt " + dmg + " points of damage!");
					}
					break;
				case 2:
					int itemOpt;
					int partyOpt;
					ArrayList<Item> inventory = PlayerParty.getInventory();
					
					if (inventory.size() == 0) {
						System.out.println("You have no items!");
						Console.pause(scanf);
						break;
					}
					
					i = 1;
					for (Item item : inventory) {
						System.out.println((i++) + ": "+ item);
					}
					
					do {
						System.out.print("What do you want to use? [1-" + inventory.size() + "] ");
						try {
							itemOpt = scanf.nextInt();
							scanf.nextLine();
						}
						catch (Exception e) {
							itemOpt = 0;
						}
					} while(itemOpt < 1 || itemOpt > inventory.size());
					
					do {
						System.out.print("Who do you want to use it on? [1-" + playerPartySize +"] ");
						try {
							partyOpt = scanf.nextInt();
							scanf.nextLine();
						}
						catch (Exception e) {
							partyOpt = 0;
						}
					} while(partyOpt < 1 || partyOpt > playerPartySize);
					
					((Medicine) PlayerParty.getInventory().get(itemOpt - 1)).cure(PlayerParty.getMembers().get(partyOpt - 1));
					break;
				case 3:
					do {
						System.out.printf("Who will " + member + " recruit? [1-" + enemyParty.getSize() + "] ");
						try {
							opt = scanf.nextInt();
							scanf.nextLine();
						}
						catch (Exception e) {
							opt = 0;
						}
					} while (opt < 1 || opt > enemyParty.getSize());
					opt--;
					
					if (enemyPartyMembers.get(opt).getHp() == 0) {
						System.out.println("They are already dead!");
						break;
					}
					
					if (!(enemyPartyMembers.get(opt) instanceof Person)) {
						System.out.println("You cannot recruit monsters!");
						break;
					}
					
					if (enemyPartyMembers.get(opt).getLvl() > member.getLvl()) {
						System.out.println("You cannot recruit this person!");
						break;
					}
					
					chance = ThreadLocalRandom.current().nextInt(0, 20);
					if (chance < 19) {
						System.out.println("You recruited " + enemyPartyMembers.get(opt) + " to your own party!");
						System.out.print("Enter a new name for " + enemyPartyMembers.get(opt) + ": ");
						String name = scanf.nextLine();
						enemyPartyMembers.get(opt).setName(name);
						PlayerParty.addMember((Person) enemyPartyMembers.remove(opt));
						System.out.println(name + " has joined the party!");
					}
					else {
						System.out.println(enemyPartyMembers.get(opt) + " laughs at your attempt!");
					}
					
					break;
				case 4:
					skip = true;
					chance = ThreadLocalRandom.current().nextInt(0, 10);
					if (chance < 5) {
						System.out.println("You escaped batttle...");
						Console.pause(scanf);
						return true;
					}
					else {
						System.out.println("You got caught escaping!");
					}
					break;
				}
				
				if (skip) {
					break;
				}
			}
			
			System.out.println();
			System.out.println("It's the enemy's turn!");

			enemyDeathCount = 0;
			for (Mob member : enemyPartyMembers) {
				if (member.getHp() == 0) {
					enemyDeathCount++;
					continue;
				}
				
				if (!(member instanceof HostileMob)) {
					System.out.println(member + " waits patiently...");
				} 
				else {
					HostileMob attacker = (HostileMob) member;
					opt = ThreadLocalRandom.current().nextInt(0, PlayerParty.getSize());
					System.out.println(member + " attacks " + playerPartyMembers.get(opt) + "!");
					dmg = attacker.attack(playerPartyMembers.get(opt));
					if (dmg == 0) {
						System.out.println("It missed...");
					}
					else {
						System.out.println("It dealt " + dmg + " points of damage!");
					}
				}
			}
			
			if (enemyDeathCount == enemyParty.getSize()) {
				int xpGain = 25 * enemyParty.getSize() + 5 * enemyParty.getAverageLvl();
				int xpOffset;
				int threshold;
				System.out.println("The enemy has been defeated!");
				for (Person member : playerPartyMembers) {
					if (member.getHp() > 0) {
						threshold = 100 + member.getLvl() * member.getLvl() + 2 * member.getLvl();
						if (member.getXp() + xpGain < threshold) {
							member.setXp(member.getXp() + xpGain);
						}
						else {
							xpOffset = member.getXp() + xpGain;
							while (member.getXp() + xpGain > threshold) {
								xpOffset = member.getXp() + xpGain - threshold;
								member.setXp(threshold);
								member.setLvl(member.getLvl() + 1);
								threshold = 100 + member.getLvl() * member.getLvl() + 2 * member.getLvl();
							}
							member.setXp(member.getXp() + xpOffset);
						}
					}
				}
				System.out.println("Living party members get " + xpGain + " EXP points.");
				PlayerParty.setMoney(PlayerParty.getMoney() + 10 * enemyParty.getSize());
				System.out.println("Your party got $" + 10 * enemyParty.getSize());
				Console.pause(scanf);
				return true;
			}
			Console.pause(scanf);
		}
		return false;
	}
	
	/* 
	 * itemShop()
	 * Driver for the item shop.
	 */
	public void itemShop() {
		Console.cls();
		int opt = 0;
		int c;
		System.out.println("Looking for something?");
		System.out.println("Kirim anu?");
		
		System.out.println("1. Buy rami-čañelay [20p, $10]");
		System.out.println("2. Buy hačaytamban [40p, $18]");
		System.out.println("3. Buy mačamanuk [65p, $25]");
		System.out.println("4. Leave");
		do {
			do {
				System.out.print(">> ");
				try {
					opt = scanf.nextInt();
					scanf.nextLine();
				}
				catch (Exception e) {
					opt = 0;
				}
			} while (opt < 1 || opt > 4);
			switch (opt) {
			case 1:
				System.out.print("How many do you want to buy? ");
				do {
					System.out.print(">> ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < 1);
				
				if (PlayerParty.getMoney() < c * 10) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - c * 10);
				
				for (int i = 0; i < c; i++) {
					PlayerParty.getInventory().add(new Medicine("hačaytamban", 40));
				}
				break;
			case 2:
				System.out.print("How many do you want to buy? ");
				do {
					System.out.print(">> ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (opt < 1);
				
				if (PlayerParty.getMoney() < c * 18) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - c * 18);
				
				for (int i = 0; i < c; i++) {
					PlayerParty.getInventory().add(new Medicine("hačaytamban", 40));
				}
				break;
			case 3:
				System.out.print("How many do you want to buy? ");
				do {
					System.out.print(">> ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (opt < 1);
				
				if (PlayerParty.getMoney() < c * 25) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - c * 25);
				
				for (int i = 0; i < c; i++) {
					PlayerParty.getInventory().add(new Medicine("mačamanuk", 65));
				}
				break;
			case 4:
				System.out.println("Thank you for shopping with us");
				System.out.println("Hutañ kami di kau ni bali kau");
				Console.pause(scanf);
				break;
			}
		} while (opt != 4);
	}
	
	public void weaponShop() {
		Console.cls();
		int opt = 0;
		int c;
		System.out.println("Looking for something?");
		System.out.println("Tohoda neo?");
		
		System.out.println("1. Buy kortaszis [20p, $40]");
		System.out.println("2. Buy kasiechida [20p, FIRE, $30]");
		System.out.println("3. Buy zmosrobo [40p, $60]");
		System.out.println("4. Buy michutara [40p, WATER, $60]");
		System.out.println("5. Buy diejdomas [80p, $125]");
		System.out.println("6. Buy horogihara [80p, EARTH, $125]");
		System.out.println("7. Exit");
		do {
			do {
				System.out.print(">> ");
				try {
					opt = scanf.nextInt();
					scanf.nextLine();
				}
				catch (Exception e) {
					opt = 0;
				}
			} while (opt < 1 || opt > 7);
			
			switch (opt) {
			case 1:		
				if (PlayerParty.getMoney() < 40) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 40);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("kortaszis", 20, Elements.NONE));
				break;
			case 2:		
				if (PlayerParty.getMoney() < 30) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 30);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("kasiechida", 20, Elements.FIRE));
				break;
			case 3:		
				if (PlayerParty.getMoney() < 60) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 60);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("zmosrobo", 40, Elements.NONE));
				break;
			case 4:		
				if (PlayerParty.getMoney() < 60) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 60);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("michutara", 40, Elements.WATER));
				break;
			case 5:		
				if (PlayerParty.getMoney() < 125) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 125);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("diejdomas", 80, Elements.NONE));
				break;
			case 6:		
				if (PlayerParty.getMoney() < 125) {
					System.out.println("You don't have enough money!");
					break;
				}
				
				PlayerParty.setMoney(PlayerParty.getMoney() - 125);
				do {
					System.out.print("Who will equip it? [1-" + PlayerParty.getSize() + "] ");
					try {
						c = scanf.nextInt();
						scanf.nextLine();
					}
					catch (Exception e) {
						c = 0;
					}
				} while (c < PlayerParty.getSize());
				
				PlayerParty.getMembers().get(c - 1).setEquipment(new Weapon("horogihara", 80, Elements.EARTH));
				break;
			case 7:
				System.out.println("Thank you for shopping with us");
				System.out.println("");
				Console.pause(scanf);
				break;
			}
		} while (opt != 7);
	}
}
