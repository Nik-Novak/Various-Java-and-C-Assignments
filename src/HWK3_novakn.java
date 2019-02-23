import java.util.ArrayList;

public class HWK3_novakn {
	public static void main(String[] args){
		ArrayList<Double> temp = new ArrayList<>();
		temp.add((double) 1);
		temp.add((double) 2);
		temp.add((double) 3);
		temp.add((double) 4);
		temp.add((double) 5);
		//temp.add((double) 6);
		subSum(temp);
		allSubs.add(temp);
		
		
		
		/*for(int i=0;i!=allSubs.size();i++){
			System.out.println("NEW SUB");
			for(int j=0;j!=allSubs.get(i).size();j++)
				System.out.println(allSubs.get(i).get(j));
		}*/
		ArrayList<ArrayList<Double>> finalSubs = new ArrayList<ArrayList<Double>>();
		for(int i=0; i!=allSubs.size(); i++){
			double sum = 0;
			for (int j= 0; j!= allSubs.get(i).size();j++){
				sum+=allSubs.get(i).get(j);
			}
			
			if(sum == 7)
				finalSubs.add(allSubs.get(i));
		}
		
		ArrayList<ArrayList<Double>> FINALLY = kO(finalSubs);
		
		printM(FINALLY);
		
		/*System.out.println("FINALLY%#^Y%$*@UY*#^%$W");
		
		for(int i=0;i!=FINALLY.size();i++){
		System.out.println("NEW SUB");
		for(int j=0;j!=FINALLY.get(i).size();j++)
			System.out.println(FINALLY.get(i).get(j));
		}*/
		
		/*for(int i=0;i!=finalSubs.size();i++){
			System.out.println("NEW SUB");
			for(int j=0;j!=finalSubs.get(i).size();j++)
				System.out.println(finalSubs.get(i).get(j));
		}*/
		
	}
	
	public static ArrayList<ArrayList<Double>> kO(ArrayList<ArrayList<Double>> M){
		ArrayList<Integer> remIndex = new ArrayList<Integer>();
		for(int i = 0; i!=M.size();i++)//For every set
			for (int j=0;j!=M.size();j++){//For every other Set
				if (i==j || M.get(i).size()!= M.get(j).size())//exclude if they're not same size or comparing itself
					continue;
				boolean flag = true;
				for(int k =0; k!=M.get(i).size();k++){ //compare each element
					if (M.get(i).get(k) != M.get(j).get(k))
						flag = false;
				}
				if (flag)
					remIndex.add(i);
			}
		//REM AFTER
		//printM(M);

		/*System.out.println("INDICES TO BE REMOVED");
		for (int i=0; i!=remIndex.size();i++)
			System.out.println(remIndex.get(i));
		//*/
		System.out.println("SIZE OF INDICES: " + remIndex.size());
		for(int i=0;i!=remIndex.size()/2;i++){
			System.out.println("REMOVED:" + remIndex.get(i));
			M.remove((int)remIndex.get(i));
			
		}
		
		//printM(M);
		
		
		return M;
	}
	
	public static void printM(ArrayList<ArrayList<Double>> M){
		for(int i=0;i!=M.size();i++){
			System.out.println("NEW SUB");
			for(int j=0;j!=M.get(i).size();j++)
				System.out.println(M.get(i).get(j));
			}
	}
	

	public static double binCoef(double n, double k){
		if (n==k || k==0)
			return 1;
		return ( (n)/(k)*binCoef(n-1,k-1) );
	}
	
	static ArrayList<ArrayList<Double>> allSubs = new ArrayList<ArrayList<Double>>();
	
	public static ArrayList<ArrayList<Double>> subSum(ArrayList<Double> M){
		
		
		for (int i = 0; i!= M.size();i++){
			ArrayList<Double> temp = (ArrayList<Double>) M.clone();
			temp.remove(i);
			allSubs.add(temp);
			subSum(temp);
		}
		
		return null;
	}
}
