package com.hieugie.banthe.security;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyRecaptcha {

    public static final String url = "https://www.google.com/recaptcha/api/siteverify?";
    public static final String secret = "6LeWZJMUAAAAAKELD2QSkSXoAv-APSM53WJcc0lQ";

    public static boolean verify(String gRecaptchaResponse, String remoteIp) throws IOException, JSONException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        // Build a URL
        String postParams = url + "secret=" + secret + "&response=" + gRecaptchaResponse + "&remoteip" + remoteIp;
        URL url = new URL(postParams);

        // Read from the URL
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine();
        }
        scan.close();

        // Get JSON object
        JSONObject obj = new JSONObject(str);
        return obj.getBoolean("success");
    }
}
