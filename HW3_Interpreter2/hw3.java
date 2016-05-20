/*  @author Winnie Liang
    @title CSE 305 Assignment 3
    @purpose To implement an interpreter using a stack. The interpreter should read an input file (input.txt) which contains lines of expressions, evaluate them and push the results onto a stack, then print the content of the stack to an output file (output.txt) when exit.  
    It must handle the following expressions:
    1) push <num>, 2) pop, 3) boolean, 4) error, 5) add, 6) sub, 7) mul, 8) div, 9) rem, 10) neg, 11) swap, 12) quit
   
	New functions to implement for Interpreter II:
    13) and, 14) or, 15) not 16) equal 17) lessThan 18) bind 19) if 20) let 21) end
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Arrays;
import java.util.Iterator;
//import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class hw3{
	public static void hw3(String inFile, String outFile) throws IOException {
		//File streams to read input and write to output
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		PrintWriter out = new PrintWriter(new FileWriter(outFile));

		try{
			String line;
			Stack<String> stack = new Stack<String>();
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			//HashMap<String, Integer> newEnvMap = new HashMap<String, Integer>(); 
			HashMap<Stack, HashMap> newEnvMap = new HashMap<Stack, HashMap>();
			HashMap<String,Integer> tempMap = new HashMap<String,Integer>(); 
			ArrayList<HashMap> environmentList = new ArrayList<HashMap>();
			while ((line = in.readLine()) != null){
				//For basic mathematical operators (+-*/)
				int x = 0;
				int y = 0;
				int r = 0;
				String result = "";
				//For comparison logical operators (AND,OR,NOT,LESSTHAN,IF)
				String bool_val1 = "";
				String bool_val2 = "";
				String string1 = "";
				String string2 = "";
				String string3 = "";
				//For binding
				int value = 0; 
				int value2 = 0;
				String name1 = "";
				String name2 = "";

/*----------------------------PUSH-----------------------------*/
				if(line.contains("push")){
					//result = line.replaceAll("[a-zA-Z]+ ","");
					result = line.substring(5); // splits "push" from the line 
           		    if(is_Integer(result)){      // _num_
           		    	if(result.contains("-0")){
           		    		result = result.replaceAll("-0", "0");
           		    	}
           		    	stack.push(result);
           		    }
           		    else if(is_String_Literal(result)){ // _string_literal_
           		    	stack.push(result);
           		    	//result = result.replaceAll("\"", "");	
           		    	//then the result gets pushed to the stack in the next if conditional  	
           		    }
           		    else if(is_Name(result)){ // _name_
           		    	stack.push(result);
           		    }
           		    else{
           		    	stack.push(":error:");
           		    }
				}
/*----------------------------POP-----------------------------*/
				else if(line.contains("pop")){
					if(stack.size() < 1){
						stack.push(":error:");
					}
					else if(stack.size() >= 1){
						stack.pop();
					}
				}
/*---------------------------:TRUE:-----------------------------*/
				else if(line.contains(":true:")){
					stack.push(line);
				}
/*---------------------------:FALSE:-----------------------------*/
				else if(line.contains(":false:")){
					stack.push(line);
				}
/*--------------------------:ERROR:-----------------------------*/
				else if(line.contains(":error:")){
					stack.push(line);
				}
