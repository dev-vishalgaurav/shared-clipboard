package com.sharedclipboard.servlets;

import com.sharedclipboard.data.UserDataStore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        //gets the id from the passed parameter and deletes the entity associated with that id
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String loginAttempt = UserDataStore.correctUsernamePassword(username, password);
        //failed login
        if(loginAttempt.equals("")) {
            //try creating a new user
            loginAttempt = UserDataStore.createNewUser(username, password);
        }

        //if failed to login or create new account return 202
        if(loginAttempt.equals(""))
            resp.setStatus(202);
        else {
            resp.setStatus(200);
            resp.addHeader("passcode", loginAttempt);
        }

    }
}
