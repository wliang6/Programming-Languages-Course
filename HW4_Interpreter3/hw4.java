import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class hw4 {
	
	public static void hw4(String input, String output) throws IOException{
		//File streams to read input and write to output
		PrintStream myconsole = new PrintStream(new File(output));
		System.setOut(myconsole);
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		ArrayList stack = new ArrayList();
		HashMap hm = new HashMap();
		HashMap closures = new HashMap();
		HashMap inOutClosures = new HashMap();
		String line = in.readLine();
		while(line!=null){
			if(line.equals("let")){
				stack.add(0,parseLet(in,hm,myconsole,closures,inOutClosures));
			}
			else if(Character.isLetter(line.charAt(0))){
				stack = parsePrimitive(line, stack, hm, myconsole, closures, inOutClosures, in);
			}
			else if(line.charAt(0)==':'){
				stack = parseBooleanOrError(line, stack, hm);
			}
			else{
				myconsole.println("There exists an error.");
			}
			line = in.readLine();
		}
		in.close();
	}
	
	
	public static Object parseLet(BufferedReader in, HashMap hm_parent, PrintStream myconsole, HashMap closures, HashMap inOutClosures) throws IOException{
		ArrayList stack_let = new ArrayList();
		HashMap hm_let = new HashMap(hm_parent);
		HashMap closures_let = new HashMap(closures);
		HashMap inOutClosures_let = new HashMap(inOutClosures);
		String line = in.readLine();
		while(!line.equals("end")){
			if(line.equals("let")){
				stack_let.add(0,parseLet(in,hm_let,myconsole,closures_let,inOutClosures_let));
			}
			else if(Character.isLetter(line.charAt(0))){
				stack_let = parsePrimitive(line, stack_let,hm_let, myconsole, closures_let, inOutClosures_let, in);
			}
			else if(line.charAt(0)==':'){
				stack_let = parseBooleanOrError(line,stack_let,hm_let);
			}
			else{
				myconsole.println("There exists an error.");
			}
			line = in.readLine();
		}
		return stack_let.get(0);
	}
	
	public static Object parseLetII(ArrayList code, HashMap hm_parent,HashMap closures, HashMap inOutClosures, BufferedReader in, PrintStream myconsole, int space) throws IOException{
		ArrayList stack_let = new ArrayList();
		HashMap hm_let = new HashMap(hm_parent);
		HashMap closures_let = new HashMap(closures);
		HashMap inOutClosures_let = new HashMap(inOutClosures);
		for(int i = space+1; i<code.size(); i++){
			String line = (String) code.get(i);
				if(line.equals("end")){
					break;
				}
				else if(line.equals("let")){
					stack_let.add(0,parseLetII(code,hm_let,closures_let,inOutClosures_let,in,myconsole,i));
				}
				else if(Character.isLetter(line.charAt(0))){
					stack_let = parsePrimitive(line,stack_let,hm_let,myconsole,closures_let,inOutClosures_let,in);
				}
				else if(line.charAt(0)==':'){
					stack_let = parseBooleanOrError(line,stack_let,hm_let);
				}
				else{
					myconsole.println("There exists an error.");
				}
		}
		return stack_let.get(0);
	}	




	public static ArrayList parseBooleanOrError(String line, ArrayList stack, HashMap hm){
		if(line.startsWith(":e")){
			stack.add(0,":error:");
		}
		else if(line.startsWith(":t")){
			stack.add(0,":true:");
		}
		else if(line.startsWith(":f")){
			stack.add(0,":false:");
		}
		
		return stack;
	}
	
	public static ArrayList parsePrimitive(String line, ArrayList stack, HashMap hm, PrintStream myconsole, HashMap closures, HashMap inOutClosures, BufferedReader in) throws IOException{
		if (line.startsWith("add")){
			stack = doAdd(stack,hm);
		}
		else if (line.startsWith("sub")){
			stack = doSub(stack,hm);
		}
		else if (line.startsWith("mul")){
			stack = doMul(stack,hm);
		}
		else if (line.startsWith("div")){
			stack = doDiv(stack,hm);
		}
		else if (line.startsWith("rem")){
			stack = doRem(stack, hm);
		}
		else if (line.startsWith("pop")){
			stack = doPop(stack);
		}
		else if (line.startsWith("push")){
			stack = doPush(stack, line);
		}
		else if (line.startsWith("swap")){
			stack = doSwap(stack);
		}
		else if (line.startsWith("neg")){
			stack = doNeg(stack,hm);
		}
		else if (line.startsWith("quit")){
			doQuit(stack, myconsole);
		}
		else if (line.startsWith("if")) {
			doIf(stack,hm);
		}
		else if (line.startsWith("not")) {
			doNot(stack,hm);
		}
		else if (line.startsWith("and")) {
			doAnd(stack,hm);
		}
		else if (line.startsWith("or")) {
			doOr(stack,hm);
		}			
		else if (line.startsWith("equal")) {
			doEqual(stack,hm);
		}
		else if (line.startsWith("lessThan")) {
			doLessThan(stack,hm);
		}
		else if (line.startsWith("bind")) {
			doBind(stack,hm);
		}
		else if(line.startsWith("fun") && line != "funEnd"){
			doFun(stack, closures, hm, line, in);
		}
		else if(line.startsWith("inOutFun")){
			doInOut(stack, hm, inOutClosures, line, in);
		}
		else if(line.startsWith("call")){
			doCall(stack, hm, closures, inOutClosures, in, myconsole);
		}
		return stack;
	}
	

	
	public static ArrayList doAdd(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("(.*)[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("(.*)[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x+y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}
	
	public static ArrayList doSub(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x-y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}
	
	public static ArrayList doMul(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x*y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}
	
	public static ArrayList doDiv(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			if (y == 0){
				stack.add(0, ":error:");
			}
			else{
				stack.remove(0);
				stack.remove(0);
				Integer newTop = x/y;
				stack.add(0, newTop.toString());
			}
		}
		return stack;
	}
	
	public static ArrayList doRem(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			if (y == 0){
				stack.add(0, ":error:");
			}
			else{
				stack.remove(0);
				stack.remove(0);
				Integer newTop = x%y;
				stack.add(0, newTop.toString());
			}
		}
		return stack;
	}
	
	public static ArrayList doNeg(ArrayList stack, HashMap hm) {
		if (stack.isEmpty()){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
            int x;
            String s0 = (String) stack.get(0);
            if(s0.matches("(.*)[0-9]+")) x = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
                    stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s0_1);
            }
			Integer newTop = -1*x;
			stack.remove(0);
			stack.add(0, newTop.toString());
        }
		return stack;
	}
	
	public static void doQuit(ArrayList stack, PrintStream myconsole) {
		for (int i = 0; i < stack.size(); i++){
			String s = (String) stack.get(i);
			myconsole.println(s.replace("\"",""));
		}
		myconsole.close();
	}
	
	public static ArrayList doIf(ArrayList stack, HashMap hm) {
		if (stack.size()<3){
			stack.add(0, ":error:");
		}
		else {
            String s2 = (String) stack.get(2);
			if(!s2.equals(":true:") && !s2.equals(":false:")) s2 = (String) hm.get(s2);
            if (s2 != null && s2.equals(":true:")) {
                stack.remove(2);
                stack.remove(1);
				
            }
            else if (s2 != null && s2.equals(":false:")) {
                stack.remove(2);
                stack.remove(0);
				
            }
            else{
                stack.add(0, ":error:");
            }
        }
		return stack;
	}
	
	public static ArrayList doNot(ArrayList stack, HashMap hm) {
		if (stack.size()<1){
			stack.add(0, ":error:");
		}
        else {
            String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
            if(s0 == null){
                stack.add(0, ":error:");
            }
            else if (s0.equals(":true:")) {
                stack.remove(0);
                stack.add(0, ":false:");
            }
            else if (s0.equals(":false:")) {
                stack.remove(0);
                stack.add(0, ":true:");
            }
            else{
                stack.add(0, ":error:");
            }
        }
		return stack;
	}
	
	public static ArrayList doAnd(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
			String s1 = (String) stack.get(1);
            if(!s1.equals(":true:") && !s1.equals(":false:")) s1 = (String) hm.get(s1);
            if(s0 == null || s1 == null) {
                stack.add(0, ":error:");
            }
			else if (s0.equals(":false:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":false:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":true:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":true:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else{
				stack.add(0, ":error:");
			}
		}
		return stack;
	}
	
	public static ArrayList doOr(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
			String s1 = (String) stack.get(1);
            if(!s1.equals(":true:") && !s1.equals(":false:")) s1 = (String) hm.get(s1);
            if(s0 == null || s1 == null) {
                stack.add(0, ":error:");
            }
			else if (s0.equals(":false:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":false:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else if (s0.equals(":true:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else if (s0.equals(":true:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else{
				stack.add(0, ":error:");
			}
		}
		return stack;
	}
	
	public static ArrayList doEqual(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
            if(x == y) stack.add(0, ":true:");
            else stack.add(0, ":false:");
		}
		return stack;
	}
	
	public static ArrayList doLessThan(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
            if(x < y) stack.add(0, ":true:");
            else stack.add(0, ":false:");
		}
		return stack;
	}
	
	private static ArrayList doSwap(ArrayList stack) {
		if (stack.size() < 2){
			stack.add(0, ":error:");
		}
		else{
			String x = (String) stack.get(1);
			String y = (String) stack.get(0);
			stack.remove(0);
			stack.remove(0);
			stack.add(0, y);
			stack.add(0, x);
		}
		return stack;
	}
	
	public static ArrayList doPush(ArrayList stack, String line) {

		String getNum = line.substring(5);
		
		if (getNum.charAt(0) == '-'){
			if (getNum.substring(1).equals("0")){
				stack.add("0");
			}
			else if (getNum.substring(1).matches("[0-9]+")){
				stack.add(0, getNum);
			}
			else{
				stack.add(0, ":error:");
			}
		}
		else if(getNum.equals(":true:")){
			stack.add(0,":true:");
		}
		else if(getNum.equals(":false:")){
			stack.add(0,":false:");
		}
		else if (getNum.matches("[0-9]+")){
			stack.add(0, getNum);
		}
		else if (getNum.matches("^[a-zA-Z].*")){
			stack.add(0, getNum);
		}
		else if (getNum.matches("^\".+\"$")){
			stack.add(0, getNum);
		}
		else{
			stack.add(0, ":error:");
		}
		return stack;
	}
	
	public static ArrayList doPop(ArrayList stack) {
		if (stack.size() < 1){
			stack.add(0, ":error:");
		}
		else{
			stack.remove(0);
		}
		return stack;
	}
	
	public static ArrayList doBind(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
			String s1 = (String) stack.get(1);
			if(s1.matches("^[a-zA-Z].*") && !s0.equals(":error:")) {
				stack.remove(0);
				stack.remove(0);
				Object o = hm.get(s0);
				if(o!=null) hm.put(s1,o);
				else hm.put(s1,s0);
				stack.add(0, ":unit:");
			}
			else {
				stack.add(0, ":error:");
			}
		}
		return stack;
	}


	public static ArrayList doFun(ArrayList stack, HashMap closures, HashMap hm, String input, BufferedReader in) throws IOException{
		String [] breakdown = input.split(" ");
		ArrayList closure = new ArrayList();
		ArrayList code = new ArrayList();
		
		String funName = breakdown[1]; //funName
		String argv = breakdown[2]; //arg 

		String line = in.readLine();
		while(!line.equals("funEnd")){ //code
			code.add(line);
			line = in.readLine();
		}

		HashMap env = new HashMap(); //env --- set a duplicate hm to be the env 
		Iterator itr = hm.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry pair = (Map.Entry)itr.next();
			env.put(pair.getKey(), pair.getValue());
		}
	
		closure.add(0, code);
		closure.add(0, env);
		closure.add(0, argv);
		
		closures.put(funName, closure);
		
		stack.add(0, ":unit:");
		return stack;
	}



	public static ArrayList doInOut(ArrayList stack, HashMap hm, HashMap inOutClosures, String lineInput, BufferedReader in) throws IOException{
		String [] breakdown = lineInput.split(" ");
		ArrayList closure = new ArrayList();
		ArrayList code = new ArrayList();
		String funName = breakdown[1];  //funName
		String argv = breakdown[2]; //arg 
		
		String line = in.readLine(); // code
		while(!line.equals("funEnd")){
			code.add(line);
			line = in.readLine();
		}

		HashMap env = new HashMap(); //env --- set a duplicate hm to be the env 
		Iterator itr = hm.entrySet().iterator(); 
		while(itr.hasNext()){
			Map.Entry pair = (Map.Entry)itr.next();
			env.put(pair.getKey(), pair.getValue());
		}
		
		closure.add(0, code);
		closure.add(0, env);
		closure.add(0, argv);
		
		inOutClosures.put(funName, closure);
		
		stack.add(0, ":unit:");
		
		return stack;
	}



	public static ArrayList doCall(ArrayList stack, HashMap hm, HashMap closures, HashMap inOutClosures, BufferedReader in, PrintStream myconsole) throws IOException{
		if(stack.size() < 2){
			stack.add(0,":error:");
		}
		else{
			ArrayList temp = new ArrayList();
			String arg = (String) stack.get(1);
			ArrayList closure = (ArrayList) closures.get(stack.get(0));
			ArrayList inOutClosure = (ArrayList) inOutClosures.get(stack.get(0));
			//###################--------FUN..... FUNEND--------###########        
			if(closures.containsKey(stack.get(0)) && !stack.get(1).equals(":error:")){
				// closure = [argv, env, code]
				String argv = (String) closure.get(0);
				HashMap env = (HashMap) closure.get(1);
				ArrayList code = (ArrayList) closure.get(2);
				stack.remove(0); // pop the funName
				stack.remove(0); // pop the arg
				// checks to see if variable is the same as arg in fun
				if(hm.containsKey(arg)){
					env.put(arg, hm.get(arg));
					code = setVariable(code, arg, argv);
				}else{
					code = setVariable(code, arg, argv);
				}

				// runs the code between fun x y .... funEnd
				for(int i = 0;i<code.size();i++){
					String line = (String) code.get(i);
					if(line.equals("let")){
						temp.add(0,parseLetII(code,env,closures,inOutClosures,in,myconsole,i));
					}
					else if(Character.isLetter(line.charAt(0))){
						temp = parsePrimitive(line, temp,env,myconsole,closures,inOutClosures,in);
					}
					else if(line.charAt(0)==':'){
						temp = parseBooleanOrError(line, temp, env);
					}
				}
				
				// check if variable is previously binded to a value
				if(env.containsKey(temp.get(0))){
					stack.add(0, env.get(temp.get(0)));
				}else{
					stack.add(0, temp.get(0));
				}
				return stack;
			}
			

			//###################--------INOUT..... FUNEND--------###########
			else if(inOutClosures.containsKey(stack.get(0)) && !stack.get(1).equals(":error:")){
				String argv = (String) inOutClosure.get(0);
				HashMap env = (HashMap) inOutClosure.get(1);
				ArrayList code = (ArrayList) inOutClosure.get(2);
				int isReturnPresent = 0;
				stack.remove(0); // pop the funName
				stack.remove(0); // pop the arg
				// checks to see if variable is the same as arg in inOutFun
				if(hm.containsKey(arg)){
					env.put(arg, hm.get(arg));
					code = setVariable(code, arg, argv);
				}else{
					code = setVariable(code,arg,argv);
				}
				
				// runs the code between inOutFun .... funEnd
				for(int i =0; i<code.size(); i++){
					String line = (String) code.get(i);
					if(line.equals("return")){
						isReturnPresent = 1;
					}
					else if(line.equals("let")){
						temp.add(0,parseLetII(code,env,closures,inOutClosures,in,myconsole,i));
					}
					else if(Character.isLetter(line.charAt(0))){
						temp = parsePrimitive(line, temp,env,myconsole,closures,inOutClosures,in);
					}
					else if(line.charAt(0) == ':'){
						temp = parseBooleanOrError(line, temp,env);
					}
				}

				// checks to see if variable is previously binded to a value
				if(isReturnPresent == 1){
					hm.put(arg, env.get(arg));
					if(env.containsKey(temp.get(0))){
						stack.add(0,env.get(temp.get(0)));
					}else{
						stack.add(0,temp.get(0));
					}
				}else{
					hm.put(arg, env.get(arg));
				}
				
				return stack;
			}
			else{
				stack.add(0,":error");
			}
		}
		
		return stack;
	}

	
	public static Boolean isVariableInArray(String variable, String[] array){
		for(int i = 0; i<array.length; i++){
			if(array[i].equals(variable)){
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList setVariable(ArrayList stack, String arg, String var){
		ArrayList list = new ArrayList();
		for(int i =0;i<stack.size();i++){
			String line = (String) stack.get(i);
			String[] temp = line.split(" ");
			if(isVariableInArray(var, temp)){
				list.add(temp[0] + ' ' + arg);
			}else{
				list.add(line);
			}
		}
		return list;
	}
	
	



	//Function call to hw4	
	public static void main(String[] arg){
		try{
			new hw4().hw4("testinput.txt", "testoutput.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
