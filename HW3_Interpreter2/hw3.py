''' @author Winnie Liang
	@title CSE 305 Assignment 3
	@purpose To implement an interpreter using a stack. The interpreter should read an input file (input.txt) which contains lines of expressions, evaluate them and push the results onto a stack, then print the content of the stack to an output file (output.txt) when exit.  
	It must handle the following expressions:
	1) push, 2) pop, 3) boolean, 4) error, 5) add, 6) sub, 7) mul, 8) div, 9) rem,
	10) neg, 11) swap, 12) quit

	New functions to implement for Interpreter II:
    13) and, 14) or, 15) not 16) equal 17) lessThan 18) bind 19) if 20) let 21) end

'''
import re


def hw2(input, output):
	try:
		input = open(input, 'r')
		output = open(output, 'w')
		stack = [] #implementing list data structure for our stack 
		dict = {}
		environmentList = []
		for line in input:
#'''-----------------------PUSH------------------------''' 
			if "push" in line:
				# number = re.findall("[-+]?\d+[\.]?\d*", line)
				# if "." in number[0]:
				# 	stack.append(":error:")
				# else:
				# 	stack.append(int(number[0]))
				line = line[5:-1]
				if (is_Integer(line)):
					stack.append(line)
				elif "." in line:
					stack.append(":error:")
				elif "-0" in line:
					stack.append("0")
				elif "\"" in line:
					stack.append(line)
				else:
					stack.append(line)	
			  

#'''-----------------------POP------------------------''' 
			elif "pop" in line:
				if len(stack) < 1:
					stack.append(":error:")
				else:
					stack.pop()
#'''---------------------:TRUE:------------------------''' 
			elif ":true:" in line:
				stack.append(":true:")
#'''---------------------:FALSE:------------------------''' 
			elif ":false:" in line:
				stack.append(":false:")
#'''---------------------:ERROR:------------------------''' 
			elif ":error:" in line:
				stack.append(":error:")
