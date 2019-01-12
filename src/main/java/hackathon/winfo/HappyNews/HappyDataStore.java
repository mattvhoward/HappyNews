package hackathon.winfo.HappyNews;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HappyDataStore {

	private Map<String, String[]> numbers;
	private String[] news;


	public HappyDataStore(String dbLoc) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(dbLoc + "/Users/Matt/numbers"));
		this.numbers = new HashMap<String, String[]>();
		for (int i = 0; sc.hasNextLine(); i++) {
			String[] line = sc.nextLine().split(", ");
			this.numbers.put(line[0], line);
		}

		sc = new Scanner(new File(dbLoc + "/Users/Matt/news"));
		this.news = new String[100];
		for (int i = 0; sc.hasNextLine() && i < 100; i++) {
			news[i] = sc.nextLine();
		}
	}

	public Set<String> getNumbers() {
		return numbers.keySet();
	}

	public String[] getNews() {
		return news;
	}

	public void addNumber(String num) {
		numbers.put(num, null);
	}

	public void close() {

	}
}