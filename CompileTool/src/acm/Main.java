package acm;

import java.util.Random;

//60 5 37 40 16 30 18
public class Main {
	static int count=0;
    public static void main(String[] args) {
    	Random random=new Random();
      for(int i=0;i<10;i++){
    	  System.out.println(random.nextInt(10)+1);
      }
    }
}