#'''-----------------------ADD------------------------''' 
			elif "add" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(x+y))
						elif(is_Name(stack[-1])):
							popped = stack.pop()
							y = dict.get(popped)
							stack.append(int(x+y))
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						popped = stack.pop()
						x = dict.get(popped)
						if(is_Name(stack[-1])):
							popped2 = stack.pop()
							y = dict.get(popped2)
							stack.append(int(x+y))
						elif(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(x+y))
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------SUB------------------------''' 
			elif "sub" in line: 
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y-x))
						elif(is_Name(stack[-1])):
							popped = stack.pop()
							y = dict.get(popped)
							stack.append(int(y-x))
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						popped = stack.pop()
						x = dict.get(popped)
						if(is_Name(stack[-1])):
							popped2 = stack.pop()
							y = dict.get(popped2)
							stack.append(int(y-x))
						elif(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y-x))
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------MUL------------------------''' 
			elif "mul" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(x*y))
						elif(is_Name(stack[-1])):
							popped = stack.pop()
							y = dict.get(popped)
							stack.append(int(x*y))
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						popped = stack.pop()
						x = dict.get(popped)
						if(is_Name(stack[-1])):
							popped2 = stack.pop()
							y = dict.get(popped2)
							stack.append(int(x*y))
						elif(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(x*y))
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------DIV------------------------''' 
			elif "div" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y/x))
						elif(is_Name(stack[-1])):
							popped = stack.pop()
							y = dict.get(popped)
							stack.append(int(y/x))
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						popped = stack.pop()
						x = dict.get(popped)
						if(is_Name(stack[-1])):
							popped2 = stack.pop()
							y = dict.get(popped2)
							stack.append(int(y/x))
						elif(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y/x))
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------REM------------------------''' 
			elif "rem" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y%x))
						elif(is_Name(stack[-1])):
							popped = stack.pop()
							y = dict.get(popped)
							stack.append(int(y%x))
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						popped = stack.pop()
						x = dict.get(popped)
						if(is_Name(stack[-1])):
							popped2 = stack.pop()
							y = dict.get(popped2)
							stack.append(int(y%x))
						elif(is_Integer(stack[-1])):
							y = int(stack.pop())
							stack.append(int(y%x))
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------NEG------------------------'''                        
			elif "neg" in line:
				if len(stack) <= 0:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						stack.append(-1 * x)
					elif(is_Name(stack[-1])):
						name = stack.pop()
						x = dict.get(name)
						r = -1 * x
						stack.append(int(r))
					else:
						stack.append(":error:")
#'''-----------------------SWAP------------------------''' 
			elif "swap" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					x = stack.pop()
					y = stack.pop()
					stack.append(x)
					stack.append(y)
#'''-----------------------AND------------------------''' 
			elif "and" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if((stack[-1] == ":true:") or (stack[-1] == ":false:")):
						x = stack.pop()
						if((stack[-1] == ":true:") or (stack[-1] == ":false:")):
							y = stack.pop()
							if(x == ":true:" and y == ":true:"):
								stack.append(":true:")
							else:
								stack.append(":false:")
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")
#'''-----------------------OR------------------------''' 
			elif "or" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if((stack[-1] == ":true:") or (stack[-1] == ":false:")):
						x = stack.pop()
						if((stack[-1] == ":true:") or (stack[-1] == ":false:")):
							y = stack.pop()
							if(x == ":false:" and y == ":false:"):
								stack.append(":false:")
							else:
								stack.append(":true:")
						else:
							stack.append(x)
							stack.append(":error:")
					else:
						stack.append(":error:")            	
#'''-----------------------NOT------------------------''' 
			elif "not" in line:
				if len(stack) < 1:
					stack.append(":error:")
				else:
					if((stack[-1] == ":true:") or (stack[-1] == ":false:")):
						x = stack.pop()
						if(x == ":true:"):
							stack.append(":false:")
						else:
							stack.append(":true:")
					else:
						stack.append(":error:")
#'''-----------------------EQUAL------------------------''' 
			elif "equal" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1]) and (is_Integer(stack[-2]))):
						x = int(stack.pop())
						y = int(stack.pop())       
						if(x == y):
							stack.append(":true:")
						else:
							stack.append(":false:")        
					else:
						stack.append(":error:")


#'''--------------------LESSTHAN------------------------''' 
			elif "lessThan" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1]) and (is_Integer(stack[-2]))):
						x = int(stack.pop())
						y = int(stack.pop())
						if(y < x):
							stack.append(":true:")
						else:
							stack.append(":false:")
					else:
						stack.append(":error:")


#'''-----------------------BIND------------------------''' 
			elif "bind" in line:
				if len(stack) <= 1:
					stack.append(":error:")
				else:
					if(is_Integer(stack[-1])):
						x = int(stack.pop())
						if(is_Name(stack[-1])):
							y = stack.pop()
							dict[y] = x
							stack.append(":unit:")
						else:
							stack.append(x)
							stack.append(":error:")
					elif(stack[-1] == ":unit:"):
						x = stack.pop()
						if(is_Name(stack[-1])):
							y = stack.pop()
							stack.append(":unit:")
						else:
							stack.append(x)
							stack.append(":error:")
					elif(is_Name(stack[-1])):
						x = stack.pop()
						newvalue = dict.get(x)
						if(is_Name(stack[-1])):
							y = stack.pop()
							dict[y] = newvalue
							stack.append(":unit:")
						else:
							stack.append(x)
							stack.append(":error:")
					elif(stack[-1] == ":error:"):
						stack.pop()
						stack.pop()
						stack.append(":error:")
					else:
						stack.append(":error:")

#'''-----------------------IF------------------------''' 
			elif "if" in line:
				if len(stack) <= 2:
					stack.append(":error:")
				else:
					x = stack.pop()
					y = stack.pop()
					z = stack.pop()
					if(z == ":true:"):
						stack.append(x)
					elif(z == ":false:"):
						stack.append(y)
					else:
						stack.append(z)
						stack.append(y)
						stack.append(x)
						stack.append(":error:")

#'''-----------------------LET------------------------''' 
			elif "let" in line:
				environmentList.append(dict)

#'''-----------------------END------------------------''' 
			elif "end" in line:
				environmentList.pop()

# #'''-----------------------QUIT------------------------''' 
			elif "quit" in line:
				stack.reverse()
				for i in stack:
					i = str(i).replace("\"", "")
					output.write(str(i) + "\n")
					#print (i)
			else:
				print("Input file contains an invalid command.")
		input.close();
		output.close();
	except IOError:
		print("Error: Unable to find file")



def is_Integer(s):
	try:
		int(s)
		return True
	except ValueError:
		return False

def is_String_Literal(s):
	if "\"" not in s:
		return False;
	return True;

def is_Name(s):
	return s[0].isalpha()

		


#Function call to hw3
#hw3("input_1", "testoutput.txt")




