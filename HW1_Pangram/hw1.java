/**
 * @author Winnie Liang
 * @title CSE 305 Assignment 1 
 * @purpose A function that accepts two strings (the names of input and output files) as arguments. 
 * Reads the input file line-by-line and checks if the line is a pangram. Lastly, the program outputs
 * the boolean value in the provided output file. 
 * A pangram is a sentence that contains all the letters of the English alphabet at least once. 
 * Example: "The quick brown fox jumps over the lazy dog" - True 
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class hw1 {
	public static void hw1(String inFile, String outFile) throws IOException{
	
		//File streams to read input and write to output	
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		PrintWriter out = new PrintWriter(new FileWriter(outFile));
		
		try {
			String line; 
			while ((line = in.readLine()) != null){
				String str = line.toLowerCase();
				if(isPangram(str) == false){
					out.println("false");	
					out.print("");
				}
				else{
					out.println("true");
					out.print("");
				}	
			}
			in.close();
			out.close(); //Closes the file. 
	
		} catch (FileNotFoundException e) {
			System.err.println("Unable to read input file.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to read line from input file.");
		}
		
	}
	
	//Helper method to check boolean value of a string for pangram check
	public static boolean isPangram(String line){
		if(line.length() < 26){
			return false;
		}
		for(char ch ='a'; ch <= 'z'; ch++){
			if(line.indexOf(ch) < 0){
				return false;
			}
		}
		return true;
	}
	
	//Function call to test code
	
	public static void main(String[] args) throws FileNotFoundException{
		try {
			new hw1().hw1("sample_input_2.txt", "sample_output_2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
     
	
}

