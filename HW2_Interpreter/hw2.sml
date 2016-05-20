(*  @author Winnie Liang
    @title CSE 305 Assignment 2
    @purpose To implement an interpreter using a stack. The interpreter should read an input file (input.txt) which contains lines of expressions, evaluate them and push the results onto a stack, then print the content of the stack to an output file (output.txt) when exit.  
    It must handle the following expressions:
    1) push, 2) pop, 3) boolean, 4) error, 5) add, 6) sub, 7) mul, 8) div, 9) rem,
    10) neg, 11) swap, 12) quit
    Example: 
___________________________________________________________
    input.txt: push 1   ------->   output.txt: 1
    		   quit
___________________________________________________________
    input.txt: push 5   ------->   output.txt: 30
    		   neg			       -5
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
*)


fun isInteger line = 
case Int.scan StringCvt.DEC Substring.getc (Substring.full line) of
    SOME(_,subs) => Substring.isEmpty subs
|   NONE         => false


(*Helper method for the push command. Takes in the line and parse it for its integer number and pushes it to current list. ALSO checks if it is an integer number*)
fun push (s : string, mystack) = 
let 
    val num  = String.substring(s, 5, ((String.size s) - 5)); (*Parses off "push " so we only get the substring of the integer number*)
in
    if isInteger(num) then
        mystack = [num]@mystack
    else
        mystack = [":error:"]@mystack
end

fun fpush x l = x::l;

(*
fun pop nil = NONE
| pop(e::S) = SOME(S, e)
*)

fun pop l = 
    case l of [] => raise Empty
|   (x::xs)=>xs

(*Helper method for the peek command for the list*)
fun peek s =
  case !s of
      [] => raise Empty
  | x :: xs => x
;


(*Helper method for addition. Checks the 2 elements at the head of the list and return the sum*)
fun add (0, 0) = 0
|   add (a, 0) = a
|   add (0, b) = b
|   add (a, b) = 
        if(b > 0) then
            add(a, b-1) + 1
        else add(a, b+1) - 1


(*Helper method for subtraction. Checks the 2 elements at the head of the list and return the subtracted*)
fun sub (0, 0) = 0
|   sub (a, 0) = a
|   sub (0, b) = ~b
|   sub (a, b) = 
        if(b > 0) then
            sub(a, b-1) - 1
        else sub(a, b+1) + 1


(*Helper method for multiplication. This recursive call calculates the multiplication of two integers through repeated addition. Here negative is ~ . ex. -5 is either 0-5 or ~5 *)
fun mul (a, 0) = 0
|   mul (0, b) = 0
|   mul (a, 1) = a
|   mul (1, b) = b
|   mul (a, ~1) = ~a
|   mul (~1, b) = ~b
(*|   mul (a, b) = b + mul(a-1, b);*)
|   mul (a, b) = 
        if(a > 0 andalso b > 0) then
            add(add(0,b), mul(a, b-1))
        else if (a < 0 andalso b < 0) then
            mul(~a, ~b)
        else if (a < 0 andalso b > 0) then
            a - mul(~a, b-1)
        else
            b - mul(a-1, ~b)

(*Helper method for division. This recursive call calculates the division of two integers*)
fun divi (0, _) = 0
|   divi (a, b) = 1 + divi(a-b, b)
        
(*
(*Helper method to swap two elements of a list*)
fun swap a =
   let
      val first = #1 a;
      val second = #2 a;
   in
      val b = (second, first);
   end
;*)

fun update x s =
  case !s of
      [] => raise Empty
    | _ :: xs => (s := x :: xs)
;

fun null s = List.null (!s);



