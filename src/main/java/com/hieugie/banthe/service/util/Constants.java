package com.hieugie.banthe.service.util;

import java.math.BigDecimal;

public interface Constants {
    String ACCOUNT_SID =
        "AC8d48bd3e131d0f9effc2536f2be5696b";
    String AUTH_TOKEN =
        "84f3b64f3464bb422c7d38473b96df3e";

    String PRIVATE_KEY = "b3daf67d5720c469eac5060a90fa6330";
    int BATCH_SIZE = 512;

    interface MENH_GIA_THE {
        BigDecimal MOT_TRIEU = BigDecimal.valueOf(1000000);
        BigDecimal NAM_TRAM = BigDecimal.valueOf(500000);
        BigDecimal HAI_TRAM = BigDecimal.valueOf(200000);
        BigDecimal MOT_TRAM = BigDecimal.valueOf(100000);
        BigDecimal NAM_CHUC = BigDecimal.valueOf(50000);
        BigDecimal HAI_CHUC = BigDecimal.valueOf(20000);
        BigDecimal MUOI_NGHIN = BigDecimal.valueOf(10000);

    }

    interface TrumTheApi {
        String TRA_TRUOC = "http://trumthe2.gachcuoctrasau.com//Home/TopUpV2";
        String TRA_SAU = "http://trumthe2.gachcuoctrasau.com//Home/TopUp";
    }

    interface TrumTheMessage {
        String DA_SU_DUNG = "Thẻ cào không hợp lệ hoặc đã được sử dụng.";
        String QUA_SO_LAN_NAP_SAI = "Nạp sai quá số lần qui định trong ngày.";
        String NAP_KHONG_THANH_CONG = "Nạp tiền không thành công.";
    }

    interface GameUrl {
        String CREATE_BILL_1 = "http://api.card.amtech.info/api/GPay/PartnerCreateOrder1";
        String CREATE_BILL_2 = "http://api.card.amtech.info/api/GPay/PartnerCreateOrder2";
        String CALL_BACK_API = "http://api.card.amtech.info/api/GPay/callback2api";
        String CANCEL_BILL = "http://api.card.amtech.info/api/GPay/PartnerCancelOrder";
    }

    interface GameAuth {
        String USERNAME = "PTHG2";
        String PASSWORD = "39c8e9953fe8ea40ff1c59876e0e2f28";
    }

    interface ChargeStatus {
        int OK = 1;
        int ERROR1 = -1; // Thẻ cào không hợp lệ hoặc đã được sử dụng
        int ERROR2 = -2; // gạch thẻ thất bại
        int ERROR3 = -3; // thẻ cào có mệnh giá < hơn mệnh giá nhập vào
    }
}
