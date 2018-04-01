public class JudgeScore {
    public static void main(String[] args) {
 
      
 
        //Declaring the variables for storing the judges scores.
        int judge1, judge2, judge3, judge4;
        judge1 = Integer.valueOf(args[0]);
        judge2 = Integer.valueOf(args[1]);
        judge3 = Integer.valueOf(args[2]);
        judge4 = Integer.valueOf(args[3]);
  
       
  
        //Declaring the variable for the final score
        double score;
        //Declaring and assigning the variable for the total score
        double total = judge1 + judge2 + judge3 + judge4;
  
        //Declaring variables for the maximum and minimum scores
        double max, min;
  
        //Declaring and assigning the boolean variables for the possible scenarios
        boolean judge1High = judge1 >= judge2 && judge1 >= judge3 && judge1 >= judge4;
        boolean judge1Low = judge1 <= judge2 && judge1 <= judge3 && judge1 <= judge4;

        boolean judge2High = judge2 >= judge1 && judge2 >= judge3 && judge2 >= judge4;
        boolean judge2Low = judge2 <= judge1 && judge2 <= judge3 && judge2 <= judge4;
 
        boolean judge3High = judge3 >= judge1 && judge3 >= judge2 && judge3 >= judge4;
        boolean judge3Low = judge3 <= judge1 && judge3 <= judge2 && judge3 <= judge4;

        //Using conditional statements to determine the maximum score
        if (judge1High){
            max = judge1;
        } else if (judge2High){
            max = judge2;
        } else if (judge3High){
            max = judge3;
        } else {
            max = judge4;
        } 
  
        //Using conditional statements to determine the minimum score
        if (judge1Low){
            min = judge1;    
        } else if (judge2Low){
            min = judge2;
        } else if (judge3Low){
            min = judge3;    
        } else {
            min = judge4; 
        } 
  
        //Calculating the final score and displaying it
        score = (total-(max + min))/2;
        System.out.println(score);
  
        
    }
}
