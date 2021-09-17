package application;

import java.util.ArrayList;

/**
 * 
 * @author mikael
 * Class that holds all the row data and what headers there are.
 */
public class CsvRow {

	// Private class variables.
	private ArrayList<String> headers = new ArrayList<String>();
	private ArrayList<String> rowArray = new ArrayList<String>();

	/*
	 * Constructor that takes in the row as an ArrayList of strings.
	 */
	public CsvRow(ArrayList<String> rowValues, ArrayList<String> rowHeaders) {
				
		// Make sure that there is data for each header.
		while(rowValues.size() < rowHeaders.size()) {
			rowValues.add("");
		}
		
		headers.addAll(rowHeaders);
		rowArray.addAll(rowValues);
	}
	
	/*
	 * Getters.
	 */	
	public ArrayList<String> getHeaders() {
		return headers;
	}

	public ArrayList<String> getRowArray() {
		return rowArray;
	}

	/*
	 * Setters.
	 */
	public void setIndex(int indexOf, String text) {
		rowArray.set(indexOf, text);
	}	
}