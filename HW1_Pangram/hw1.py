''' @author Winnie Liang
    @title CSE 305 Assignment 1
    @purpose A function that accepts two strings (the names of input and output files) as arguments.
    Reads the input file line-by-line and checks if the line is a pangram. Lastly, the program outputs the boolean value in the provided output file.
    A pangram is a sentence that contains all the letters of the English alphabet at least once.
    Example: "The quick brown fox jumps over the lazy dog" - True
'''

#Method to read file input and write to file output
def hw1(input, output):
    try:
        input = open(input, 'r')
        output = open(output, 'w+')
        #Iterating through a file for every line of the file
        for line in input.readlines():
            str = line.lower()
            if isPangram(str) == False:
                output.write('false\n')
            else:
                output.write('true\n')
        input.close()
        output.close()
    except IOError:
        print ("Error: Unable to find file")

#Helper method called isPangram
def isPangram(line):
    alphabet = set('abcdefghjijklmnopqrstuvwxyz')
    str = set(line.lower())
    if str.issuperset(alphabet):
        return True
    return False



#Function call to hw1

hw1('sample_input_3.txt', 'sample_output_3.txt')

