import java.util.*;
class Frequency {
    Frequency (){

    }
    char[] charArray = new char [3];

    int[] charfreq = new int [3];
     void frequency(String abc) {
        Map<Character, Integer> freq = new HashMap<Character, Integer>();
        for (char ch : abc.toCharArray()){
        if (!freq.containsKey(ch)){
            freq.put(ch,1);
        }
        else {
            int value = freq.get(ch);
            freq.put(ch,value+1);
        }
    }
       
        int i=0;
    System.out.println(freq);
    for(Map.Entry<Character, Integer> container : freq.entrySet()){
        charArray[i]= container.getKey();
        System.out.println("Inside loop "+ charArray[i]);
        charfreq[i]= container.getValue();
        System.out.println("Inside loop "+ charfreq[i]);
        i++;
    }
    
}
}
class HuffmanTree extends Node {
    Node root;
    void InsertTree(){
     
}
}
class Practice {
    public static void main(String[] args) {
        String abc = "aaaaabbbcc";
        Frequency obj = new Frequency();
        obj.frequency(abc);
    }

    
}