/*----------------------------ADD-----------------------------*/
				else if(line.contains("add")){	
					//newEnvMap = environmentList.remove(environmentList.size());
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x + y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Name(stack.peek())){
								name1 = stack.pop();
								y = map.get(name1);
								r = x + y;
								result = Integer.toString(r);
								stack.push(result);
							}							
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							x = map.get(name1);
							if(is_Name(stack.peek())){
								name2 = stack.pop();
								y = map.get(name2);
								r = x + y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x + y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(name1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------SUB-----------------------------*/
				else if(line.contains("sub")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = y - x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Name(stack.peek())){
								name1 = stack.pop();
								y = map.get(name1);
								r = y - x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							x = map.get(name1);
							if(is_Name(stack.peek())){
								name2 = stack.pop();
								y = map.get(name2);
								r = y - x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = y - x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(name1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------MUL-----------------------------*/
				else if(line.contains("mul")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x*y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Name(stack.peek())){
								name1 = stack.pop();
								y = map.get(name1);
								r = x*y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							x = map.get(name1);
							if(is_Name(stack.peek())){
								name2 = stack.pop();
								y = map.get(name2);
								r = x*y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x*y;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(name1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------DIV-----------------------------*/
				else if(line.contains("div")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								if(x > 0){
									y = Integer.parseInt(stack.pop());
									r = y / x;
									result = Integer.toString(r);
									stack.push(result);
								}
								else{
									stack.push(Integer.toString(x));
									stack.push(":error:");
								}
							}
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							x = map.get(name1);
							if(is_Name(stack.peek())){
								name2 = stack.pop();
								y = map.get(name2);
								r = y / x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = y / x;
								result = Integer.toString(r);
								stack.push(result);
							}
							else{
								stack.push(name1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------REM-----------------------------*/
				else if(line.contains("rem")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								if(x > 0){
									y = Integer.parseInt(stack.pop());
									r = y%x;
									result = Integer.toString(r);
									stack.push(result);
								}
								else{
									stack.push(Integer.toString(x));
									stack.push(":error:");
								}
							}
							else if(is_Name(stack.peek())){
								name1 = stack.pop();
								x = map.get(name1);
								if(is_Name(stack.peek())){
									name2 = stack.pop();
									y = map.get(name2);
									r = y%x;
									result = Integer.toString(r);
									stack.push(result);
								}
								else if(is_Integer(stack.peek())){
									y = Integer.parseInt(stack.pop());
									r = y%x;
									result = Integer.toString(r);
									stack.push(result);
								}
								else{
									stack.push(Integer.toString(x));
									stack.push(":error:");
								}
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------NEG-----------------------------*/
				else if(line.contains("neg")){
					if(stack.size() >= 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
								r = -1 * x;
								result = Integer.toString(r);
								stack.push(result);
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							x = map.get(name1);
							r = -1 * x;
							result = Integer.toString(r);
							stack.push(result);
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------SWAP-----------------------------*/
				else if(line.contains("swap")){
					if(stack.size() > 1){
						String a, b;
						a = stack.pop();
						b = stack.pop();
						stack.push(a);	
						stack.push(b);
					}
					else{
						stack.push(":error:");
					}
					
				}
/***********---------HW3 STARTS HERE --- ADDING MORE FUNCTIONS-----------------**********/
/*-----------------------------AND------------------------------*/
				else if(line.contains("and")){
					if(stack.size() > 1){
						if(is_Boolean(stack.peek())){
							bool_val1 = stack.pop().replaceAll(":","");
							if(is_Boolean(stack.peek())){
								bool_val2 = stack.pop().replaceAll(":","");
								//Now check the top 2 bool values and apply AND operator
								//T and T will always give you T, otherwise F.
								if(Boolean.valueOf(bool_val1) == true && Boolean.valueOf(bool_val2) == true){
									stack.push(":true:");
								}
								else{
									stack.push(":false:");
								}
							}
							else{
								stack.push(bool_val1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*-----------------------------OR------------------------------*/
				else if(line.contains("or")){
					if(stack.size() > 1){			
						if(is_Boolean(stack.peek())){
							bool_val1 = stack.pop().replaceAll(":","");
							if(is_Boolean(stack.peek())){
								bool_val2 = stack.pop().replaceAll(":","");
								//Now check the top 2 bool values and apply OR operator
								//F and F will always give you F, otherwise T. 
								if(Boolean.valueOf(bool_val1) == false && Boolean.valueOf(bool_val2) == false){
									stack.push(":false:");
								}
								else{
									stack.push(":true:");
								}
							}
							else{
								stack.push(bool_val1);
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					
					}
					else{
						stack.push(":error:");
					}				
				}
/*-----------------------------NOT-----------------------------*/
				else if(line.contains("not")){
					if(stack.size() > 0){
						if(is_Boolean(stack.peek())){
							bool_val1 = stack.pop().replaceAll(":","");
							//Now check the top bool value and apply NOT operator
							//T gives F and F gives T. 
							if(Boolean.valueOf(bool_val1) == true){
								stack.push(":false:");
							}
							else{ //false
								stack.push(":true:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}				
				}
/*----------------------------EQUAL----------------------------*/
				else if(line.contains("equal")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								if(x == y){
									stack.push(":true:");
								}
								else{
									stack.push(":false:");
								}
							}
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}					
				}
/*--------------------------LESSTHAN----------------------------*/
				else if(line.contains("lessThan")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(is_Integer(stack.peek())){
								y = Integer.parseInt(stack.pop());
								if(y < x){
									stack.push(":true:");
								}
								else{
									stack.push(":false:");
								}
							}
							else{
								stack.push(Integer.toString(x));
								stack.push(":error:");
							}
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}
				}
/*----------------------------BIND------------------------------*/
				else if(line.contains("bind")){
					if(stack.size() > 1){
						if(is_Integer(stack.peek())){
							value = Integer.parseInt(stack.pop());
							if(is_Name(stack.peek())){
								name1 = stack.pop();
								//Bind the integer values to a name by storing the pair on a map
								map.put(name1, value);
								stack.push(":unit:");
							}
							else{
							 	stack.push(Integer.toString(x));
							 	stack.push(":error:");
							}
						}
						else if(stack.peek() == ":unit:"){
							stack.pop();
							if(is_Name(stack.peek())){
								name2 = stack.pop();
								stack.push(":unit:");
							}
							else{
								stack.push(":unit:");
								stack.push(":error:");
							}
						}
						else if(is_Name(stack.peek())){
							name1 = stack.pop();
							if(is_Name(stack.peek())){	
								name2 = stack.pop();
								value = map.get(name1);
								map.put(name2, value);
								stack.push(":unit:");	
							}
							else{
								stack.push(name1);
								stack.push(":error:");
							}
						}
						else if(stack.peek() == ":error:"){
							stack.pop();
							stack.pop();
							stack.push(":error:");
						}
						else{
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}					
				}
/*-----------------------------IF------------------------------*/
				else if(line.contains("if")){
					if(stack.size() > 1){
						string1 = stack.pop();	
						string2 = stack.pop();
						if(is_Boolean(stack.peek())){
							string3 = stack.pop().replaceAll(":","");
							if(Boolean.valueOf(string3) == true){
								stack.push(string1);
							}
							else{
								stack.push(string2);
							}
						}
						else{
							stack.push(string2);
							stack.push(string1);
							stack.push(":error:");
						}
					}
					else{
						stack.push(":error:");
					}	
						
				}
/*-----------------------------LET-------------------------*/
				else if(line.contains("let")){
					//store the stack and the corresponding map that contains bind info
					newEnvMap.put(stack, map);
					environmentList.add(newEnvMap);
				}
/*-----------------------------END-------------------------*/
				else if(line.contains("end")){		
					environmentList.remove(environmentList.size()-1);
				}			

/*----------------------------QUIT-----------------------------*/
				else if(line.contains("quit")){
					Iterator<String> iter = stack.iterator();
					while(iter.hasNext()){
						out.print(stack.pop().replaceAll("\"","") + "\n");
					}
				}			
				else{
					System.err.println("File input has unrecognizable commands.");
				}
			}
			in.close(); //closes the streams
			out.close();
		}catch(FileNotFoundException e){
			System.err.println("Unable to find input file");
		}catch(IOException e){
			e.printStackTrace();
			System.err.println("Unable to read line from input file.");
		}
	}




	//Helper method that checks if the line read is an integer.
	public static boolean is_Integer(String s){
		try{
			Integer.parseInt(s);
		} catch(NumberFormatException e){
			return false;
		} catch(NullPointerException e){
			return false;
		}
		return true;
	}

	public static boolean is_Boolean(String s){
		if(s.contains("true") || s.contains("false")){
			return true;
		}
		return false;
	}

	//Helper method that checks if the line read is a string literal (a string surrounded by quotes)
	public static boolean is_String_Literal(String s){
		if(s.contains("\"")){
			return true;
		}
		return false;
	}

	//Helper method that checks if the line is a name (first character must be a letter)
	public static boolean is_Name(String s){
		//return Pattern.compile("^[A-Za-z]").matcher(s).find();
		//String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char c = s.charAt(0);
		return Character.isLetter(c);
	}


	//Function call to hw3 to run the program
	// public static void main(String [] args) throws IOException{
	// 	try{
	// 		new hw3().hw3("inp3.txt", "testoutput.txt");
	// 	}catch(IOException e){
	// 		e.printStackTrace();
	// 		System.err.println("Unable to read line from input file.");
	// 	}
	// }
}















