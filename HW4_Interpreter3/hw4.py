#  @author Winnie Liang
    # @title CSE 305 Assignment 4
    # @purpose To implement an interpreter using a stack. The interpreter should read an input file (input.txt) which contains lines of expressions, evaluate them and push the results onto a stack, then print the content of the stack to an output file (output.txt) when exit.  
    # It must handle the following expressions:
    # 1) push <num>, 2) pop, 3) boolean, 4) error, 5) add, 6) sub, 7) mul, 8) div, 9) rem, 10) neg, 11) swap, 12) quit
   
    # New functions to implement for Interpreter II:
    # 13) and, 14) or, 15) not 16) equal 17) lessThan 18) bind 19) if 20) let 21) end

    # New functions to implement for Interpreter III:
    # 1) fun x y ... funEnd   2) inOut x y .... funEnd   3) fun x y in let..end
#

import copy
import re
fInput = 0
def hw4(input, output):
    global fInput
    fInput = open(input,'r')
    f = open(output,'w')
    stack = []
    hm = {}
    closures = {} # for regular fun functions
    inOutClosures = {} # for inOut functions
    for line in fileRead():
        if line.startswith('let'):
            stack.insert(0,parseLet(fInput,copy.deepcopy(hm),f,closures,inOutClosures))
        elif line[0].isalpha():
            parsePrimitive(line, stack, hm, f,closures,inOutClosures)
        elif line[0] == ':':
            parseBooleanOrError(line, stack, hm)
    fInput.close()

def fileRead():
    for line in fInput:
        yield line 

def parseLet(input,hm_parent,f,closures,inOutClosures):
    global fInput
    stack_let = []
    hm_let = hm_parent
    closures = {}
    inOutClosures = {}
    for line in fileRead():
        if line.startswith('end'):
            return stack_let[0]
        elif line.startswith('let'):
            stack_let.insert(0,parseLet(fInput,copy.deepcopy(hm_let),f, closures, inOutClosures))
        elif line[0].isalpha():
            parsePrimitive(line, stack_let, hm_let, f, closures, inOutClosures)
        elif line[0] == ':':
            parseBooleanOrError(line, stack_let, hm_let)

def parseLetII(code,hm_parent,f,closures,inOutClosures):
    stack_let = []
    hm_let = hm_parent
    closures = {}
    inOutClosures = {}
    for line in copy.deepcopy(code):
        if line.startswith('end'):
            return stack_let[0]
        elif line[0].startswith('let'):
            stack_let.insert(0,parseLetII(temp_code, copy.deepcopy(hm_let), f, closures, inOutClosures))
        elif line[0].isalpha():
            parsePrimitive(line, stack_let, hm_let, f, closures, inOutClosures)
        elif line[0] ==':':
            parseBooleanOrError(line, stack_let, hm_let)

def parsePrimitive(line, stack, hm, f, closures, inOutClosures):
    if line.startswith('add'):
        doAdd(stack, hm)
    elif line.startswith('sub'):
        doSub(stack, hm)
    elif line.startswith('mul'):
        doMul(stack, hm)
    elif line.startswith('div'):
        doDiv(stack, hm)
    elif line.startswith('rem'):
        doRem(stack, hm)
    elif line.startswith('pop'):
        doPop(stack)
    elif line.startswith('push'):
        doPush(stack, line)
    elif line.startswith('swap'):
        doSwap(stack)
    elif line.startswith('neg'):
        doNeg(stack, hm)
    elif line.startswith('quit'):
        doQuit(stack, f, hm)
    elif line.startswith('if'):
        doIf(stack, hm)
    elif line.startswith('not'):
        doNot(stack, hm)
    elif line.startswith('and'):
        doAnd(stack, hm)
    elif line.startswith('or'):
        doOr(stack, hm)
    elif line.startswith('equal'):
        doEqual(stack, hm)
    elif line.startswith('lessThan'):
        doLessThan(stack, hm)
    elif line.startswith('bind'):
        doBind(stack, hm)
    elif line.startswith('fun') and line != 'funEnd\n':
        doFun(stack, closures, hm, line)
    elif line.startswith('inOutFun'):
        doInOut(stack, inOutClosures, hm, line)
    elif line.startswith('call'):
        doCall(stack, hm, closures, inOutClosures, f)




