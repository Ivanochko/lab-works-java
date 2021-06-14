package threads2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher2 {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		String filename1 = "src\\threads2\\text1";
		String filename2 = "src\\threads2\\text1";
		String filename3 = "src\\threads2\\text3";

		String regex = "[`'\"()<>;,-.?:=+!/\\&@*/^~_§{}|\\[\\]]";

		FileHandler fh1 = new FileHandler(0, filename1, regex);
		FileHandler fh2 = new FileHandler(1, filename2, regex);
		FileHandler fh3 = new FileHandler(2, filename3, regex);

		ThreadCall tc1 = new ThreadCall(fh1);
		ThreadCall tc2 = new ThreadCall(fh2);
		ThreadCall tc3 = new ThreadCall(fh3);
		
		ArrayList<String> listFiles = new ArrayList<>();
		listFiles.add(filename1);
		listFiles.add(filename2);
		listFiles.add(filename3);
		
		Model model = new Model(listFiles);
		
		List<ThreadCall> lci = new ArrayList<>();
		lci.add(tc1);
		lci.add(tc2);
		lci.add(tc3);

		ExecutorService ex = Executors.newFixedThreadPool(3) ;// newCachedThreadPool();
		
		long time1 = System.nanoTime();

		List<Future<Integer>> lfi = ex.invokeAll(lci);
		
		int i = 0;
		for (Future<Integer> temp : lfi) {
			model.setValueInMap(i, temp.get());
			i++;
		}
		ex.shutdown();		
		long time2 = System.nanoTime();
		
		System.out.println(model);
		System.out.println(time1 + " " + time2);
	}
}

class Model {
	HashMap<Integer, Integer> resultMap = new HashMap<>();
	ArrayList<String> arrayNamesOfFiles;
	int numberOfFiles;

	Model(ArrayList<String> arrayNamesOfFiles) {
		this.arrayNamesOfFiles = arrayNamesOfFiles;
		this.numberOfFiles = arrayNamesOfFiles.size();
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

class FileHandler {
	int idFile;
	String filename;
	String regexStr; // = "[`'\"()<>;,-.?:!=+/\\&@*/^~_§{}|\\[\\]]";
	Pattern pattern;

	public FileHandler(int idFile, String filename, String regexStr) {
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

class ThreadHandler {
	int idFile;
	String filename;
	// future result
	int countSymbols = 0;
	// regular string for pattern
	static String regexStr = "[`'\"()<>;,-.?:=+!/\\&@*/^~_§{}|\\[\\]]";
	// pattern with all symbols from regexStr
	static Pattern pattern = Pattern.compile(ThreadHandler.regexStr);

	public ThreadHandler(int idFile, String filename) {
		this.idFile = idFile;
		this.filename = filename;
	}

	public int getCountSymbols() {
		return this.countSymbols;
	}

//	public void getCountFromLine(String line) {
//		this.countSymbols += this.getCountFromLine(line, this.countSymbols);
//	}

	public int getCountFromLine(String line, int count) {
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

	public void openFileAndCalculateCountForRunnable() {
		this.countSymbols = this.openFileAndCalculateCountForCallable();
//		try {
//			BufferedReader buffer = new BufferedReader(new FileReader(this.filename));
//			// read first line
//			String line = buffer.readLine();
//			while (line != null) {
//				this.getCountFromLine(line);
//				line = buffer.readLine();
//			}
//			buffer.close();
//
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found!");
//		} catch (IOException e) {
//			System.out.println("Something goes wrong!");
//		}
	}

}

class ThreadRunnable extends ThreadHandler implements Runnable {
	public ThreadRunnable(int idFile, String filename) {
		super(idFile, filename);
	}

	@Override
	public void run() {
		this.openFileAndCalculateCountForRunnable();
	}
}

class ThreadCall implements Callable<Integer> {
	FileHandler handler;

	public ThreadCall(FileHandler handler) {
		this.handler = handler;
	}

	@Override
	public Integer call() throws Exception {
		return handler.openFileAndCalculateCountForCallable();
	}
}
