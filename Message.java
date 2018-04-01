package assignment1;

public class Message {
	
	public String message;
	public int lengthOfMessage;

	public Message (String m){
		message = m;
		lengthOfMessage = m.length();
		this.makeValid();
	}
	
	public Message (String m, boolean b){
		message = m;
		lengthOfMessage = m.length();
	}
	
	/**
	 * makeValid modifies message to remove any character that is not a letter and turn Upper Case into Lower Case
	 */
	public void makeValid(){
		//INSERT YOUR CODE HERE
		
		//Creating a new String to store the new message
		String newMessage = "";
		
		//Modifying the String 
		this.message = this.message.toLowerCase();
		for(int i=0; i<this.message.length(); i++) {
			if(Character.isLetter(this.message.charAt(i))) {
				newMessage += this.message.charAt(i);
			}
		}
		//Updating the message attribute and the amount of letters in the message
		this.lengthOfMessage = newMessage.length();
		this.message = newMessage;
	
	}
	
	
	/**
	 * prints the string message
	 */
	public void print(){
		System.out.println(message);
	}
	
	/**
	 * tests if two Messages are equal
	 */
	public boolean equals(Message m){
		if (message.equals(m.message) && lengthOfMessage == m.lengthOfMessage){
			return true;
		}
		return false;
	}
	
	/**
	 * caesarCipher implements the Caesar cipher : it shifts all letter by the number 'key' given as a parameter.
	 * @param key
	 */
	public void caesarCipher(int key){
		// INSERT YOUR CODE HERE
		
		//Creating a String to store the modified message
		String newMessage = "";
		//Shifting each letter by the given key
		for(int i=0; i<this.message.length(); i++) {
			//Accounting for positive keys
			if(key>=0) {
				char shifted = (char)(this.message.charAt(i)+(key%26));
				//Adjusting cases where the shifted character is beyond the lowercase alphabet
			    if(shifted > 'z') {
			    	shifted = (char)(shifted - 26);
			    }
				newMessage += shifted;
			} else {
				//Accounting for negative keys
				char shifted = (char)(this.message.charAt(i)+((26+key)%26));
				//Adjusting cases where the shifted character is beyond or before the lowercase alphabet
				if(shifted > 'z') {
		           	shifted = (char)(shifted - 26);
		        }
				if(shifted < 'a') {
			        shifted = (char)(shifted + 26);
			    }
				newMessage += shifted;
			}
		}
		//updating the attribute
		this.message = newMessage;
		
	}
	
	public void caesarDecipher(int key){
		this.caesarCipher(- key);
	}
	
	/**
	 * caesarAnalysis breaks the Caesar cipher
	 * you will implement the following algorithm :
	 * - compute how often each letter appear in the message
	 * - compute a shift (key) such that the letter that happens the most was originally an 'e'
	 * - decipher the message using the key you have just computed
	 */
	public void caesarAnalysis(){
		// INSERT YOUR CODE HERE
		//Calling the uniqueCharacters method to create a char array with one occurrence of each char in the message
		//Repeated chars in the message are replaced with '*'s
		char[] uniqueChars = uniqueCharacters();
		//Creating another char array to store only the letters 
		//Getting rid of the '*'s from the uniqueChars array that represent repeated chars
		char[] letters = new char[numUnique(uniqueChars)];
		int i=0; 
		int index=0;
		/*Going through the uniqueChars array and initializing the letters array 
		with only the elements in uniqueChars that are letters */
		while(i<letters.length) {
			for(int j=index; j<uniqueChars.length; j++) {
				if(Character.isLetter(uniqueChars[j])) {
					letters[i] = uniqueChars[j];
					i++; 
					index++;
					break;
				} else {
					index++;
					continue;
				}
			}
		}
		
		//Creating an int array to store the number of times each character appears in the message
		//Each element of the int array corresponds to the char at the same index of the letters array
		int[] numChars = new int[letters.length];
		for(int c=0; c<numChars.length; c++) {
			//calling the countChars method to determine the frequency of the specific char
			numChars[c] = countChars(letters,c);
		}
		
		//Finding the largest number in the numChars array 
		/*The char in the letters array with the same index as the largest number 
		in the numChars array is the letter that appears the most in the message*/
		int largest =numChars[0];
		int indexOfLargest =0;
		for(int n=0; n<numChars.length; n++) {
			if(largest<numChars[n]) {
				largest = numChars[n];
				indexOfLargest = n;
			}
		}
		//Creating a char variable to store the most frequent char
		char mostFrequent = letters[indexOfLargest];
		
		//Creating a variable for the key and calling the method caesarDecipher to decipher the message
		int key = (int)(mostFrequent - 'e');
		caesarDecipher(key);
	
	}
	//A helper method to count the number of the same letters in the message
	public int countChars(char[] letters, int index) {
		int numChar=0;
		for(int i=0; i<this.message.length(); i++) {
			if(letters[index]==this.message.charAt(i))
				numChar++;
		}
		return numChar;
	}
	//A helper method to create a character array storing the unique characters of a message
	//Non-unique characters will be represented as a '*'
	public char[] uniqueCharacters() {
		
		//Creating an array to store the chars and first initializing the elements to be '*'
		char[] uniqueChars = new char[this.lengthOfMessage];
		for(int i=0; i<uniqueChars.length; i++) {
			uniqueChars[i] = '*';
		}
		
		int index =0;
		int i=0;
		/*Going through the array and calling the exists method to find if each char 
		in the message already exists in the array*/
		//If the char does not exist, it replaces the '*' in the uniqueChars array 
		while(i<uniqueChars.length) {
			for(int j=index; j<this.lengthOfMessage; j++) {
				if(!exists(uniqueChars,j)) {
					uniqueChars[i] = this.message.charAt(j);
				}
				i++;
				index++;
			}
		}
		return uniqueChars;
	}
	/*A helper method to determine whether a character in a message
	already exists in the uniqueChars array*/
	//Returns true if the char exists, and false otherwise
	public boolean exists(char[] uniqueChars, int j) {
		for(int i=0; i<uniqueChars.length; i++) {
			if(this.message.charAt(j) == uniqueChars[i])
				return true;
		}
		return false;
	}
	//A helper method to determine the number of unique characters in the message
	public int numUnique(char[] uniqueCharacters) {
		/*Calling the uniqueCharacters method to create a char array to store the characters
		and '*'s representing the repeated chars*/
		char[] uniqueChars = uniqueCharacters();
		int numUniqueChars=0;
		for(int i=0; i<uniqueChars.length; i++) {
			if(Character.isLetter(uniqueChars[i])) {
				numUniqueChars++;
			} else {
				continue;
			}
		}
		return numUniqueChars;
	}

	
	/**
	 * vigenereCipher implements the Vigenere Cipher : it shifts all letter from message by the corresponding shift in the 'key'
	 * @param key
	 */
	public void vigenereCipher (int[] key){
		// INSERT YOUR CODE HERE
		String newMessage = "";
		
		//Going through each letter in the message and calling the shiftByKey method 
		//Each letter is shifted by its respective key
		for(int i=0; i<this.message.length(); i++) {
			newMessage += (char)(shiftByKey(this.message.charAt(i), key[i%(key.length)]));
		}
		//updating the message atribute
		this.message = newMessage;
		
	}
	//A helper method to shift a letter by a given key
	public char shiftByKey(char c, int key) {
		char shifted = c;
		if(key>=0) {
			shifted = (char)(shifted+(key%26));
			//Adjusting cases where the shifted character is beyond the lowercase alphabet
		    if(shifted > 'z') {
		    	shifted = (char)(shifted - 26);
		    }
			
		} else {
			//Accounting for negative keys
			shifted = (char)(shifted+((26+key)%26));
			//Adjusting cases where the shifted character is beyond or before the lowercase alphabet
			if(shifted > 'z') {
	           	shifted = (char)(shifted - 26);
	        }
			if(shifted < 'a') {
		        shifted = (char)(shifted + 26);
		    }
		}
		return shifted;
	}

