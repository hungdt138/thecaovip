package com.hieugie.banthe.service.util;

public class SMSUtils {
    private static final String FROM_NUMBER = "+12055093833";
    private static final String COUNTRY_CODE = "+84";
    public static final String SECRET_CODE = "hieugie";
    private static final String OTP_TEMPLATE = "Mã xác nhận của bạn là - ";

    public static boolean sendOtp(String phoneNumber, String token) {
//        Twilio.init(Constants.ACCOUNT_SID, Constants.AUTH_TOKEN);
//        StringBuilder sms = new StringBuilder(OTP_TEMPLATE);
//
//        try {
//            sms.append(token);
//            Message.creator(
//                new com.twilio.type.PhoneNumber(phoneNumber),
//                new com.twilio.type.PhoneNumber(FROM_NUMBER),
//                sms.toString())
//                .create();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;
    }
}
