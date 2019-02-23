/*
Name: Nikolas Novak
MacID: novakn
Student Number: 1406602
Description: This program is assignment 1 of the class 2SO3. It receives an array of arguments from the command line and prints out the reverse of said array.
*/
public class HWK1_novakn {
	public static void main(String[] args){
		for (int i = args.length-1; i>=0 ;i--)
			System.out.print(args[i] + " ");
	}
}
