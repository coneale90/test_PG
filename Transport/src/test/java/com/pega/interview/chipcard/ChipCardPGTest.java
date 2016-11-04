package test.java.com.pega.interview.chipcard;

import main.java.com.pega.interview.chipcard.ChipCard;
import main.java.com.pega.interview.chipcard.ChipCardPG;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

/**
 * Created by Conese on 04/11/2016.
 */
public class ChipCardPGTest {

    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testAddMoney(){
        ChipCard chipCard = new ChipCardPG(1);

        chipCard.addMoney(100);
        Assert.assertTrue("The amount as to be equal", chipCard.getBalance() == 100);
    }

    @Test(expected=IllegalStateException.class)
    public void testAddMoneyNegative(){
        ChipCard chipCard = new ChipCardPG(1);

        chipCard.addMoney(-100);
    }

    @Test
    public void checkin() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        String message = chipCard.checkin(dt);
        Assert.assertTrue( message.equals("OK"));
        Assert.assertTrue(chipCard.getBalance() == 100);

    }

    @Test
    public void doubleCheckinInTime() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        chipCard.checkin(dt);
        String message = chipCard.checkin(dt);

        Assert.assertTrue( message.equals("TRANSFER OK"));
        //pay only once the BASIC_RATE
        Assert.assertTrue(chipCard.getBalance() == 100);
    }

    @Test
    public void doubleCheckinNOTInTime() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        chipCard.checkin(dt);
        Date dt2 = new Date(System.currentTimeMillis() + 1001*60*ChipCard.MAX_TRANSFER_TIME);
        String message = chipCard.checkin(dt2);

        //pay twice the BASIC_RATE
        Assert.assertTrue( message.equals("OK"));
        Assert.assertTrue(chipCard.getBalance() == 100);
    }

    @Test
    public void checkout() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        chipCard.checkin(dt);
        Date dt2 = new Date(System.currentTimeMillis() + 5000);
        chipCard.checkout(dt2,1);
        double totalSpent = ChipCard.BASIC_RATE + ChipCard.KILOMETER_RATE;
        double amountRemaining = 100 - totalSpent;
        Assert.assertTrue(chipCard.getBalance() == amountRemaining);
    }

    @Test
    public void checkoutNotInTime() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        chipCard.checkin(dt);
        Date dt2 = new Date(System.currentTimeMillis() + 2*ChipCard.MAX_TRANFERT_TIME_MILLI);
        chipCard.checkout(dt2,1);
        //after you passed the threshold you have to pay 4EUR
        double totalSpent = 4;
        double amountRemaining = 100 - totalSpent;
        Assert.assertTrue(chipCard.getBalance() == amountRemaining);
    }

    @Test
    public void checkoutInSamePlace() throws Exception {
        ChipCard chipCard = new ChipCardPG(1);
        chipCard.addMoney(100);
        Date dt = new Date(System.currentTimeMillis());
        chipCard.checkin(dt);
        Date dt2 = new Date(System.currentTimeMillis() + 2*ChipCard.MAX_TRANFERT_TIME_MILLI);
        chipCard.checkout(dt2,1);
        //after you passed the threshold you have to pay 4EUR
        double totalSpent = 4;
        double amountRemaining = 100 - totalSpent;
        Assert.assertTrue(chipCard.getBalance() == amountRemaining);
    }


}