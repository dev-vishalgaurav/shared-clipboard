package com.sharedclipboard.servlets;

import com.sharedclipboard.data.Clipping;
import com.sharedclipboard.data.ClippingsDataStore;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/31/16.
 */
public class LatestClippingServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        //grabs passcode
        String passcode = req.getParameter("passcode");

        Clipping clipping = ClippingsDataStore.getLatestClipping(passcode);

        if(clipping == null)
            return;

        PrintWriter writer = resp.getWriter();
        writer.write(clipping.getText());
        resp.addHeader("Clipping", clipping.getText());
        resp.addHeader("clipping", clipping.getText());
    }
}
