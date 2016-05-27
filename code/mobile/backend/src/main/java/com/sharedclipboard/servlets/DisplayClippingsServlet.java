package com.sharedclipboard.servlets;

import com.sharedclipboard.data.Clipping;
import com.sharedclipboard.data.ClippingsDataStore;
import com.sharedclipboard.data.UserDataStore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/25/16.
 */
public class DisplayClippingsServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String id = req.getParameter("id");

        if(id == null || !UserDataStore.isValidPasscode(id)) {
            //redirects to the main page while flagging the invalid passcode
            resp.sendRedirect("/home.do?passcode=invalid");
        }

        PrintWriter writer = resp.getWriter();
        writer.write("<html>\n");
        writer.write("<h2>Welcome To Your Clippings</h2>");

        writer.write(getTableHTML(id));
        writer.write("</html>");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doPost(req, resp);
    }

    private String getTableHTML(String passcode) {
        //fetch all clippings for a given passcode
        ArrayList<Clipping> clippings = ClippingsDataStore.getAllClippingsWithPasscode(passcode);

        //create a string builder
        StringBuilder builder = new StringBuilder();

        builder.append("<table style=\"border:1px solid black\"><tr>");
        builder.append("<td style=\"border:1px solid black\">Date</td>");
        builder.append("<td style=\"border:1px solid black\">Text</td></tr>");


        //loop over clippings
        for(Clipping clipping : clippings) {
            //create the HTML for the clipping row
            builder.append("<tr><td style=\"border:1px solid black\">" + clipping.getDateString() + "</td>");
            builder.append("<td style=\"border:1px solid black\">" + clipping.getText() + "</td></tr>");
        }

        builder.append("</table>");

        return builder.toString();
    }
}
