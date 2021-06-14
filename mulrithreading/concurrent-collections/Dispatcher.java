package concurrentCollections;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {

	public static void main(String[] args) {

		Result result = new Result();

		String filename1 = "text1";
		String filename2 = "text2";
		String filename3 = "text3";

		String path = "src\\";

		Thread fh1 = new Thread(new FileHandler(path, filename1, result));
		Thread fh2 = new Thread(new FileHandler(path, filename2, result));
		Thread fh3 = new Thread(new FileHandler(path, filename3, result));

		fh1.start();
		fh2.start();
		fh3.start();

		try {
			fh1.join();
			fh2.join();
			fh3.join();
		} catch (InterruptedException e) {
		}
		result.outWrappedSet();
		result.outWrappedMap();
	}
}

class Result {
	Map<String, Long> wordsRepeat;
	Set<String> wordsEqualsEndStartLetters;

	Result() {
		this.wordsRepeat = Collections.synchronizedMap(new HashMap<>());
		this.wordsEqualsEndStartLetters = Collections.synchronizedSet(new HashSet<>());
	}

	void addToResultMap(String word) {
		this.wordsRepeat.compute(word, (key, value) -> value == null ? 1 : ++value);
	}

	void addToResultSet(String word) {
		boolean flag = word.endsWith("" + word.charAt(0));
		if (flag)
			synchronized (this) {
				this.wordsEqualsEndStartLetters.add(word);
			}
	}

	String toStringResultMap() {
		String result = "";
		int i = 0;
		for (Map.Entry<String, Long> out : this.wordsRepeat.entrySet()) {
			i++;
			result += String.format(" |%15s = %-5d| ", out.getKey(), out.getValue());
			if (i % 3 == 0)
				result += "\n";

		}
		return result;
	}

	String toStringResultSet() {
		String result = "";
		int i = 0;
		for (String string : wordsEqualsEndStartLetters) {
			i += string.length();
			if (i > 70) {
				result += "\n";
				i = 0;
			}
			result += string + ", ";
		}
		return result;
	}

	void outWrappedMap() {
		String result = "====================================* Map *======================================\n";
		result += "\\./                     \\./\\./                     \\./\\./                     \\./\n";
		result += this.toStringResultMap();
		result += "\n/.\\                     /.\\/.\\                     /.\\/.\\                     /.\\\n";
		result += "=================================================================================\n";
		System.out.println(result);
	}

	void outWrappedSet() {
		String result = "====================================* Set *======================================\n";
		result += "\\./                     \\./\\./                     \\./\\./                     \\./\n";
		result += this.toStringResultSet();
		result += "\n/.\\                     /.\\/.\\                     /.\\/.\\                     /.\\\n";
		result += "=================================================================================\n";
		System.out.println(result);
	}
}

class FileHandler implements Runnable {
	String pathToFile;
	String filename;
	Result result;
	static Pattern pattern = Pattern.compile("\\b[^\\d\\W'\\-]+\\b");

	FileHandler(String pathToFile, String filename, Result result) {
		this.pathToFile = pathToFile;
		this.filename = filename;
		this.result = result;
	}

	void searchResultWords() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.pathToFile + this.filename));
			String line = buffer.readLine();

			while (line != null) {
				this.checkLine(line);
				line = buffer.readLine();
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Something goes wrong!");
		} catch (Exception e) {
			System.out.println("Something very bad goes wrong!!");
		}
	}

	void checkLine(String line) {
		Matcher matcher = FileHandler.pattern.matcher(line);
		String temp = "";
		while (matcher.find()) {
			temp = matcher.group().toLowerCase();
			this.result.addToResultSet(temp);
			this.result.addToResultMap(temp);
		}
	}

	@Override
	public void run() {
		this.searchResultWords();
	}
}
