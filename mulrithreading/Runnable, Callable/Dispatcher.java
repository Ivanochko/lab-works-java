package threads2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {
	public static void main(String[] args) throws InterruptedException {
		ArrayList<String> listOfFiles = new ArrayList<>();

		Model model = new Model(listOfFiles);

		ThreadRunnable tr1 = new ThreadRunnable(0, "src\\threads2\\text1");
		Thread firstThread = new Thread(tr1);

		ThreadRunnable tr2 = new ThreadRunnable(1, "src\\threads2\\text1");
		Thread secondThread = new Thread(tr2);

		ThreadRunnable tr3 = new ThreadRunnable(2, "src\\threads2\\text3");
		Thread thirdThread = new Thread(tr3);

		long startTime = System.currentTimeMillis();

		firstThread.start();
		secondThread.start();
		thirdThread.start();
		
		firstThread.join();
		secondThread.join();
		thirdThread.join();

		long endTime = System.currentTimeMillis();

		System.out.println("Time of work: " + (endTime - startTime));

		model.setValueInMap(tr1.idFile, tr1.countSymbols);
		model.setValueInMap(tr2.idFile, tr2.countSymbols);
		model.setValueInMap(tr3.idFile, tr3.countSymbols);

		System.out.println(model);

		ThreadCall c1 = new ThreadCall(new FileHandler(3, "src\\threads2\\text1", "[`'\"()<>;,-.?:!/\\&@*/^~_§{}|\\[\\]]"));
		
		FutureTask<ThreadCall> task = new FutureTask(c1); // перетворення Callable одночасно в Future і Runnable
		Thread tc = new Thread(task);

		tc.start();
		tc.join();
		int symbolCount = 0;
		try {
			symbolCount = task.get().call();
		} catch (Exception ee) {
		}
		System.out.println(symbolCount);
	}
}

class Model2 {
	HashMap<Integer, Integer> resultMap = new HashMap<>();
	ArrayList<String> arrayNamesOfFiles;
	int numberOfFiles;

	Model2(ArrayList<String> arrayNamesOfFiles) {
		this.arrayNamesOfFiles = arrayNamesOfFiles;
		this.numberOfFiles = arrayNamesOfFiles.size();

		for (int i = 0; i < numberOfFiles; i++) {
			// put start value on HashMap
			this.resultMap.put(i, 0);
		}
	}

	void setValueInMap(int idFile, int value) {
		this.resultMap.put(idFile, value);
	}

	@Override
	public String toString() {
		String result = "";
		for (Map.Entry<Integer, Integer> out : this.resultMap.entrySet()) {
			result += "File number " + out.getKey() + " has a " + out.getValue() + " symbols\n";
		}
		return result;
	}
}

class FileHandler2 {
	int idFile;
	String filename;
	String regexStr; // = "[`'\"()<>;,-.?:!=+/\\&@*/^~_§{}|\\[\\]]";
	Pattern pattern;
	public FileHandler2(int idFile, String filename, String regexStr) {
		this.idFile = idFile;
		this.filename = filename;
		this.regexStr = regexStr;
		this.pattern = Pattern.compile(this.regexStr);
	}


	public int openFileAndCalculateCountForCallable() {
		int count = 0;
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.filename));
			// read first line
			String line = buffer.readLine();
			while (line != null) {
				count = this.getCountFromLine(line, count);
				line = buffer.readLine();
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Something goes wrong!");
		}
		return count;
	}

	public int getCountFromLine(String line, int count) { //
		Matcher matcher = this.pattern.matcher(line);
		while (matcher.find()) {
			count++;
		}
		return count;
	}
}

class ThreadHandler2 {
	int idFile;
	String filename;
	// future result
	int countSymbols = 0; // not for Callable
	// regular string for pattern
	static String regexStr = "[`'\"()<>;,-.?:!/\\&@*/^~_§{}|\\[\\]]";
	// pattern with all symbols from regexStr
	static Pattern pattern = Pattern.compile(ThreadHandler.regexStr);

	public ThreadHandler2(int idFile, String filename) {
		this.idFile = idFile;
		this.filename = filename;
	}

	public int getCountSymbols() {
		return this.countSymbols;
	}

	public void getCountFromLine(String line) { // for Runnable
		Matcher matcher = ThreadHandler.pattern.matcher(line);
		while (matcher.find()) {
			this.countSymbols++;
		}
	}

	public int getCountFromLine(String line, int count) { //
		Matcher matcher = ThreadHandler.pattern.matcher(line);
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	public int openFileAndCalculateCountForCallable() {
		int count = 0;
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.filename));
			// read first line
			String line = buffer.readLine();
			while (line != null) {
				count = this.getCountFromLine(line, count);
				line = buffer.readLine();
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Something goes wrong!");
		}
		return count;
	}

	public void openFileAndCalculateCount() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(this.filename));
			// read first line
			String line = buffer.readLine();
			while (line != null) {
				this.getCountFromLine(line);
				line = buffer.readLine();
			}
			buffer.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Something goes wrong!");
		}
	}

}

class ThreadRunnable2 extends ThreadHandler implements Runnable {
	public ThreadRunnable2(int idFile, String filename) {
		super(idFile, filename);
	}

	@Override
	public void run() {
		openFileAndCalculateCountForRunnable();
	}
}

class ThreadCall2 implements Callable {
	FileHandler handler;

	public ThreadCall2(FileHandler handler) {
		this.handler = handler;
	}

	@Override
	public Integer call() throws Exception {
		int result = handler.openFileAndCalculateCountForCallable();
		return result;
	}
}
