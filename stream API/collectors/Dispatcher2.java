package collectors;

import java.util.HashMap;

public class Dispatcher2 {

	public static void main(String[] args) {
		String textEN = "aaaxxeeuuii, asd qwe. aalllsll sdd. qwertyu gggacmn. zxcvbnm asd. asdfgh.";
		// 0: v=11, c=6, diff=5
		// 1: v=2, c=9, diff=7
		// 2: v=4, c=10, diff=6
		// 3: v=1, c=9, diff=8
		// 4: v=1, c=5, diff=4

		System.out.println(Task.doTask(textEN));

	}

}

class Task {
	static int difference = 0;
	static int currentSentence = 0;
	static HashMap<Integer, Integer> differencesVowelConsonant = new HashMap<>();

	static HashMap<Integer, Integer> doTask(String text) {
		difference = 0;
		currentSentence = 0;
		differencesVowelConsonant = new HashMap<>();
		text.chars().forEach(c -> {
			checkByEndOfSentence(c);
			checkByLetters(c);
		});
		return differencesVowelConsonant;
	}

	static boolean isVowel(int c) {
		// It's easier to lowercase 1 character than to check the extra 6 characters
		// It is easier to check vowel letters than consonants
		// because there are fewer count of them
		return String.valueOf(Character.toChars(c)).toLowerCase().matches("[aeiouy]");
	}

	static void checkByEndOfSentence(int c) {
		if (c == 46 || c == 63 || c == 33) { // ASCII code of char .(46) ?(63) !(33)
			differencesVowelConsonant.put(currentSentence, Math.abs(difference));
			currentSentence++;
			difference = 0;
		}
	}

	static void checkByLetters(int c) {
		if (Character.isAlphabetic(c))
			difference += isVowel(c) ? 1 : -1;
	}
}
