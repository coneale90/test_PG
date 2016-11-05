package main.java.com.pega.interview.chipcard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

/**
 * Created by Conese on 05/11/2016.
 */
public class ChipCardMain {

    public static void main(String [ ] args){
        boolean end=false;
        int cmd;
        ChipCard card = new ChipCardPG(1);
        Random rm = new Random();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Created new Card\n");
        while (!end){
            System.out.print("Insert command:\n1 - Add Money\n2 - Check-in\n3 - Check-out\n0 - End\n");
            String result = "";
            try {
                cmd = Integer.parseInt(br.readLine());
                switch (cmd){
                    case 0:
                        result = "Bye Bye";
                        end = true;
                        break;
                    case 1:
                        System.out.print("Insert amount to charge: \t");
                        int amount = Integer.parseInt(br.readLine());
                        result = card.addMoney(amount);
                        break;
                    case 2:
                        Date dtIn = new Date(System.currentTimeMillis());
                        System.out.println("Check-in at: "+dtIn.toString());
                        result = card.checkin(dtIn);
                        break;
                    case 3:
                        Date dtOut = new Date(System.currentTimeMillis());
                        int distance = rm.nextInt(10);
                        System.out.println("Check-out at: "+dtOut.toString() + " with distance: "+distance);
                        result = card.checkout(dtOut, distance);
                        break;
                    default:
                        result = "Wrong command";
                }

            } catch (IOException e) {
                //
            }
            System.out.print(result+"\n");
        }
    }
}
