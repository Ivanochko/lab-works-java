package concurrentLock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {

	public static void main(String[] args) {

		for (int i = 0; i < 50; i++) {
			String filename1 = "file1.txt";
			String filename2 = "file2.txt";
			String filename3 = "file3.txt";

			String path = "src\\";

			String regex = "-?\\d+\\.?\\d*";

			FileHandler file1 = new FileHandler(filename1, path, regex);
			FileHandler file2 = new FileHandler(filename2, path, regex);
			FileHandler file3 = new FileHandler(filename3, path, regex);

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
			} catch (InterruptedException ignored) {
			}

			System.out.println("Sum of all numbers from all files = " + FileHandler.sum);
			FileHandler.sum = 0;
		}
	}
}

class FileHandler {
	static double sum;
	String filename;
	String path;
	String regex;
	Pattern pattern;
	static Lock lock = new ReentrantLock();

	FileHandler(String filename, String path, String regex) {
		this.filename = filename;
		this.path = path;
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	void getSumOfNumberFromFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.path + this.filename));
			String line = buffer.readLine();
			double sum;

			while (line != null) {
				sum = getSumFromLine(line);
				this.addToSum(sum);
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

	void addToSum(double sumToAdd) {
		lock.lock();
		try {
			sum += sumToAdd;
		} finally {
			lock.unlock();
		}
	}

	double getSumFromLine(String line) {
		Matcher matcher = this.pattern.matcher(line);
		double tempSum = 0;
		while (matcher.find())
			tempSum += Double.parseDouble(matcher.group());
		
		return tempSum;
	}
}

class FileHandlerThread implements Runnable {
	FileHandler fileHandler;

	FileHandlerThread(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	@Override
	public void run() {
		this.fileHandler.getSumOfNumberFromFile();
	}
}
