import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Apriori {
	static String arr[];
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		Map<String, Integer> candidateSet = new TreeMap<>();
		Set<String> items = new HashSet<>();
		System.out.println("Enter the number of rows in the dataset");
		int n = sc.nextInt();
		arr = new String[n];
		sc.nextLine();					//To flush the buffer
		System.out.println("Enter the dataset.\nSeparate the values with spaces.");
		for(int i=0; i<n; ++i){
			arr[i] = sc.nextLine();
			String temp[] = arr[i].split("\\s+");
			for(int j=0; j<temp.length; ++j){
				if(candidateSet.containsKey(temp[j])){
					int something = (int) candidateSet.get(temp[j]);
					something += 1;
					candidateSet.put(temp[j], something);
				} else{
					candidateSet.put(temp[j], 1);
				}
			}
		}
		
		//Separated into single elements
		
		System.out.println("Enter the minimum support count");
		int msc = sc.nextInt();
		int l = 0;
		int i=2;
		
		for(Map.Entry<String, Integer> entry: candidateSet.entrySet()){
			if(entry.getValue()<msc){						//Remove the entries with count < minimum support count
				String key = entry.getKey();
				items.add(key);
				l++;
			}
		}
		for(Object s : items){
			candidateSet.remove(s.toString());
		}
		
		while(true){
			Map<String, Integer> tempMap = new HashMap<>();
			
			Set<String> keys = candidateSet.keySet();
			Iterator it1 = keys.iterator();
			i = 2;
			while(it1.hasNext()){
				String temp = it1.next().toString();
				Iterator it2 = keys.iterator();
				int r=1;
				while(r<i){						
					if(it2.hasNext())
						it2.next();
					++r;
				}
				String newKey=null;
				while(it2.hasNext()){
					String it2Value = it2.next().toString();
					newKey = temp + it2Value;
					String candidate = removeDuplicates(newKey);
					//Count occurrences of new String
					int countOfNewKey = countOccurrences(candidate);
					if(countOfNewKey<msc){
						continue;
					} else
						tempMap.put(candidate, countOfNewKey);
				}
				++i;
			}
			
			if(!tempMap.isEmpty()){
				candidateSet.clear();
				candidateSet.putAll(tempMap);
				tempMap.clear();
			}else
				break;
			
		}
		//System.out.println("Out of the while loop");
		for(Map.Entry<String, Integer> entry: candidateSet.entrySet()){
			
			char key[] = entry.getKey().toCharArray();					//one key of the set
			String positions = "0";
			for(int m=0; m<key.length-1; ++m)
				positions += "0";
			for(int m=0; m<Math.pow(2, (double)key.length)-2; ++m){			//minus 2 since 000 is gone and i don't want 111
				positions = addOneToBinary(positions);
				char arr[] = positions.toCharArray();
				String zero = null;
				String one = null;
				boolean start0=false, start1=false;
				for(int x = 0; x<arr.length; ++x){
					if(arr[x]=='0'){
						if(start0)
							zero += key[x];
						else{
							zero = String.valueOf(key[x]);
							start0 = true;
						}
					} else {
						if(start1)
							one += key[x];
						else{
							one = String.valueOf(key[x]);
							start1 = true;
						}
					}
				}
				int countOne = countOccurrences(one);
				System.out.println(one+" => "+zero+" with confidence = "+(((double)entry.getValue()/(double)countOne)*100));
			}
		}
		
	}
	
	public static String addOneToBinary(String prev){
		int index = prev.lastIndexOf('0');
		char arr[] = prev.toCharArray();
		arr[index] = '1';
		for(int i = index+1; i<arr.length; ++i){
			if(arr[i] == '0')
				arr[i] = '1';
			else
				arr[i] = '0';
		}
		String result = String.valueOf(arr);
		//System.out.println(result);
		return result;
	}
	
	
	public static int countOccurrences(String newKey){
		int count = 0;
		char keySplit[] = newKey.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			int check = 0;
			for(int j=0; j < keySplit.length; ++j){
				if(arr[i].lastIndexOf(((Character)keySplit[j]).toString())==-1)
					break;
				else {
					check += 1;
				}
			}
			if(check == keySplit.length)
				++count;
		}
		return count;
	}
	
	public static String removeDuplicates(String something){
		Set<String> set = new HashSet<>();
		for(int i=0; i<something.length(); ++i){
			Character temp = something.charAt(i);
			set.add(temp.toString());
		}
		String temp = null;
		for(String a : set){
			if(temp==null){
				temp = a;
			} else
				temp += a;
		}
		return temp;
	}
	
	
}
