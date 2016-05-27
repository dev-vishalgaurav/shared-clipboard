package com.sharedclipboard.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/25/16.
 */
public class HomeServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        PrintWriter writer = resp.getWriter();
        writer.write("<html>\n");

        String valid = req.getParameter("passcode");
        if(valid != null && valid.equals("invalid"))
            writer.write("<h3>Invalid Web Id, Please try again</h3>");

        writer.write("<form action=\"display.do\" method=\"get\">\n" +
                "    Web Id:<br>\n" +
                "    <input type=\"text\" name=\"id\">\n" +
                "    <input type=\"submit\" value=\"Submit\">\n" +
                "    </form>");
        writer.write("</html>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }


}
