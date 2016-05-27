package com.sharedclipboard.servlets;

import com.sharedclipboard.MessagingEndpoint;
import com.sharedclipboard.data.Clipping;
import com.sharedclipboard.data.ClippingsDataStore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/26/16.
 */
public class AddClippingsServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        handleRequest(req,resp);
    }
    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        //grabs passcode and type
        String passcode = req.getParameter("passcode");
        String inputType = req.getParameter("input_type");
        String text = req.getParameter("clipping");

        long time = System.currentTimeMillis() / 1000;
        Clipping clipping = new Clipping(time, text);
        ClippingsDataStore.addClipping(clipping, passcode);

        //if input type is desktop, forward the info to the devices
        if(inputType.equals("desktop")) {
            MessagingEndpoint messagingEndpoint = new MessagingEndpoint();
            String message = Long.toString(time) + "_" + text;
            messagingEndpoint.sendMessage(message, passcode);
        }
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        handleRequest(req,resp);
    }

}
