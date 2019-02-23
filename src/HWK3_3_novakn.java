/*
Name: Nikolas Novak
MacID: novakn
Student Number: 1406602
Description: This is a java program that receives as input an array of numbers and as teh last item, a sum. THe program then recursively computes all the combinations of these numbers that sum to teh given sum and stores tehse in a set.
*/

//imports
import java.util.ArrayList;//import ArrayList
import java.util.HashMap;//import HashMap
import java.util.HashSet;//import HashSet
import java.util.List;//import List
import java.util.Set;//import Set

public class HWK3_3_novakn {//begin HWK3_3_novakn class
	static ArrayList<ArrayList<Character>> allSubs = new ArrayList<ArrayList<Character>>();//global list of lists of chars that stores all 
	
	static ArrayList<String> allRem = new ArrayList<String>();

		public static void main(String[] args){
			String str = "12345";

			calcRem(str,0);
			calcRem(str,1);
			
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			for(int i=0; i!= str.length();i++){
				map.put(str.charAt(i), i);
			}
			map.put(',', -2);
			map.put(' ',-3);
			
			ArrayList<Character> s = new ArrayList<Character>();
			for (char c : str.toCharArray()) {
				  s.add(c);
				}
			subString(s);

			int n = allSubs.size();
			for(int i=0; i!= n; i++){
				try{
					ArrayList<Character> tempC = (ArrayList<Character>) allSubs.get(i).clone();
					tempC.remove(0);
					allSubs.add(tempC);
				}catch(Exception e){}
				try{
					ArrayList<Character> tempC = (ArrayList<Character>) allSubs.get(i).clone();
					tempC.remove(tempC.size()-1);
					allSubs.add(tempC);
				}catch(Exception e){}
				
			}
			
			ArrayList<String> subsAsString = cAarrayTosArray(allSubs);

			List<String> al = subsAsString;
			Set<String> hs = new HashSet<>();
			hs.addAll(al);
			al.clear();
			al.addAll(hs);
			
			for(int i = 0; i< al.size(); i++){
				try{
				if(al.get(i).length()<2){
					al.remove(i);
					i--;
				}
				}catch(Exception e){}
				
				
				
			}
			try{
				for(int i = 0; i!= allRem.size();i++)
					for(int j=0; j!=al.size();j++){
						if(allRem.get(i).equals(al.get(j))){
							al.remove(j);
							j--;
						}
					}
			}catch(Exception e){}
			
			System.out.println(al);
			
			for(int i = 0; i!= al.size(); i++){
				String temp = al.get(i);
				int l = temp.length();
				for (int k = 0; k!=l-1;k++){
					System.out.println();
					int oPos = map.get(temp.charAt(k));
					int oPosNext = map.get(temp.charAt(k+1));
					if( oPos != -2 || oPos !=-3 || oPosNext!=-2 || oPosNext!=-3){
						System.out.println(temp);
						System.out.println("oPos: " + oPos + " oPosNext: " + oPosNext);
						System.out.println(oPosNext-oPos);
						if ((Math.abs(oPosNext-oPos))!=1){
							System.out.println("Comma added");
							temp = temp.substring(0, k+1) + ", " + temp.substring(k+1);
						}
						
					}
				}
				System.out.println(temp);
				al.set(i, temp);
			}
			ArrayList<String> formatted = new ArrayList<String>();
			for(int i = 0; i!= al.size(); i++){
				formatted.add("{" + al.get(i)+ "}");
			}
	}
		
		private static void calcRem(String str,int mode) {
			if(mode==0){
				if(str.length()==1)
					return;
				String temp = str.substring(1);
				allRem.add(temp);
				calcRem(temp,mode);
			}
			else if(mode==1){
				if(str.length()==1)
					return;
				String temp = str.substring(0,str.length()-1);
				allRem.add(temp);
				calcRem(temp,mode);
			}
		}

		public static ArrayList<String> cAarrayTosArray(ArrayList<ArrayList<Character>> chars){
			ArrayList<String> aOfC = new ArrayList<String>();
			for(int i = 0; i!= chars.size(); i++){
			StringBuilder result = new StringBuilder(chars.get(i).size());
			for (Character c : chars.get(i)) {
			  result.append(c);
			}
			aOfC.add( result.toString());
			}
			return aOfC;
		}
	
		public static void subString(ArrayList<Character> s){

			for (int i = 1; i!= s.size()-1;i++){//for every subset single delete
				ArrayList<Character> temp = (ArrayList<Character>) s.clone();
				temp.remove(i);
				
				allSubs.add(temp);
				subString(temp);
			}
		}
}
