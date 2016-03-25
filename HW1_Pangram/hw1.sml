(*
 * @author Winnie Liang
 * @title CSE 305 Assignment 1 
 * @purpose A function that accepts two strings (the names of input and output files) as arguments. 
 * Reads the input file line-by-line and checks if the line is a pangram. Lastly, the program outputs
 * the boolean value in the provided output file. 
 * A pangram is a sentence that contains all the letters of the English alphabet at least once. 
 * Example: "The quick brown fox jumps over the lazy dog" - True 
 * 
 *)

(*This match pattern function checks boolean value if a character is an item of the list*)
(*x is a character and b::y is a list*)
fun member(x,[]) = false
|   member(x, b::y) =
        if x=b then true
        else member(x,y);

(*This match pattern function checks boolean value if a list is a subset*)
(*a::x is a list and y is a list as well*)
fun subset([],y) = true
|   subset(a::x, y) =
        if member(a,y) then subset(x,y)
        else false;

(*Helper function to return boolean value of a string for pangram check*)
fun isPangram (line : string) =
let
	(*Convert string type of the lines and alphabet string to list type*)
	val alphabet = explode "abcdefghijklmnopqrstuvwxyz"
	val chlist = explode(line) 
in
	if (length(chlist) < 26) then false
	else subset(alphabet, chlist) (*return boolean value if alphabet is a subset of ch (characters of each line)*)	
end



(*Filestreams to read input and write to output*)
fun hw1(inFile : string, outFile : string) =
let
	(*Open input and output file streams*)
	val ins = TextIO.openIn inFile
	val outs = TextIO.openOut outFile
	val readLine = TextIO.inputLine ins
	(*Read from the input file stream*)
	fun readInput (readLine : string option) = 
		case readLine of
			NONE => (TextIO.closeIn ins; TextIO.closeOut outs)
	         	| SOME(c) =>
			(if (isPangram(c) = false) then	
			(*Returns the boolean values to the output file*) 
				TextIO.output(outs, "false" ^ "\n")
			else
			(*Returns the boolean values to the output file*) 
				TextIO.output(outs, "true" ^ "\n");
			readInput(TextIO.inputLine ins))
			
in
	readInput(readLine)
end
	
						
(*Function call to test code*)
 val _ = hw1("sample_input_2.txt", "sample_output_2.txt")  
