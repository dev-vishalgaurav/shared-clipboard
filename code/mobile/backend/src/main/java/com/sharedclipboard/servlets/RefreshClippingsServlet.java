package com.sharedclipboard.servlets;

import com.sharedclipboard.MessagingEndpoint;
import com.sharedclipboard.data.Clipping;
import com.sharedclipboard.data.ClippingsDataStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/29/16.
 */
public class RefreshClippingsServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String passcode = req.getParameter("passcode");

        //grabs clippings time
        String clippingsTimes = req.getParameter("clippings");
        String[] clippingTimesArray = clippingsTimes.split(":");

        HashMap<String, Boolean> dateTimeMap = new HashMap<String, Boolean>();

        for(String time : clippingTimesArray) {
            dateTimeMap.put(time, true);
        }

        //fetch all clippings for a given passcode
        List<Clipping> clippings = ClippingsDataStore.getAllClippingsWithPasscode(passcode);

        for(Clipping c : clippings) {
            //if the data store has something the phone doesn't, send it
            if(!dateTimeMap.containsKey(Long.toString(c.getDateTime())))  {
                MessagingEndpoint messagingEndpoint = new MessagingEndpoint();
                String message = Long.toString(c.getDateTime()) + "_" + c.getText();
                messagingEndpoint.sendMessage(message, passcode);
            }
        }

    }
}
