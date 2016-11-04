package com.pega.interview.chipcard;

import java.util.Date;

/**
 * <p>
 * OV-chipkaart is used across the Netherlands to pay for the travel by public transport.<br/>
 * The usage of the card is very simple: <br/>
 * When you enter the transport you have to swipe your card over the reader (this procedure is called check-in).<br/>
 * When you leave the transport you have to swipe the card again to notify the system that travel is ended (this procedure is called check-out).
 * </p>
 *
 * <p>
 * The cost of the travel is calculated using the following rules:<br/><br/>
 * <i>cost = basic_rate+kilometer_rate*traveled_kilometers</i><br/>
 * <ul>
 *     <li><i>basic_rate</i> is a "boarding" fee equal to 0.78 EUR. If you transfer (get out of a bus and take another one) within 35 minutes, you do not have to pay the basic rate again.<br/></li>
 *     <li><i>kilometer_rate</i> is a payment for one kilometer equal to 0.11 EUR.</li>
 *     <li><i>traveled_kilometers</i> is a number of traveled kilometers</li>
 *     <li>In case you did check-in but did not check-out 4 EUR is taken</li>
 *     <li>In case you checked-in and checked-out at the same station within allowed transfer time, money are not taken</li>
 *     <li>One day a year all travels are free of cost. This day is 30th of April</li>
 * </ul>
 * </p>
 *
 */
public interface ChipCard {

    final double BASIC_RATE = 0.78;
    final double KILOMETER_RATE = 0.11;
    final int MAX_TRANSFER_TIME = 35;

    /**
     * Adds money to the chip-card.
     * @param amount of money to put
     * @return status string:
     *            <ul>
     *                 <li><i>OK new_balance</i> - if everything is ok</li>
     *            </ul>
     * @throws IllegalStateException in case some error occurred
     */
    String addMoney(double amount);

    /**
     * Gets current balance on the card.
     * @return current balance
     */
    double getBalance();

    /**
     * "Check-in" event. Happens when card holder enters the transport<br/>
     *
     * @param date of the event
     * @return status string:
     *             <ul>
     *                 <li><i>OK</i> - if traveling started</li>
     *                 <li><i>TRANSFER OK</i> - if transfer started</li>
     *                 <li><i>NOT ENOUGH MONEY</i> - if balance is negative</li>
     *             </ul>
     * @throws IllegalStateException in case some error occurred
     */
    String checkin(Date date);

    /**
     * "Check-out" event. Happens when card holder leaves the transport<br/>
     *
     * @param date of the event
     * @param distance traveled in kilometers
     * @return status string:
     *             <ul>
     *                 <li><i>GOOD BYE. You have payed X EUR, Y EUR left on the card.</i></li>
     *             </ul>
     * @throws IllegalStateException in case some error occurred
     */
    String checkout(Date date, int distance);
}

	