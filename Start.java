import Mathfiles.Distance;

import java.util.Scanner;

public class Start extends Distance{
     public static void main(String[] args){

         Start start = new Start();
         start.callApp();
     }
     private void callApp(){

         System.out.println("Zip code start: ");
         Scanner scanner = new Scanner(System.in);
         String zipStart = scanner.next().toUpperCase();

         System.out.println("Zip code end: ");
         String zipEnd = scanner.next().toUpperCase();

         double distance = calculateDistance(zipStart, zipEnd);
         System.out.println(distance);

     }


}