def doAdd(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            stack.pop(0)
            stack.pop(0)
            newTop = x+y
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')


def doSub(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            stack.pop(0)
            stack.pop(0)
            newTop = x-y
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')



def doMul(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            stack.pop(0)
            stack.pop(0)
            newTop = x*y
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')



def doDiv(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            if y == 0:
                return stack.insert(0, ':error:')
            stack.pop(0)
            stack.pop(0)
            newTop = x//y
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')



def doRem(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            if y == 0:
                return stack.insert(0, ':error:')
            stack.pop(0)
            stack.pop(0)
            newTop = x%y
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')


def doPop(stack):
    if len(stack) < 1:
        return stack.insert(0, ':error:')
    else:
        return stack.pop(0)


def doPush(stack, line):
    closure = line.split(' ',1)
    closure[1] = closure[1].strip('\n')
    if closure[1][0] == '-':
        if closure[1][1:] == '0':
            return stack.insert(0,'0')
        elif closure[1][1:].isdigit():
            return stack.insert(0, closure[1])
        else:
            return stack.insert(0, ':error:')
    elif closure[1].isdigit():
        return stack.insert(0, closure[1])
    elif closure[1] == ':true:':
        return stack.insert(0,closure[1])
    elif closure[1] == ':false:':
        return stack.insert(0,closure[1])
    elif re.match('^[a-zA-Z].*',closure[1],0):
        return stack.insert(0, closure[1])
    elif re.match('^".+"$',closure[1],0):
        return stack.insert(0, closure[1])
    else:
        return stack.insert(0, ':error:')


def doSwap(stack):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    else:
        x = stack[1]
        y = stack[0]
        stack.pop(0)
        stack.pop(0)
        stack.insert(0, y)
        return stack.insert(0, x)



def doNeg(stack, hm):
    if len(stack) < 1:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if s0.isdigit:
            x = int(s0)
            stack.pop(0)
            newTop = -1*x
            return stack.insert(0, str(newTop))
        else:
            return stack.insert(0, ':error:')


def doQuit(stack, f, hm):
    for ele in stack:
        #ele = str(hm.get(ele,ele))
        f.write(ele.strip('"') + '\n')
        #print (ele.strip('"'))
    f.close()

def parseBooleanOrError(line, stack, hm):
    if line[1] == 'e':
        return stack.insert(0,':error:')
    elif line[1] == 't':
        return stack.insert(0,':true:')
    else:
        return stack.insert(0,':false:')

def doIf(stack, hm):
    if len(stack) < 3:
        return stack.insert(0, ':error:')
    else:
        s2 = str(stack[2])
        if (s2 != ':true:' and s2 != ':false:') or (s2 != ':true:\n' and s2 != ':false:\n'):
            s2 = str(hm.get(s2,s2))
        if s2 == ':true:' or s2 == ':true:\n':
            stack.pop(2)
            stack.pop(1)
            return stack
        elif s2 == ':false:' or ':false:\n' :
            stack.pop(2)
            stack.pop(0)
            return stack
        else:
            return stack.insert(0,':error:')

def doNot(stack, hm):
    if len(stack) < 1:
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        if s0 != ':true:' and s0 != ':false:':
            s0 = str(hm.get(s0,s0))
        if s0 == ':true:':
            stack.pop(0)
            stack.insert(0,':false:')
            return stack
        elif s0 == ':false:':
            stack.pop(0)
            stack.insert(0,':true:')
            return stack
        else:
            return stack.insert(0,':error:')
        
def doAnd(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if s0 != ':true:' and s0 != ':false:':
            s0 = str(hm.get(s0,s0))
        if s1 != ':true:' and s1 != ':false:':
            s1 = str(hm.get(s1,s1))
        if s0 == ':false:' and s1 == ':false:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':false:')
            return stack
        elif s0 == ':false:' and s1 == ':true:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':false:')
            return stack
        elif s0 == ':true:' and s1 == ':false:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':false:')
            return stack
        elif s0 == ':true:' and s1 == ':true:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':true:')
            return stack
        else:
            return stack.insert(0,':error:')
        
def doOr(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if s0 != ':true:' and s0 != ':false:':
            s0 = str(hm.get(s0,s0))
        if s1 != ':true:' and s1 != ':false:':
            s1 = str(hm.get(s1,s1))
        if s0 == ':false:' and s1 == ':false:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':false:')
            return stack
        elif s0 == ':false:' and s1 == ':true:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':true:')
            return stack
        elif s0 == ':true:' and s1 == ':false:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':true:')
            return stack
        elif s0 == ':true:' and s1 == ':true:':
            stack.pop(0)
            stack.pop(0)
            stack.insert(0,':true:')
            return stack
        else:
            return stack.insert(0,':error:')
        
def doEqual(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            stack.pop(0)
            stack.pop(0)
            if x==y:
                return stack.insert(0, ':true:')
            else:
                return stack.insert(0, ':false:')
        else:
            return stack.insert(0, ':error:')
        
def doLessThan(stack, hm):
    if len(stack) < 2:
        return stack.insert(0, ':error:')
    elif stack[0][0] == ':' or stack[1][0] == ':':
        return stack.insert(0, ':error:')
    else:
        s0 = str(stack[0])
        s1 = str(stack[1])
        if re.match('^[a-zA-Z].*',s0,0):
            s0 = str(hm.get(s0,s0))
        if re.match('^[a-zA-Z].*',s1,0):
            s1 = str(hm.get(s1,s1))
        if s0.isdigit and s1.isdigit:
            y = int(s0)
            x = int(s1)
            stack.pop(0)
            stack.pop(0)
            if x<y:
                return stack.insert(0, ':true:')
            else:
                return stack.insert(0, ':false:')
        else:
            return stack.insert(0, ':error:')
        
def doBind(stack, hm):
    if len(stack) < 2:
        return stack.insert(0,':error:')
    else:
        s0 = stack[0]
        s1 = stack[1]
        if re.match('^[a-zA-Z].*',stack[1],0) and stack[0] != ':error:':
            stack.pop(0)
            stack.pop(0)
            s0 = str(hm.get(s0,s0))
            hm[s1] = s0
            return stack.insert(0,':unit:')
        else:
            return stack.insert(0,':error:')


def doFun(stack, closures, hm, line):
    closure = []
    code = []
    breakdown = line.split()
    if breakdown[0] == 'fun': # checks if the line begins with fun in fun x y
        funName = breakdown[1]
        argv = breakdown[2]
        env = copy.deepcopy(hm)
        closure.append(argv)
        closure.append(env)
        for i in fileRead():
            if i != 'funEnd\n':
                code.append(i.strip('\n'))
            else: # fill in the code between fun x y and funEnd
                closure.append(code)
                closures[funName] = closure
                return stack.insert(0,':unit:')      
    else:
        return stack.insert(0, ':error:')
    # print (closure)
    # print (code)
        


def doInOut(stack, inOutClosures, hm, line):
    closure = []
    code = []
    breakdown = line.split()
    if breakdown[0] == 'inOutFun': # checks if the line begins with fun in fun x y
        funName = breakdown[1]
        argv = breakdown[2]
        env = copy.deepcopy(hm)
        closure.append(argv)
        closure.append(env)
        for i in fileRead():
            if i != 'funEnd\n':
                code.append(i.strip('\n'))
            else: # fill in the code between fun x y and funEnd
                closure.append(code)
                inOutClosures[funName] = closure
                return stack.insert(0,':unit:')      
    else:
        return stack.insert(0, ':error:')





def doCall(stack, hm, closures, inOutClosures, f):
    if len(stack) < 2:
        return stack.insert(0,':error:')
    else:
        temp = [] # this is our temp stack
        closure = closures.get(stack[0])
        inOutClosure = inOutClosures.get(stack[0])
###################--------FUN..... FUNEND--------###########        
        if stack[0] in closures and stack[1] != ':error:':
            argval = closure[0]
            env = closure[1]
            code = closure[2]
            arg = stack[1]
            stack.pop(0) # pop the funName
            stack.pop(0) # pop the arg
            # checks to see if variable is the same as arg in fun
            if arg in hm:
                env[arg] = hm.get(arg)
                code = setVariable(code, arg, argval) # where arg is from function and argval is part of closure
            else:
                code = setVariable(code, arg, argval)

            for line in code:
                if line.startswith('let'):
                    temp.insert(0,parseLetII(code, env, f, closures, inOutClosures))
                elif line[0].isalpha():
                    parsePrimitive(line, temp, env, f, closures, inOutClosures)
                elif line[0] == ':':
                    parseBooleanOrError(line, temp, env)

            # check if variable is previously binded
            if temp[0] in env:
                return stack.insert(0, env.get(temp[0]))
            else:
                return stack.insert(0, temp[0])

###################--------INOUT..... FUNEND--------###########
        elif stack[0] in inOutClosures and stack[1] != ':error:':
            argval = inOutClosure[0]
            env = inOutClosure[1]
            code = inOutClosure[2]
            arg = stack[1]
            isReturnPresent = 0 # our flag to see if return is in the inOut function
            stack.pop(0) # pop the funName
            stack.pop(0) # pop the arg
            # checks to see if variable is the same as arg in inOut fun
            if arg in hm:
                env[arg] = hm.get(arg)
                code = setVariable(code, arg, argval) # if it is, get the key and push the value from dict
            else:
                code = setVariable(code, arg, argval)
                
            for line in code:
                if line.startswith('let'):
                    temp.insert(0,parseLetII(code, env, f, closures, inOutClosures))
                elif line[0].isalpha():
                    parsePrimitive(line, temp, env, f, closures, inOutClosures)
                elif line[0] == ':':
                    parseBooleanOrError(line, temp, env)

            if isReturnPresent == 1:
                hm[arg] = env[arg] #switch to the original binding hm
                # check if variable is previously binded
                if temp[0] in env:
                    return stack.insert(0, env.get(temp[0]))
                else:
                    return stack.insert(0, temp[0])
            else:
                hm[arg] = env[arg]
        else:
            return stack.insert(0, ':error:')


def setVariable(stack, arg, variable):
    temp = []
    for i in stack:
        code = i.split()
        if variable not in code:
            temp.append(i)
        else:
            code.remove(variable)
            new = ' '.join(code) + ' ' + arg
            temp.append(new)
    return temp







hw4('testinput.txt','testoutput.txt')
