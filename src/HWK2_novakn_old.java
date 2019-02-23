import java.util.ArrayList;//imports ArrayList object, to contain list of matrices (easier to read/more clarity than 3D arrays)

public class HWK2_novakn_old {//Beginning og HWK2_novakn class
	public static void main(String[] args){//Main method, with args passed in
		int[] input = new int[args.length];//converting strings
		if(input.length!=0){//perform the required operations only if there actually was input
			for(int i=0;i!=input.length;i++)//for every string input element of args
				input[i] = Integer.parseInt(args[i]);//convert string input to int and store it in an array
			ArrayList<double[][]> matrices = create2D(input);//container ArrayList for all 2D arrays (i.e. matrices)
			System.out.println("INPUT MATRICES");
			for(int n=0; n!=matrices.size();n++)
				printM(matrices.get(n));
			System.out.println("END INPUT MATRICES");
			double[][] multM = multiply(matrices);//invokes the multiply method and stores the result of M1*M2..Mn in multM
			System.out.println("Multiplied:");
			printM(multM);
			System.out.println("Inverted:");
			printM(invert(multM)); //Inverts the multiplied array
		}//end inputif
		else System.out.println("No input received");//prints a notice if there is no input
	}//end main()
	
	public static double[][] invert(double[][] M) {//method which inverts a matrix M and returns this inverted 2D array
		if(M.length==1&&M[0].length==1){//checks for the case of a 1x1 matrix
			double[][] d = {{1/M[0][0]}};//instantly computes the inverse if it is a 1x1 matrix
			return d;//returns inverse
		}//end 1x1 if
		double redFact = reduceM(M);//invokes reduceM, to reduce the matrix to a smaller size (see reduceM method comment for an explanation on why this is necessary) and stores the reduction factor
		double determinant = gaussianDet(M);//invokes determinant computation and stores the result
		if(determinant == 0)//checks if matrix is singular
			System.out.println("Matrix not invertible");//prints a notice if so
		double[][] tempInv = adjInv(M,determinant);//invokes adjInv which computes the inverse of a matrix through its adjoint and its determinant, storing it in a temporary variable
		regainM(tempInv,redFact);//re-enlarges the matrix to the original factor by multiplying by the previously divided factor
		return tempInv;//returns the actual inverted matrix
	}//end invert()
	
