package com.pega.interview.chipcard;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Conese on 04/11/2016.
 */
public class ChipCardPG implements ChipCard{

    boolean isTravelling = true;
    double money = 0;

    TimerTask checkTask;
    Timer timer;
    Date lastCheckin;

    public ChipCardPG(){
        timer = new Timer();
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
            if (isTravelling){
               return "TRANSFER OK";
            }
            if (money < BASIC_RATE ){
                return "NOT ENOUGH MONEY";
            }
            lastCheckin = date;
            isTravelling = true;
            checkTask = new CheckTask(this);
            timer.schedule(checkTask,new Date(date.getTime()+MAX_TRANSFER_TIME*MILLISECOND*SECOND));
            return "OK";
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
                }else if (time>MAX_TRANSFER_TIME){
                    spent = 4;
                }else{
                    spent = BASIC_RATE + distance*KILOMETER_RATE;
                }
                money = money - spent;
                lastCheckin = null;
                isTravelling = false;
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
