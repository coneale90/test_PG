package main.java.com.pega.interview.chipcard;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Conese on 04/11/2016.
 */
public class ChipCardPG implements ChipCard {

    long id;
    boolean isTravelling;
    double money;
    double toPay;

    TimerTask checkTask;
    Timer timer;
    Date lastCheckin;

    public ChipCardPG(long id){
        money = 0;
        this.id = id;
        isTravelling = false;
        toPay=0;
    }

    public String addMoney(double amount) {
        if (amount < 0){
            throw new IllegalStateException("Impossible to add negative money");
        }
        money = money + amount;
        return "OK " + money;
    }

    public double getBalance() {
        return money;
    }

    public String checkin(Date date) {
        try{
            if (money < BASIC_RATE ){
                return "NOT ENOUGH MONEY";
            }
            String message;
            //check if checking is done for the first time or is done inside the MAX_TRANSFER_TIME
            boolean checkinNotInTime = lastCheckin == null ? true : date.getTime()-lastCheckin.getTime() > MAX_TRANFERT_TIME_MILLI;
            if (!isTravelling || checkinNotInTime){
                toPay = toPay + BASIC_RATE;
                message = "OK";
            }else{
                message = "TRANSFER OK";
            }
            lastCheckin = date;
            isTravelling = true;
            timer = new Timer();
            checkTask = new CheckTask(this);
            timer.schedule(checkTask,new Date(date.getTime()+MAX_TRANFERT_TIME_MILLI));
            return message;
        }catch(Exception e){
            throw new IllegalStateException("Something goes wrong "+e);
        }
    }

    public String checkout(Date date, int distance) {
        double spent;
        try{
            if (isTravelling){
                timer.cancel();
                long time = (date.getTime()-lastCheckin.getTime())/(MILLISECOND*SECOND);
                if (date.getDay() == 30 && date.getMonth() == 4){
                    spent = 0;
                }else if (distance == 0 && time < ALLOWED_TIME){
                    //checkout from the same station in a short time, no money is taken
                    spent = 0;
                }else if (time>MAX_TRANSFER_TIME){
                    //after you passed the threshold you have to pay 4EUR
                    spent = 4;
                }else{
                    spent = distance*KILOMETER_RATE + toPay;
                }
                money = money - spent;
                isTravelling = false;
                toPay = 0;
                return "GOOD BYE. You have payed " + spent + " EUR, " + money + " EUR left on the card";
            }
        }catch(Exception e){
            throw new IllegalStateException("Something goes wrong "+e);
        }
        return "";
    }

    public void changeTravelStatus(boolean travelStatus){
        this.isTravelling = travelStatus;
    }


    public void takeMoney() {
        //the max time is passed before the checkout, take the money.
        money = money - COST_MAX;
    }
}
