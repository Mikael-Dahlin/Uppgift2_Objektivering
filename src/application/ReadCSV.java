package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author mikael
 * Class that handles the logic, reading and saving the ".csv" file .
 */
public class ReadCSV {

	// Declaration of global variables.
	private String COMMA_DELIMITER = ",";

	private ArrayList<CsvRow> wholeSheet = new ArrayList<>();
	private ArrayList<CsvRow> sortedSheet = new ArrayList<>();
	private ArrayList<String> sheetHeaders = new ArrayList<String>();

	/*
	 * Method that reads the CSV-file.
	 */
	public void loadCSV() {
		// Skips if the file has been read already.
		if (wholeSheet.isEmpty()) {
			String path = "sample.csv";
			String line = "";
		
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				Boolean headers = true;
				while (line != null) {
					line = br.readLine();
					if(line != null) {
						getRows(line, headers);
						headers = false;
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Method that handles each row of the file.
	 */
	private void getRows(String row, Boolean headers) {
		// Declaration of variables.
		ArrayList<String> rowValues = new ArrayList<String>();
		ArrayList<String> exceptions = new ArrayList<String>();
		
		// Scans the row and puts the values into an ArrayList based on the delimiter.
		try (Scanner rowScanner = new Scanner(row)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			
			// Check if there are any quotes and add them to exceptions.
			String quotes[] = row.split("\"");
			for (int j = 0; j < quotes.length; j++) {
				if (j % 3 == 1) {
					exceptions.add(quotes[j]);
				}
			}

			// Loop through all items and add them to an ArrayList.
			int e = 0;
			boolean isAdded = true;
			
			while (rowScanner.hasNext()) {
				String currentCell = rowScanner.next();
				
				if (currentCell.contains("\"") && isAdded) {
					currentCell = exceptions.get(e);
					rowValues.add(currentCell);
					isAdded = false;
					e++;
				} else if (isAdded){
					rowValues.add(currentCell);				
				} else {
					isAdded = true;
				}
				
			} 
		}
		
		// First row is turned into a headers array.
		if (headers) {
			sheetHeaders.addAll(rowValues);
			for (String header : sheetHeaders) {
				sheetHeaders.set(sheetHeaders.indexOf(header), header.toLowerCase());
			}
		} else {
			// All other rows gets put as an Object in the wholeSheet ArrayList.
			wholeSheet.add(new CsvRow(rowValues, sheetHeaders));
		}
	}
	
	/*
	 * Method for sorting the sheet.
	 */
	public ArrayList<CsvRow> sortedCSV(String header) {
		// Loads the CSV-file if it is the first method called.
		loadCSV();
		
		if(sortedSheet.isEmpty()) {			
			sortedSheet.addAll(wholeSheet);
		}
		
		// Sort the sheet (as strings) based on the provided header.
		sortedSheet.sort((CsvRow o1, CsvRow o2) -> 
						((String) o1.getRowArray().get(sheetHeaders.indexOf(header.toLowerCase())))
						.compareTo((String) o2.getRowArray().get(sheetHeaders.indexOf(header.toLowerCase()))));

		// Prints the whole sheet or just selected column.
		return sortedSheet;
	}

	/*
	 * Method that saves the data in a ".csv" file. (Takes the file as parameter)
	 */
	public void saveFile(File file) {
		
		// Check if there is a file.
		if (file == null){
			System.out.println("Not file selected, the sheet was not saved!");
		} else {
			// Print the data to a ".csv" file.
			try {
				// Declare variables.
				PrintWriter pw = new PrintWriter(file.getName());
				String text = "";
				String cell = "";
				ArrayList<String> row;
				
				// Add the headers.
				for (int i = 0; i < sheetHeaders.size() - 1;i++) {
					text += sheetHeaders.get(i);
					text += ",";
				}
				text += sheetHeaders.get(sheetHeaders.size() - 1);
				text += "\n";
				
				// Add the rest of the data.
				for(int i = 0; i < wholeSheet.size(); i++) {
					row = wholeSheet.get(i).getRowArray();
					for (int j = 0;j < row.size();j++) {
						cell = row.get(j);
						if (cell.contains(",")) {
							cell = "\"" + cell + "\"";
						}
						text += cell;
						if (j < row.size() - 1) {
							text += ",";
						}
					}
					if (i < wholeSheet.size() - 1) {
						text += "\n";
					}
				}
				
				// Write to file and close writer.
				pw.write(text);
				pw.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}	
	}
}