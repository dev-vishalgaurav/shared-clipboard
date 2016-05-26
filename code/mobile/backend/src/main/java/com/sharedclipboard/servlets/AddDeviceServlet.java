package com.sharedclipboard.servlets;

import com.sharedclipboard.data.DeviceDataStore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Girouard23 on 5/26/16.
 */
public class AddDeviceServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        //grabs passcode and deviceKey
        String passcode = req.getParameter("passcode");
        String deviceKey = req.getParameter("reg_id");

        DeviceDataStore.insertDevice(deviceKey, passcode);
    }
}
