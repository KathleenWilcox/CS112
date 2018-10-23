package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	
	
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		BigInteger num = new BigInteger();
		
		if(integer == null) { throw new IllegalArgumentException(); } // check if string is empty
		
		integer = integer.trim(); // get rid of spaces on either sides of number
		
		if(integer.charAt(0) == '-') //checks to see if num is negative 
		{	
			integer = integer.substring(1);
			 num.negative = true; 
		}
		if(integer.charAt(0) == '+') //checks to see if num is negative 
		{	
			integer = integer.substring(1);
			 num.negative = false; 
		}
		
		boolean allzero = true;
		for(int i = 0; i < integer.length(); i++)
		{
			if(integer.charAt(i)!= '0') 
			{
				allzero= false;
			}
		}
		
		if(allzero) 
		{
			integer = "0";
		}
		
		int counter = 0;
		int zeros = 0;
		while(counter < integer.length() && integer.charAt(counter)== '0'&& allzero == false)
		{
			zeros++;
			counter++;
		}
		integer = integer.substring(zeros);
		
		if(integer.equals("0")) //if the digit is -0, gets rid of negative
		{
			num.negative = false;
		}
		
		for(int i = 0; i < integer.length(); i++) //checks to make sure every char is a digit
		{
			if(!(Character.isDigit(integer.charAt(i))))
			{
				throw new IllegalArgumentException();
			}
		}
			
		
		for(int i = 0 ; i < integer.length(); i++) { // traverses through the entire string
			
			char c = integer.charAt(i); // takes character of string and stores it in a char variable
			int x = Character.getNumericValue(c); // converts char to int
			DigitNode curr = new DigitNode(x, null); // creates DigitNode with int value 
			curr.next = num.front; // has object point to object stored in front
			num.front = curr; //front points to object just added, now it is in the front
			
			num.numDigits++;
			
		}
		
		return num; 
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	
	private static BigInteger reverseList(BigInteger sum)
	{	
		BigInteger newList = new BigInteger();
		
		DigitNode ptr = sum.front;
		
	boolean allZeros = true;
	
	for(DigitNode check = ptr; check!= null; check= check.next) 
	{
		if(check.digit != 0 )
		{
			allZeros = false;
		}
	}
	
		if(allZeros)
		{  
			DigitNode zero = new DigitNode(0,null);
			zero.next = newList.front;
			newList.front = zero;
			newList.numDigits = newList.numDigits+1;
			newList.negative = false;

		}
	
		while(ptr.digit == 0 && allZeros == false)
		{	
			ptr = ptr.next;
			sum.front = ptr;

		}
		
		
		if(ptr.digit < 0 ) {
			newList.negative = true;
	
		}
		
		while(ptr != null && allZeros == false)
		{	
			if(ptr.digit < 0) 
			{
				ptr.digit = ptr.digit/-1;
			}
			
			DigitNode number = new DigitNode(ptr.digit, null);
			number.next = newList.front; 
			newList.front = number;
			newList.numDigits = newList.numDigits+1;
			ptr=ptr.next;
			
		}
		
		
		return newList;
	}
	
	private static BigInteger copyOfList(BigInteger list)
	{
		BigInteger listCopy = new BigInteger();
		
		
		DigitNode pointer1 = list.front;
		if(list.negative== true)
		{	
			listCopy.negative = true;
		}
		
		while(pointer1 != null)
		{	
			int number = pointer1.digit;
			DigitNode copyNode = new DigitNode(number, null);
			copyNode.next = listCopy.front;
			listCopy.front = copyNode;
			
			pointer1 = pointer1.next;	
			listCopy.numDigits = listCopy.numDigits +1;
		}
		
		listCopy = reverseList(listCopy);
		
		return listCopy;
	}
	
		
	private static BigInteger subtractWithCarrying(BigInteger larger, BigInteger smaller)
	{	
		
		BigInteger large = new BigInteger();
		BigInteger small = new BigInteger();
		
		
		small = copyOfList(smaller);
		large = copyOfList(larger);
		
		if(larger.negative == true)
		{
			large.negative = true;
		}
		if(smaller.negative == true)
		{
			small.negative = true;
		}
			
		BigInteger differenceCarry = new BigInteger();
		DigitNode ptr1 = large.front;
		DigitNode ptr2 = small.front;
		DigitNode search = ptr1.next;
		boolean allSmaller = false;
		boolean mostlySmaller = false;
		boolean borrow = false;
		int diff = 0;
		int count= 0;
		
		if(large.numDigits < small.numDigits)
		{	
			BigInteger temp = copyOfList(larger);
			large = copyOfList(smaller);
			small = copyOfList(temp);
			
			if(larger.negative == true)
			{
				small.negative = true;
			}
			if(smaller.negative == true)
			{
				large.negative = true;
			}
			
			ptr1 = large.front;
			ptr2 = small.front;
			search = ptr1.next;
			
		}
		
		if(large.numDigits == small.numDigits)
		{ 
			while(ptr1 != null && ptr2 != null)
			{	
				if(ptr1.digit < ptr2.digit)
				{	
					allSmaller = true; 
				}	
				else
				{
					allSmaller = false;
				}

				ptr1 = ptr1.next;				
				ptr2 = ptr2.next;
				
			}
			
			
			ptr1 = large.front;
			ptr2 =small.front;
			
			while(ptr1 != null && ptr2 != null)
			{
				if(ptr1.digit <= ptr2.digit)
				{	
					count++; 	
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			
			if(count >= large.numDigits/2)
			{	
				mostlySmaller = true;	
			}
		
			ptr1 = large.front;
			ptr2 =small.front;
			int num2 = 0;
			int num1 = 0;
			if(allSmaller == true || mostlySmaller == true)
			{ 
				while(ptr1 != null && ptr2 != null)
				{
					
					num2 = ptr2.digit;
					num1 = ptr1.digit;
	
					ptr1.digit = checkforNegative(large, ptr1);
					ptr2.digit = checkforNegative(small, ptr2);
				
					diff =  num2 - num1;
					
					DigitNode add = new DigitNode(diff, null);
				
					add.next = differenceCarry.front;
					differenceCarry.front = add;
					
					ptr1 = ptr1.next;
					ptr2= ptr2.next;	
				}
			}
			
		}
		
		if(allSmaller == false)
		{
		
			while(ptr2 != null)
			{	
				if(ptr1.digit < ptr2.digit)
				{	System.out.println("hi");
					while(search != null)
						//System.out.println("ptr1: " + ptr1);
					{	//System.out.println("search digit before searching: " + search.digit);
						if(search.digit > 0 && search != ptr1)
						{	
							search.digit = search.digit -1;
							borrow = true; 
							search = ptr1.next;
							break;
						}
						else
						{	
							search = search.next;
						}
					}
				}
				
				if(borrow == true)
				{	
					ptr1.digit = ptr1.digit + 10;
					System.out.println("adding to digit in order to subtract: " + ptr1.digit);
				}
				
				ptr1.digit = checkforNegative(large, ptr1);
				ptr2.digit = checkforNegative(small, ptr2);
				
				System.out.print("subtracting : ");
				System.out.println(ptr1.digit + " + " + ptr2.digit);
				diff = ptr1.digit + ptr2.digit;
				System.out.println("diff: " + diff);
				
				
				DigitNode add = new DigitNode(diff, null);
				
				add.next = differenceCarry.front;
				differenceCarry.front = add;
				
				ptr1 = ptr1.next;
				ptr2= ptr2.next;
				
				while(borrow==true && search.digit == 0 && search.next != null) {
					search.digit = search.digit + 9;
					search = search.next;
					
				}
				
				borrow = false; 
				search = ptr1;
			}
	
			while(ptr1 != null)
			{ 	
				
				diff = ptr1.digit;
				
				DigitNode add = new DigitNode(diff, null);			
				add.next = differenceCarry.front;
				differenceCarry.front = add;
			
				ptr1 = ptr1.next; 
			}
		
		}
		//differenceCarry = reverseList(differenceCarry);
		return differenceCarry;	
	}
	
	private static int checkforNegative(BigInteger number, DigitNode singlenum)
	{	 
		int num = 0; 
	
		if(number.negative ) 
		{
			num = singlenum.digit*-1;
		}
		else 
		{
			num = singlenum.digit;
		}
		
		return num;
	}
	
	public static BigInteger add(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		BigInteger result = new BigInteger();
		BigInteger backwardsSum = new BigInteger();
				
		
			DigitNode ptr1 = first.front;
			DigitNode ptr2 = second.front; 
			int num1=0;
			int num2=0;
			int carry = 0;
			
		
			if(first.numDigits == second.numDigits ) 
			{ 	
				if(first.negative != second.negative)
				{ 	
					backwardsSum = subtractWithCarrying(first,second);	
				}
			
				else {
					
				
					while(ptr1!= null && ptr2!= null) 
					{	
					
						num1 = checkforNegative(first, ptr1);
						num2 = checkforNegative(second, ptr2);
						if(first.negative == true && first.negative == second.negative)
						{
							carry = carry * -1;
						}
						int x =  num1 + num2 + carry;
						
					
						if( ((num1 + num2 + carry) > 9) || (((num1 + num2 + carry)*-1) > 9) && (ptr1.next != null && ptr2.next!= null))
						{	
							x = (num1 + num2 + carry) % 10; 
							carry = 1;	
						}
						else
						{	
							x = (num1 + num2 + carry);
							carry = 0;
						}
					
						DigitNode add = new DigitNode(x, null);
						add.next = backwardsSum.front;
						backwardsSum.front = add;
			
						ptr1 = ptr1.next; 
						ptr2= ptr2.next;
					}
				}
				
				
				result = reverseList(backwardsSum);
			}
			
			
			if(first.numDigits > second.numDigits)
			{ 	
				if(second.negative != first.negative)
				{ 	
					backwardsSum = subtractWithCarrying(first,second);	
				}
				else {
					
					while(ptr2!= null)
					{	
						num1 = checkforNegative(first, ptr1);
						num2 = checkforNegative(second, ptr2);
						if(first.negative == true && first.negative == second.negative)
						{
							carry = carry * -1;
						}
						int x =  num1 + num2 + carry;
						
	
						
						if (((num1 + num2 + carry) > 9 || ((num1 + num2 + carry)*-1) > 9) && ptr1.next != null)
						{
							x = (num1 + num2 + carry) % 10; 
							carry = 1;
						}
						else
						{							
							x = (num1 + num2 + carry);
							carry = 0;
						}
						
						DigitNode add = new DigitNode(x, null);
						add.next = backwardsSum.front;
						backwardsSum.front = add;
					
				
						ptr1 = ptr1.next; 
						ptr2= ptr2.next;
					
					}
				
					while(ptr1!= null)
					{	
						num1 = checkforNegative(first, ptr1);
						if(first.negative == true && first.negative == second.negative)
						{
							carry = carry * -1;
						}
						int x =  num1 + carry;
						
					
						if( ((num1 + carry) > 9) || (((num1 + carry)*-1) > 9) && ptr1.next!= null)
						{
							x =  (num1+carry) % 10; 
							carry = 1;
						}
						else
						{
							x = (num1+carry);
							carry = 0;
						}
		
						DigitNode add = new DigitNode(x, null);
						add.next = backwardsSum.front;
						backwardsSum.front = add;
				
					
						ptr1 = ptr1.next; 
					
					
					}
				}
				
				result = reverseList(backwardsSum);
			}
			
			if(second.numDigits > first.numDigits)
			{ 	
				if((second.negative != first.negative))
				{ 	
					backwardsSum = subtractWithCarrying(first,second);	
					result = reverseList(backwardsSum);
				}
				else {
				
					while(ptr1!= null)
					{	
						num1 = checkforNegative(first, ptr1);
						num2 = checkforNegative(second, ptr2);
						
						if(first.negative == true && first.negative == second.negative)
						{
							carry = carry * -1;
						}
						int x =  num1 + num2 + carry;
						
					
						if( ((num1 + num2 + carry) > 9) || (((num1 + num2 + carry)*-1) > 9) && ptr2.next != null )
						{
							x = (num1+num2+carry) % 10; 
							carry = 1;
						}
						else
						{							
							x = (num1+num2+carry);
							carry = 0;
						}
					
						DigitNode add = new DigitNode(x, null);
						add.next = backwardsSum.front;
						backwardsSum.front = add;
				
				
						ptr2 = ptr2.next; 
						ptr1= ptr1.next;
					
					}
				
					while(ptr2!= null)
					{	
						num2 = checkforNegative(second, ptr2);
						if(first.negative == true && first.negative == second.negative)
						{
							carry = carry * -1;
						}
						int x = num2 + carry;
						
						if( ((num2 + carry) > 9) || (((num2 + carry)*-1) > 9)&& ptr2.next!= null)
						{
							x = (num2+carry) % 10; 
							carry = 1;
						}
						else
						{
							x = (num2+carry);
							carry = 0;
						}
					
						DigitNode add = new DigitNode(x, null);
						add.next = backwardsSum.front;
						backwardsSum.front = add;
				
						ptr2 = ptr2.next; 
					
					
					}
					
					result = reverseList(backwardsSum);
				}
				
			}
		if(first.numDigits > second.numDigits && first.negative == true)
		{
			result.negative =true;
		}
		if(second.numDigits > first.numDigits && second.negative == true)
		{
			result.negative = true;
		}
		

		return result; 
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	
	
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		BigInteger product = new BigInteger();
		BigInteger additions = new BigInteger();
		DigitNode ptr1 = first.front;
		DigitNode ptr2 = second.front;
		int carryInt = 0;
		int x = 0;
		
				
		if((ptr1.digit == 0 &&  first.numDigits == 1) || (ptr2.digit == 0 && second.numDigits == 1))
		{
			return product;
		}
		
		if((first.numDigits == 1 && second.numDigits == 1)  )
		{	
			 x = ptr1.digit * ptr2.digit;
		
			if(x > 9)
			{
				
				carryInt  = x/10;
				x = x % 10;
				
			}
			
			else
			{	
				carryInt = 0;
			}
			
			DigitNode node1 = new DigitNode(x, null);
			node1.next = product.front;
			product.front = node1;
			
			if(carryInt > 0 )
			{
				DigitNode carrynode = new DigitNode(carryInt, null);
				carrynode.next = product.front;
				product.front = carrynode;
			}
		
			product = reverseList(product);
		}
		
		
		int placeHolder = 0;
		int countLoops = 0;
		if(first.numDigits == second.numDigits && !(first.numDigits == 1 && second.numDigits == 1))
		{	
			DigitNode zero = new DigitNode(0, null);
			zero.next = product.front;
			product.front = zero;
			product.numDigits = product.numDigits +1;
		
			while( ptr2!= null )
			{ 		
				
				if(countLoops != 0)
				{	
					placeHolder++;	
				}
				
				additions.front = null;
				
				if(placeHolder > 0) 
				{	
					for(int i = 0; i < placeHolder; i++)
					{	
						DigitNode zeroplaceHolder = new DigitNode(0, null);
						zeroplaceHolder.next = additions.front;
						additions.front = zeroplaceHolder;
					}
					
				}
				
				
				while( ptr1 != null )
				{	
					x = (ptr1.digit * ptr2.digit) + carryInt;	
					
					if( x > 9 && (ptr1.next != null) )
					{	
						carryInt  = x/10;
						x = x % 10;
						DigitNode dig = new DigitNode(x, null); 
						dig.next = additions.front;
						additions.front = dig;
					}
					
					else if(x <= 9 )
					{
						carryInt = 0;
						DigitNode dig = new DigitNode(x, null); 
						dig.next = additions.front;
						additions.front = dig;
					}
					
					if(x > 9 && ptr1.next == null)
					{	
						
						DigitNode firstDig = new DigitNode((x%10), null);
						firstDig.next = additions.front; 
						additions.front = firstDig;
				
						DigitNode secondDig = new DigitNode((x/10), null);
						secondDig.next = additions.front; 
						additions.front = secondDig;
					
					}
					
					
					ptr1 = ptr1.next; 
				
				}
			
				additions = reverseList(additions);
				product = add(additions, product);

				
				
				countLoops++;
				ptr2 = ptr2.next;
				ptr1 = first.front;
				carryInt = 0;
					
			}
			
		}
		
		 placeHolder = 0;
		 countLoops = 0;
		if(second.numDigits < first.numDigits)
		{	
			DigitNode zero = new DigitNode(0, null);
			zero.next = product.front;
			product.front = zero;
			product.numDigits = product.numDigits +1;
		
			while(ptr2 != null) 
			{
				
				if(countLoops != 0)
				{	
					placeHolder++;
				}
				
				additions.front = null;
				
				if(placeHolder > 0) 
				{	
					for(int i = 0; i < placeHolder; i++)
					{	
						DigitNode zeroplaceHolder = new DigitNode(0, null);
						zeroplaceHolder.next = additions.front;
						additions.front = zeroplaceHolder;
					}
				}
				
					while(ptr1!= null){
				
				
					x = (ptr1.digit * ptr2.digit) + carryInt;
				
					if( x > 9 && (ptr1.next != null) )
					{	
						
						carryInt  = x/10;
						x = x % 10;
						DigitNode dig = new DigitNode(x, null);
						dig.next = additions.front;
						additions.front = dig;
						
					}
					
					else if(x <= 9 )
					{
						carryInt = 0;
						DigitNode dig = new DigitNode(x, null); 
						dig.next = additions.front;
						additions.front = dig;
					}
					
					if(x > 9 && ptr1.next == null)
					{	
						
						DigitNode firstDig = new DigitNode((x%10), null);
						firstDig.next = additions.front; 
						additions.front = firstDig;
				
						DigitNode secondDig = new DigitNode((x/10), null);
						secondDig.next = additions.front; 
						additions.front = secondDig;
					
					}
					
					ptr1 = ptr1.next; 
				
				}
				additions = reverseList(additions);
				product = add(product, additions);
				countLoops++;
				ptr2 = ptr2.next;
				ptr1 = first.front;
				carryInt = 0;
			}
			
		}
		
		placeHolder = 0;
		countLoops = 0;
		if(first.numDigits < second.numDigits)
		{	
			DigitNode zero = new DigitNode(0, null);
			zero.next = product.front;
			product.front = zero;
			product.numDigits = product.numDigits +1;
		
			while(ptr1 != null) 
			{	
				if(countLoops != 0)
				{	
					placeHolder++;
				}
			
				additions.front = null;
			
				if(placeHolder > 0) 
				{	
					for(int i = 0; i < placeHolder; i++)
					{
						DigitNode zeroplaceHolder = new DigitNode(0, null);
						zeroplaceHolder.next = additions.front;
						additions.front = zeroplaceHolder;
					}
				}
				
				while(ptr2!= null)
				{

					x = (ptr1.digit * ptr2.digit) + carryInt;
				
					if( x > 9 && (ptr2.next != null) )
					{	
						
						carryInt  = x/10;
						x = x % 10;
						DigitNode dig = new DigitNode(x, null); 
						dig.next = additions.front;
						additions.front = dig;
						
					}
					
					else if(x <= 9 )
					{
						carryInt = 0;
						DigitNode dig = new DigitNode(x, null); 
						dig.next = additions.front;
						additions.front = dig;
					}
					
					if(x > 9 && ptr2.next == null)
					{	
						
						DigitNode firstDig = new DigitNode((x%10), null);
						firstDig.next = additions.front; 
						additions.front = firstDig;
				
						DigitNode secondDig = new DigitNode((x/10), null);
						secondDig.next = additions.front; 
						additions.front = secondDig;
					
					}
					ptr2 = ptr2.next; 
				
				}
				
				additions = reverseList(additions);
				product = add(product, additions);
				countLoops++;
				ptr1 = ptr1.next;
				ptr2 = second.front;
				carryInt = 0;
			}
			
		}
		
		if(first.negative == second.negative)
		{
			product.negative = false;
		}
		else
		{
			product.negative = true;
		}
		
		
		return product; 
		
		}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