(*
fun process ([], stack) = 0
|   process ("quit"::lines, i::stack) = i
|   process ("add"::lines, i::j::stack) = process(lines,(i+j)::stack)
|   process ("sub"::lines, i::j::stack) = process(lines,(i-j)::stack)
|   process ("mul"::lines, i::j::stack) = process(lines,(i*j)::stack)
|   process ("div"::lines, i::j::stack) = process(lines,(i div j)::stack)
        (*if(i > 0 andalso j>0) then 
                    process(lines,(i div j)::stack) 
                    else
                    process(lines,(":"))*)
|   process ("rem"::lines, i::j::stack) = process(lines,(i mod j)::stack) 
|   process (s::lines, stack) = 
        let 
            val d = String.sub(s,5)
            val i = Char.ord d - Char.ord(#"0")
        in
            process(lines,i::stack)
        end;
*)




(*This helper method reads in the commands from the input file*)
fun parse (line : string, ref stack) 
let 
    val a = "";
    val b = "";
    val x = 0;
    val y = 0;
    val r = 0; 

in 
    
    if line = "push" then 
        push(line, stack)
        
    else if line = "pop" then
        if (length(stack) < 1) then
            stack = [":error:"]@stack         
        else 
            pop(stack)  
    
    else if line == ":true:" then
        stack := [":true:"]@stack

    else if line == ":false:" then
        stack := [":false:"]@stack

    else if line == ":error:"  then
        stack := [":error:"]@stack


    else if line == "add" then
        if (length(stack) > 1) then 
            (let
                val a = pop(stack)
                val b = pop(stack)
            in
                push(add(a,b), stack)
            end)
        else
            stack := [":error:"]@stack

    else if line == "sub" then
        if (length(stack) > 1) then 
            (let
                val a = pop(stack)
                val b = pop(stack)
            in
                push(sub(a,b), stack)
            end)
        else
            stack := [":error:"]@stack

    else if line == "mul" then
        if (length(stack) > 1) then
            (let
                val a = pop(stack);
                val b = pop(stack)
            in    
                push(mul(a, b), stack)
            end)
        else 
            stack := [":error:"]@stack

    else if line == "div" then
        if (length(stack) > 1) then
            (let
                val a = pop(stack);
                val b = pop(stack)
            in    
                if(a < 0 andalso b <= 0) then
                    push(b, stack)
                    push(a, stack)
                    stack := [":error:"]@stack
                else 
                    push(div(a, b), stack)
            end)
        else 
            stack := [":error:"]@stack
       

    else if line == "rem" then
        if (length(stack) > 1) then
            (let
                val a = pop(stack);
                val b = pop(stack)
            in    
                if(a < 0 andalso b <= 0) then
                    push(b, stack)
                    push(a, stack)
                     stack := [":error:"]@stack
                else 
                    push(rem(a, b), stack)
            end)
        else 
            stack := [":error:"]@stack

    else if line == "neg" then
        if (length(stack) >= 1) then
            (let
                val a = pop(stack);
            in    
                push(neg(a), stack)
            end)
        else 
            stack := [":error:"]@stack

    else if line == "swap" then
        if (length(stack) > 1) then
            (let
                val a = pop(stack);
                val b = pop(stack)
            in    
                push(a, stack)
                push(b, stack)
            end)
        else 
            stack := [":error:"]@stack

    else if line == "quit" then
        (continue)

    else
        print ("File input has unrecognizable commands.")

    
end    

(*Filestreams to read input and write to output*)
fun hw2(inFile : string, outFile : string) =
let
    (*Open input and output file streams*)
    val ins = TextIO.openIn inFile
    val outs = TextIO.openOut outFile
    val readLine = TextIO.inputLine ins
    val stack: string list ref = ref [] (* Implementing a stack using a list data structure using a reference*)
    (*Read from the input file stream *)
    fun readInput (readLine : string option) = 
        case readLine of
        NONE => (TextIO.closeIn ins; TextIO.closeOut outs)
            | SOME(c) =>
	(*Call to helper function called parse that takes in each line read from input file*)
        (parse(c, stack)
        readInput(TextIO.inputLine ins))

in
    readInput(readLine)
end



(*Function call towards hw2 to test code*)
(*val _ = hw2("sample_input1.txt", "sample_input1.txt")*)


