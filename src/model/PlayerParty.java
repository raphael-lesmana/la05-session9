package model;

import java.util.ArrayList;

/*
 * PlayerParty.java
 * A class for managing the player's party. Uses static functions
 * and methods to ensure that every time a method is executed, it is
 * reading the same array list.
 */

public final class PlayerParty {
	private static ArrayList<Person> members = new ArrayList<Person>();
	private static ArrayList<Item> inventory = new ArrayList<Item>();
	private static int money = 609;
	
	public static ArrayList<Person> getMembers() {
		return members;
	}
	
	public static void setMembers(ArrayList<Person> members) {
		PlayerParty.members = members;
	}
	
	public static void addMember(Person member) {
		members.add(member);
	}
	
	public static ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public static void setInventory(ArrayList<Item> inventory) {
		PlayerParty.inventory = inventory;
	}
	
	public static int getMoney() {
		return money;
	}
	
	public static void setMoney(int money) {
		PlayerParty.money = money;
	}
	
	public static int getAverageLvl() {
		int total = 0;
		for (Person member : members) {
			total += member.getLvl();
		}
		return total / members.size();
	}
	
	public static int getSize() {
		return PlayerParty.members.size();
	}
}
