package com.yifat.finalproject.Helpers;

import java.net.InetAddress;

public class GeneralHelper {

    // Checking if Internet is available
    public static boolean isInternetAvailable() {
        try {
            InetAddress inetAddress = InetAddress.getByName("google.com"); //You can replace it with your name

            if (inetAddress.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
