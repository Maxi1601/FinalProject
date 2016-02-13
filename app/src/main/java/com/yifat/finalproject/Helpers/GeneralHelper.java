package com.yifat.finalproject.Helpers;

import java.net.InetAddress;

/**
 * Created by Yifat on 2/10/16.
 */
public class GeneralHelper {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
