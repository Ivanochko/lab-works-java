package threadsAtomic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {

	public static void main(String[] args) {

		String filename1 = "text1";
		String filename2 = "text2";
		String filename3 = "text3";

		String path = "src\\threadsAtomic\\";

		String regex = "\\b[aeouyiAEOUYI]\\w*[aeouyiAEOUYI]\\b";

		SumHandler sum = new SumHandler();

		FileHandler file1 = new FileHandler(filename1, path, sum, regex);
		FileHandler file2 = new FileHandler(filename2, path, sum, regex);
		FileHandler file3 = new FileHandler(filename3, path, sum, regex);

		Thread fileTr1 = new Thread(new FileHandlerThread(file1));
		Thread fileTr2 = new Thread(new FileHandlerThread(file2));
		Thread fileTr3 = new Thread(new FileHandlerThread(file3));

		fileTr1.start();
		fileTr2.start();
		fileTr3.start();

		try {
			fileTr1.join();
			fileTr2.join();
			fileTr3.join();
		} catch (InterruptedException e) {
		}

		System.out.println(sum);
	}
}

class SumHandler {
	LongAdder lAdder;

	SumHandler() {
		lAdder = new LongAdder();
	}

	long getGeneralSum() {
		return this.lAdder.sum();
	}

	@Override
	public String toString() {
		return "Count of words that match with regex from all files is " + this.lAdder.sum();
	}
}

class FileHandler {
	String filename;
	String path;
	String regex;
	SumHandler sumHandler;
	Pattern pattern;

	FileHandler(String filename, String path, SumHandler sumHandler, String regex) {
		this.filename = filename;
		this.path = path;
		this.sumHandler = sumHandler;
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	void getCountWordsFromFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.path + this.filename));
			String line = buffer.readLine();
			int count;

			while (line != null) {
				count = getCountWordsLine(line);
				this.addToAdder(count);
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

	void addToAdder(int count) {
		this.sumHandler.lAdder.add(count);
	}

	int getCountWordsLine(String line) {
		Matcher matcher = this.pattern.matcher(line);
		int tempCount = 0;
		while (matcher.find())
			tempCount++;

		return tempCount;
	}
}

class FileHandlerThread implements Runnable {
	FileHandler fileHandler;

	FileHandlerThread(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	@Override
	public void run() {
		this.fileHandler.getCountWordsFromFile();
	}
}
