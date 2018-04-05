# CSV-Generator
Simple java utility for repilcating input csv file and genrating configured replication with variations

# Usage Guide
1st line is treated as header and will be simple added in output file without replication
  *keyword ${alpha} is replaced by 12 characeter random alpha numeric string 
  *keyword ${num} is replaced by current line number

# Run 
Invoke class com.poojan.kothari.CSVGenerator.java with command line params <numberOfReplication> <InputCSVFilePath> <OutputCSVFilePath>
  
# Example 
java -cp <classpath> com.poojan.kothari.CSVGenerator 3 /home/poojan/input.csv /home/poojan/output.csv
  
  
 Suggestion are welcomed :)
