/*  @author Winnie Liang
    @title CSE 305 Assignment 2
    @purpose To implement an interpreter using a stack. The interpreter should read an input file (input.txt) which contains lines of expressions, evaluate them and push the results onto a stack, then print the content of the stack to an output file (output.txt) when exit.  
    It must handle the following expressions:
    1) push <num>, 2) pop, 3) boolean, 4) error, 5) add, 6) sub, 7) mul, 8) div, 9) rem, 10) neg, 11) swap, 12) quit
    Example: 
___________________________________________________________
    input.txt: push 1   ------->   output.txt: 1
    		   quit
___________________________________________________________
    input.txt: push 5   ------->   output.txt: 30
    		   neg		               -5
    		   push 10
    		   push 20
    		   add
    		   quit
___________________________________________________________
    input.txt: push 6   ------->   output.txt: :error:
    		   push 2		       3
    		   div
    		   mul
    		   quit
___________________________________________________________
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

public class hw2{
	public static void hw2(String inFile, String outFile) throws IOException {
		//File streams to read input and write to output
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		PrintWriter out = new PrintWriter(new FileWriter(outFile));

		try{
			String line;
			Stack<String> stack = new Stack<String>();
			while ((line = in.readLine()) != null){
				int x = 0;
				int y = 0;
				int r = 0;
				String result = "";
/*----------------------------PUSH-----------------------------*/
				if(line.contains("push")){
					result = line.replaceAll("[a-zA-Z]+ ","");
           		    // if (result.contains(".")){
                 //    	stack.push(":error:");
           		    // }
           		    // else if (result.contains()){
           		    // 	stack.push(":error:");
           		    // }
           		    if(isInteger(result)){
           		    	if(result.contains("-0")){
           		    		result = result.replaceAll("-0", "0");
           		    	}
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
					if(stack.size() > 1){
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(isInteger(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x + y;
								result = Integer.toString(r);
								stack.push(result);
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
/*----------------------------SUB-----------------------------*/
				else if(line.contains("sub")){
					if(stack.size() > 1){
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(isInteger(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = y - x;
								result = Integer.toString(r);
								stack.push(result);
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
/*----------------------------MUL-----------------------------*/
				else if(line.contains("mul")){
					if(stack.size() > 1){
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(isInteger(stack.peek())){
								y = Integer.parseInt(stack.pop());
								r = x*y;
								result = Integer.toString(r);
								stack.push(result);
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
/*----------------------------DIV-----------------------------*/
				else if(line.contains("div")){
					if(stack.size() > 1){
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(isInteger(stack.peek())){
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
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
							if(isInteger(stack.peek())){
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
/*----------------------------NEG-----------------------------*/
				else if(line.contains("neg")){
					if(stack.size() >= 1){
						if(isInteger(stack.peek())){
							x = Integer.parseInt(stack.pop());
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
/*----------------------------QUIT-----------------------------*/
				else if(line.contains("quit")){
					//while(!stack.isEmpty()){
					Iterator<String> iter = stack.iterator();
					while(iter.hasNext()){
						out.print(stack.pop() + "\n");
					}
					//}
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
	public static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
		} catch(NumberFormatException e){
			return false;
		} catch(NullPointerException e){
			return false;
		}
		return true;
	}


	//Helper method that checks if the string contains any digits or integer numbers (non-negative and negatives)
	/*public static boolean hasNumbers(String s){
		for(int i=0; i<s.length(); i++){
			if(!Character.isDigit(s.charAt(i))){
				if(s.charAt(i) == '-'){
				return true;
				}
				return false;
			}
		}
		return true;
	}*/


	//Function call to hw2 to run the program
	public static void main(String [] args) throws IOException{
		try{
			new hw2().hw2("2pushtest_input.txt", "2pushtest_output.txt");
		}catch(IOException e){
			e.printStackTrace();
			System.err.println("Unable to read line from input file.");
		}
	}
}











