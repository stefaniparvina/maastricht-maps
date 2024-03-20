import Mathfiles.Distance;
import Mathfiles.WalkSpeed;

import java.util.Scanner;

public class Start extends WalkSpeed{
     public static void main(String[] args){

        System.out.println("Zip code start: ");
        Scanner scanner = new Scanner(System.in);
        String zipStart = scanner.next().toUpperCase();

        System.out.println("Zip code end: ");
        String zipEnd = scanner.next().toUpperCase();

        WalkSpeed time = new WalkSpeed();
        System.out.println(time.calculateWalkingTime(zipStart, zipEnd));
     }
}
