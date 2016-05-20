''' @author Winnie Liang
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
               neg                             -5
               push 10
               push 20
               add
               quit
___________________________________________________________
    input.txt: push 6   ------->   output.txt: :error:
               push 2                          3
               div
               mul
               quit
___________________________________________________________
'''
import re


def hw2(input, output):
    try:
        input = open(input, 'r')
        output = open(output, 'w')
        stack = [] #implementing list data structure for our stack 
        for line in input:
#'''-----------------------PUSH------------------------''' 
            if "push" in line:
                number = re.findall("[-+]?\d+[\.]?\d*", line)
                if "." in number[0]:
                    stack.append(":error:")
                else:
                    stack.append(int(number[0]))
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
                    if(isinstance(stack[-1], int) and (isinstance(stack[-2], int))):
                        x = int(stack.pop())
                        y = int(stack.pop())
                        stack.append(x+y)
                    else:
                        stack.append(":error:")
#'''-----------------------SUB------------------------''' 
            elif "sub" in line: 
                if len(stack) <= 1:
                    stack.append(":error:")
                else:
                    if(isinstance(stack[-1], int) and (isinstance(stack[-2], int))):
                        x = int(stack.pop())
                        y = int(stack.pop())
                        stack.append(y-x)
                    else:
                        stack.append(":error:")
#'''-----------------------MUL------------------------''' 
            elif "mul" in line:
                if len(stack) <= 1:
                    stack.append(":error:")
                else:
                    if(isinstance(stack[-1], int) and (isinstance(stack[-2], int))):
                        x = int(stack.pop())
                        y = int(stack.pop())
                        stack.append(x*y)
                    else:
                        stack.append(":error:")
#'''-----------------------DIV------------------------''' 
            elif "div" in line:
                if len(stack) <= 1:
                    stack.append(":error:")
                else:
                    if(isinstance(stack[-1], int) and (isinstance(stack[-2], int))):
                        x = int(stack.pop())
                        y = int(stack.pop())
                        if x > 0:
                            stack.append(y/x)
                        else:
                            stack.append(y)
                            stack.append(x)
                            stack.append(":error:")
                    else:
                        stack.append(":error:")
#'''-----------------------REM------------------------''' 
            elif "rem" in line:
                if len(stack) <= 1:
                    stack.append(":error:")
                else:
                    if(isinstance(stack[-1], int) and (isinstance(stack[-2], int))):
                        x = int(stack.pop())
                        y = int(stack.pop())
                        if(x > 0):
                            stack.append(y%x)
                        else:
                            stack.append(y)
                            stack.append(x)
                            stack.append(":error:")
                    else:
                        stack.append(":error:")
#'''-----------------------NEG------------------------'''                        
            elif "neg" in line:
                if len(stack) <= 0:
                    stack.append(":error:")
                else:
                    if(isinstance(stack[-1], int)):
                        x = int(stack.pop())
                        stack.append(-1 * x)
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
#'''-----------------------QUIT------------------------''' 
            elif "quit" in line:
                stack.reverse()
                for i in stack:
                    output.write(str(i)+ "\n")
                    #print i
            else:
                print("Input file contains an invalid command.")
        input.close();
        output.close();
    except IOError:
        print("Error: Unable to find file")




#Function call to hw2
hw2("2pushtest_input.txt", "2pushtest_output.txt")