	private static double reduceM(double[][] M) {//matrix reduction method, which divides every element by the minimum matrix element, so as to reduce the figures for Gaussian Elimination. Through test cases, it was found that matrices with even marginally large values would easily overflow the maximum capacity of a double due to constant multiplication.
		double min = M[0][0];//set the minimum element value to the first element
		for(int i=0;i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				if(M[i][j]<min)//if the matrix entry is smaller than the minimum
					min=M[i][j];//assign new minimum
		for(int i=0;i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				M[i][j]/=min;//divide each entry by the minimum entry
		return min;//return this reduction factor
	}//end reduceM()
	
	private static void regainM(double[][] M, double redFact){//matrix regain method which counteracts the reduction used earlier (before heavy computation), in order to compute the proper inverse
		for(int i=0;i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				M[i][j]/=redFact;//divide each element by the factor
	}//end regainM()
	
	private static double[][] copy(double[][]M){//method to prevent aliasing
		double[][] nM = new double[M.length][M[0].length];//creating a new matrix of the same size
		for (int i=0;i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				nM[i][j]=M[i][j];//copy elements to new matrix
		return nM;//return new matrix
	}//end copy()

	private static double[][] adjInv(double[][] M,double detM) {//method that computes the adjoint matrix and then divides it by the determinant to compute the inverse
		M = copy(M);//avoiding aliasing
		double[][] adj = new double[M.length][M[0].length];//creating new matrix of same size to be the adjoint matrix
		for (int i=0;i!=M.length;i++)//for every row
			for (int j=0;j!=M[0].length;j++)//for every column
				adj[i][j]=Math.pow(-1, i+j)*gaussianDet(subM(M,i,j))/detM;//computes the Cofactor matrix entry by: creating a + - checkerboard, multiplying this by the determinant of the minor, and dividing the result by the determinant
		return transpose(adj);//returns the transpose of the cofactor matrix (adjoint)
	}//end adjInv()
	
	private static double[][] subM(double[][] M, int excR, int excC){//a method which creates a sub-matrix of a given matrix M by specifying which rows to be "deleted"
		M = copy(M);//avoid aliasing
		int count = 0;//counter used to track the number of valid entries read
		double[][] nM = new double[M.length-1][M[0].length-1];//new matrix of size one integer smaller than original matrix
		for (int i=0;i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				if(i!=excR && j!= excC){//if the entry is valid for the minor matrix (i.e. doesn't belong to an excluded row or column)
					nM[count/nM.length][count%nM.length] = M[i][j]; //calculate the position of the valid value in the new matrix, using count and the new matrix's length, and store the valid value there (left-right and top-bottom)
					count++;//incrememnt count
				}//end validity check
		return nM;//return the new minor matrix
	}//end subM()
	
	private static double[][] transpose(double[][]M){//takes a given matrix and returns the transpose
		M = copy(M);//avoid aliasing
		double[][] nM = new double[M.length][M[0].length];//new empty matrix of same size
		for(int i=0; i!=M.length;i++)//for every row
			for(int j=0;j!=M[0].length;j++)//for every column
				nM[j][i] = M[i][j];//switch the row index with the column index of the given matrix M and store it in the blank matrix
		return nM;//return the transpose
	}//end transpose()

	private static double gaussianDet(double[][] M) {//computes the determinant by creating an upper triangular matrix through Gaussian Elimination and multiplying the diagonal entries
		M = copy(M);//avoid aliasing
		double detFactor = 1;//factor value that stores the changes to the determinant due to row operations
		int n = M.length; //shorter way to reference the array's #of rows and #of columns
		for(int s=0;s!=n-1;s++){//for every column in M
			for(int r=n-1;r!=s;r--){//for every element under pivot (starting from bottom and ending at first value under pivot)
				if(M[s][s]==0)//if the pivot element is 0
					for(int z=n-1;z!=s;z--)//for every element under pivot
						if(M[z][s]!=0)//if an element under the pivot is non-zero
							detFactor*=row_swap(M,s,z);//swap rows such that we have a non-zero pivot
				if(M[r][s]==0)//if the element under pivot is already 0
					continue;//skip to next iteration
				int negate = -1;//negation value for use in calculating what to multiply two rows by in order to cause an elimination (by default, if both the pivot and the Row Of Interest are of same sign then one must be negated by multiplying *(-1) negation value)
				if (M[r][s]*M[s][s]<0)//if the Pivot and ROI are of different signs, there's no need to negate one of them, they already destroy each other upon addition
					negate = 1;//remove the negation effect
				double temp = M[s][s];//temporary variable to store original value so as not to multiply multiple times (through aliasing in row_sMult)
				detFactor*=row_sMult(M,s,Math.abs(M[r][s]));//multiply pivot row by the ROI element (store the determination multiplication factor)
				detFactor*=row_sMult(M,r,negate*Math.abs(temp));//multiply ROI by the pivot's original element, negating if necessary (store the determination multiplication factor)
				row_add(M,s,r);//add both rows together
				if(M[r][s]>=5E-15)//if the addition of both rows has somehow not produced 0 (occurs when values get too large, etc. but fixed by reduction)
					System.out.println("Critical algorithm failure");//print a notice
			}//end for every element under pivot
		}//end for every column
		double det=1;//determinant product variable
		for(int i =0; i!=n;i++)//for every diagonal element (#diagonal elems = #rows = #columns = n)
			det*= M[i][i];//multiply together each diagonal and store it in det
		return det/detFactor;//return the determinant but make sure it is properly affected by row operations performed (i.e. convert determinant of triangular matrix to determinant of original matrix)
	}//end gaussianDet()
	
	private static double row_swap(double[][] M, int s, int z) {//swaps two rows of a matrix, s and z and returns the determinant change factor of -1
		double temp;//temp value swapping variable
		for(int i=0;i!=M[0].length;i++){//for every element in the row
			temp = M[s][i];//store the s row element in a temp so as not to get overwritten
			M[s][i]=M[z][i];//replace the s row element with the appropriate element from z row
			M[z][i]=temp;//place the original s row element in the z row
		}//end for every row element
		return -1;//return determinant change factor
	}//end row_swap()
	
	private static double row_sMult(double[][] M, int row, double k){//multiply a row of a matrix by a given scalar
		for(int i=0; i!= M[0].length;i++)//for every element in the row
			M[row][i]*=k;//multiply the row element's value by constant k and store back in itself
		return k;//return the determinant change factor
	}//end row_sMult()
	
	private static void row_add(double[][] M, int row1, int row2){ //adds two rows together and stores it in the second row for a given matrix (row1 + row2 -> row2)
		for(int i=0; i!=M[0].length;i++)//for every element in the row
			M[row2][i] += M[row1][i];//add the first row element to the second and store it in the second
	}//end row_add()
	
	public static ArrayList<double[][]> create2D(int input[]){//takes the given integer array (required input) and converts it to a set of 2D matrices for ease-of-use
		int initIndex = input[0]*2 +1;//addition to an index which allows to skip exactly the number of matrix size-specificative integers
		int sizeLast = 0;//variable for storing the size of the last matrix, to shift the index over by that amount for the next matrix
		ArrayList<double[][]> arrays = new ArrayList<double[][]>();//container for all matrices read-in
		for (int N = 0; N!= input[0]; N++){ //for each matrix
			arrays.add(new double[input[1+N*2]][input[2+N*2]]);//initialize the array 2D size based on the size-specific integers and store it in the ArrayList
			double[][] curMatrix = arrays.get(N);//store the current array we're concerned with in a variable
			int count = 0;//count variable for shifting the index after elements are read
			int nRows = curMatrix.length;//easy reference of the number of rows in the new matrix
			int nCol = curMatrix[0].length;//easy reference of the number of columns in the new matrix
			if(N>0)//check to avoid index error for accessing the ArrayList at (N-1). (so only perform this for matrices after the first)
				sizeLast += arrays.get(N-1)[0].length * arrays.get(N-1).length;//add the last array's size to the index shifter to avoid reading the same values

			for (int i=0; i!= nRows; i++)//for every row
				for(int j = 0; j!= nCol;j++){//for every column
					curMatrix[i][j] = input[initIndex + count + sizeLast];//find the index of the appropriate element by: skipping the size-specific integers, skipping the previously read elements of that matrix, skipping the indices of previous matrix entries. Then assign the corresponding input element to the ith row jth column of 2D array
					count++;//increment the current matrix reader index
				}//end for every column
		}//end for each matrix
		return arrays;//return the ArrayList of matrices
	}//end create2D()
	
	public static double[][] multiply(ArrayList<double[][]> matrices){//multiply a list of matrices together and return a single matrix
		ArrayList<double[][]> MS = (ArrayList<double[][]>) matrices.clone();//avoid aliasing
		for (int n=1; n!= MS.size();n++){//for every matrix in the ArrayList, beginning at the second
			double[][] temp = new double[MS.get(n-1).length][MS.get(n)[0].length];//make a temporary matrix of matching row space of the previous matrix, and matching column space of the current matrix
			if(MS.get(n).length != MS.get(n-1)[0].length){//if the #columns of the previous matrix doesn't match the #rows of the current matrix
				System.out.println("Multiplication Error");//print a notice of failed multiplication
				return null;//exit the method
			}//end if(column-row check)
			else{//otherwise perform multiplication
				for(int i=0;i!=MS.get(n-1).length;i++)//for every row of new matrix
					for(int j=0;j!=MS.get(n)[0].length;j++){//for every column of new matrix
						int sum=0;//sum variable for storing the element multiplication results (final value of the new matrix)
						for(int s=0;s!=MS.get(n-1)[0].length;s++)//iterate the same number of times as multiplications needed (ie.e # of columns of the first)
							sum += MS.get(n-1)[i][s] * MS.get(n)[s][j];//multiply across the ith row of the previous matrix with the jth column of the current matrix, and add it to the overall sum for that element
						temp[i][j] = sum;//store the sum in the new array
					}//end for every column
				MS.set(n, temp);//set the new matrix to the nth element (overwrite the 'current matrix' we just worked with)
			}//end else
		}//end for every matrix
		return MS.get(MS.size()-1).clone();//return the final element of the ArrayList (the final element is the product of all previous matrices in order)
	}//end multiply()
	
	public static void printM(double[][] M){//a more advanced print function which allows for a better visualization of any given matrix (prints in a table-like format to the stdout)
		System.out.println("************MATRIX PRINT*****************");//visual organizer
		for (int i=0; i!= M.length;i++){//for every row
			for (int j=0;j!= M[0].length;j++)//for every column
				System.out.print(M[i][j] + "\t");//successively print (side by side) the values of the matrix, separated by tab characters
			System.out.println();//at the end of each row (all columns for that row are printed) start the next line
		}//end for every row
		System.out.println("*****************************************");//visual organizer
	}//end printM()
}//end class HWK2_novakn