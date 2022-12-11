package main;

/*
 * Console.java
 * A class to assist with console operations.
 */

import java.util.Scanner;

public class Console {
	public static void cls() {
		for (int i = 0; i < 40; i++) {
			System.out.println();
		}
	}
	
	public static void cls(int n) {
		for (int i = 0; i < n; i++) {
			System.out.println();
		}
	}
	
	public static void pause(Scanner scanf) {
		System.out.println("Press enter to continue...");
		scanf.nextLine();
	}
}
