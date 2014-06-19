/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.time;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zenology
 */
public class NTPTime {

    public static Calendar getNTPCalendar() {
        try {
            String timeString = NtpMessage.timestampToString(NtpClient.GetNTPTime().receiveTimestamp);
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date d = df.parse(timeString);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c;
        } catch (UnknownHostException ex) {
            Logger.getLogger(NTPTime.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NTPTime.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(NTPTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * NTPTime.reformatTime(Calendar.getInstance(), 4 , 0, 30) <br/> Delay for 4 hours 0 mins and 30 secs
     * @param hostTime
     * @param hour how many delay in hour
     * @param minute how many delay in minute 
     * @param second how many delay in second
     * @return 
     */
    public static Calendar reformatTime(Calendar hostTime, int hour, int minute, int second) {
        hostTime.set(Calendar.MILLISECOND, 0);
        long host = hostTime.getTimeInMillis();
        long format = (second * 1000) + (minute * 60 * 1000) + (hour * 60 * 60 * 1000);

        long formatTime = ((long) Math.floor(host / format)) * format;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(formatTime);
        return c;
    }
}
