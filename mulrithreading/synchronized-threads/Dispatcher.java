package threadsSynchron;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {
	public static void main(String[] args) throws InterruptedException {
		String filename1 = "text1"; // Sum is 109
		String filename2 = "text2"; // Sum is 751
		String filename3 = "text3"; // Sum id 85
		String filename4 = "text1"; // Sum is 109
		String filename5 = "text2"; // Sum is 751
		String filename6 = "text3"; // Sum id 85

		String path = "src\\threadsSynchron\\";

		SumHandler sumHandler = new SumHandler();
		SumHandler sumHandler2 = new SumHandler();
		
		FileHandler fileHandler1 = new FileHandler(filename1, path, sumHandler);
		FileHandler fileHandler2 = new FileHandler(filename2, path, sumHandler);
		FileHandler fileHandler3 = new FileHandler(filename3, path, sumHandler);
		FileHandler fileHandler4 = new FileHandler(filename4, path, sumHandler2);
		FileHandler fileHandler5 = new FileHandler(filename5, path, sumHandler2);
		FileHandler fileHandler6 = new FileHandler(filename6, path, sumHandler2);

		Thread fileHandlerThread1 = new Thread(new FileHandlerThread(fileHandler1));
		Thread fileHandlerThread2 = new Thread(new FileHandlerThread(fileHandler2));
		Thread fileHandlerThread3 = new Thread(new FileHandlerThread(fileHandler3));
		Thread fileHandlerThread4 = new Thread(new FileHandlerThread(fileHandler4));
		Thread fileHandlerThread5 = new Thread(new FileHandlerThread(fileHandler5));
		Thread fileHandlerThread6 = new Thread(new FileHandlerThread(fileHandler6));

		long time1 = System.currentTimeMillis();
		fileHandlerThread4.run();
		fileHandlerThread5.run();
		fileHandlerThread6.run();
		long time2 = System.currentTimeMillis();
		
		System.out.println("========================================");
		System.out.println("          Single-stream mode: ");
		System.out.println("         ---------------------         ");
		
		System.out.println(fileHandler4);
		System.out.println(fileHandler5);
		System.out.println(fileHandler6);
		
		System.out.println("Time spent: " + (time2 - time1) + " milis");
		
		time1 = System.currentTimeMillis();

		fileHandlerThread1.start();
		fileHandlerThread2.start();
		fileHandlerThread3.start();

		fileHandlerThread1.join();
		fileHandlerThread2.join();
		fileHandlerThread3.join();

		time2 = System.currentTimeMillis();
		System.out.println("========================================\n");
		System.out.println("========================================");
		System.out.println("          Multi-stream mode: ");
		System.out.println("         --------------------         ");
		
		System.out.println(fileHandler1);
		System.out.println(fileHandler2);
		System.out.println(fileHandler3);

		System.out.println("Time spent: " + (time2 - time1) + " milis");

		System.out.println("========================================");
		System.out.println();
		System.out.println(sumHandler);
	
	}
}

class SumHandler {
	private int generalSum;

	SumHandler() {
	}

	synchronized void addToGeneralSum(int value) {
		generalSum += value;
	}

	int getGeneralSum() {
		return this.generalSum;
	}

	@Override
	public String toString() {
		return "General sum of numbers from all files is " + this.generalSum;
	}
}

class FileHandler {
	private int sum;
	String filename;
	String path;
	SumHandler sumHandler;

	FileHandler(String filename, String path, SumHandler sumHandler) {
		this.filename = filename;
		this.path = path;
		this.sumHandler = sumHandler;
	}

	int getSum() {
		return this.sum;
	}

	@Override
	public String toString() {
		return "Sum of numbers from file: <" + filename + "> is " + this.sum;
	}

	void getSumNumbersFromFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.path + this.filename));
			String line = buffer.readLine();
			int temp;
			while (line != null) {
				temp = getSumFromLine(line);
				// Add to local sum of object fileHandler
				this.addToSum(temp);
				// Add to general sum of sumHandler 
				// using synchronized method
				this.sumHandler.addToGeneralSum(temp);
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

	int getSumFromLine(String line) {
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(line);
		int tempSum = 0;
		while (matcher.find()) {
			tempSum += Integer.parseInt(matcher.group());
		}
		return tempSum;
	}

	void addToSum(int value) {
		sum += value;
	}
}

class FileHandlerThread implements Runnable {
	FileHandler fileHandler;

	FileHandlerThread(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	@Override
	public void run() {
		this.fileHandler.getSumNumbersFromFile();
	}
}