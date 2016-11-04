package main.java.com.pega.interview.chipcard;

import java.util.TimerTask;

/**
 * Created by Conese on 04/11/2016.
 */
public class CheckTask extends TimerTask {
    ChipCard card;

    public CheckTask(ChipCard pg){
        card = pg;
    }

    public void run() {
        card.takeMoney();
        card.changeTravelStatus(false);
    }
}