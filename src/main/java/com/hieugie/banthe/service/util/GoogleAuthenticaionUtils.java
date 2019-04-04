package com.hieugie.banthe.service.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GoogleAuthenticaionUtils {

    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
    }

    public static String createQrCode(String user, String host, String secret) {
        String data = "otpauth://totp/" + user + "@" + host + "?secret=" + secret;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return new String(Base64.encodeBase64(outputStream.toByteArray()), StandardCharsets.UTF_8);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //To check whether generated google auth key is authenticated or not
    public static boolean performAuthentication(String value, String secretKey, long lastVerifiedTime, int lastUsedPassword) {
        final GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Integer totp = Integer.valueOf((value.equals("") ? "-1" : value));
        int WINDOW_SIZE = 10;
        boolean unused = isUnusedPassword(totp, WINDOW_SIZE, lastVerifiedTime, lastUsedPassword);
        boolean matches = gAuth.authorize(secretKey, totp);
        return (unused && matches);
    }

    private static long KEY_VALIDATION_INTERVAL_MS = TimeUnit.SECONDS.toMillis(30);

    private static boolean isUnusedPassword(int password, int windowSize, long lastVerifiedTime, int lastUsedPassword) {
        long now = new Date().getTime();
        long timesLotNow = now / KEY_VALIDATION_INTERVAL_MS;
        int forwardTimesLots = ((windowSize - 1) / 2);
        long timesLotThen = lastVerifiedTime / KEY_VALIDATION_INTERVAL_MS;
        return password != lastUsedPassword || timesLotNow > timesLotThen + forwardTimesLots;
    }

    public static String createRandomKey() {
        final GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey googleAuthkey = gAuth.createCredentials();
        return googleAuthkey.getKey();
    }
}