	/**
	 * vigenereDecipher deciphers the message given the 'key' according to the Vigenere Cipher
	 * @param key
	 */
	public void vigenereDecipher (int[] key){
		// INSERT YOUR CODE HERE
		
		String newMessage = "";
		//Going through each letter in the message and calling the shiftByKey method 
		//Each letter is shifted back by its respective key
		for(int i=0; i<this.message.length(); i++) {
			newMessage += (char)(shiftByKey(this.message.charAt(i), -(key[i%(key.length)])));
		}
		//updating the message attribute
		this.message = newMessage;
		
	}
	
	/**
	 * transpositionCipher performs the transition cipher on the message by reorganizing the letters and eventually adding characters
	 * @param key
	 */
	public void transpositionCipher (int key){
		// INSERT YOUR CODE HERE
		
		//Finding the number of remaining spaces that will be filled by stars
		int remainingSpaces;
		if(this.lengthOfMessage % key == 0) {
			remainingSpaces=0;
		} else {
			remainingSpaces = key - (this.lengthOfMessage % key);
		}
		//Creating a 2D cipher array to store the chars 
		int numCharsPerColumn = (this.message.length() + remainingSpaces)/key;
		char[][] cipher = new char[key][numCharsPerColumn];
		
		//Going through the message and storing it into the 2D cipher array
		//Adding stars where there are empty spaces in the array
		int index=0;
		for(int i=0; i<numCharsPerColumn; i++) {
			for(int j=0; j<key; j++) {
				if(index<this.message.length()) {
					cipher[j][i] = this.message.charAt(index);
					index++;
				} else {
					cipher[j][i] = '*';
					index++;
				}
			}
		}
		//Constructing a String for the ciphered message
		//Going through the elements of the cipher array to create the message
		String newMessage = "";
		for(int m=0; m<key; m++) {
			for(int n=0; n<numCharsPerColumn; n++) {
				newMessage += cipher[m][n];
			}
		}
		//Updating attributes
		this.message = newMessage;
		this.lengthOfMessage += remainingSpaces;
	}
	
	/**
	 * transpositionDecipher deciphers the message given the 'key'  according to the transition cipher.
	 * @param key
	 */
	public void transpositionDecipher (int key){
		// INSERT YOUR CODE HERE
		
		//Finding the number of stars in the message in order to update the message length
		int numStars=0;
		for(int i=0; i<lengthOfMessage; i++) {
			if(this.message.charAt(i) == '*') {
				numStars++;
			}
		}
		//Creating a 2D decipher array store the chars of the message
		int numCharsPerColumn = (this.message.length() / key);
		char[][] decipher = new char[key][numCharsPerColumn];
		int index =0;
		//Going through the message to initialize the elements of the decipher array 
		for(int i=0; i<key; i++) {
			for(int j=0; j<numCharsPerColumn; j++) {
				decipher[i][j] = this.message.charAt(index);
				index++;
			}
		}
		//Creating a String to store the deciphered message
		//Running through the elements of the decipher array to construct the deciphered message
		String newMessage = "";
		for(int m =0; m<numCharsPerColumn; m++) {
			for(int n=0; n<key; n++) {
				if(Character.isLetter(decipher[n][m])) {
					newMessage += decipher[n][m];
				}
				
			}
		}
		//Updating attributes
		this.message = newMessage;
		this.lengthOfMessage -= numStars;
	}
	
}

