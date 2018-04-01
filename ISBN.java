public class ISBN {

    public static void main(String[] args) {
 
    
 
        //Declaring the variable to represent the ISBN number
        int n = Integer.parseInt(args[0]);
        
        
  
        
        //Declaring and assigning the first 4 digits of the ISBN by using the powers of 10
        int d5 = n/1000;
        int d4 = (n-d5*1000)/100;
        int d3 = (n-(d5*1000+d4*100))/10;
        int d2 = (n-(d5*1000+d4*100+d3*10))/1;
        
        //Finding the sum of the first 4 digits multiplied by their respective number
        int first4 = 5*d5 + 4*d4 + 3*d3 + 2*d2;
        
        //Finding the factor of the final sum and the multiple of 11
        int factor = first4/11 + 1;
        int multiple = factor*11;
        
        //Finding d1
        int d1 = multiple - (5*d5 + 4*d4 + 3*d3 + 2*d2);
        
        /*Displaying d1 as X if it is 10, or otherwise as its respective number 
         between 0(inclusive) and 10*/
        if (d1 == 10) {
            System.out.println("X");
        } else if (d1 == 11) {
            System.out.println("0");
        } else {
            System.out.println(d1);
        }
       
       
    }

}
