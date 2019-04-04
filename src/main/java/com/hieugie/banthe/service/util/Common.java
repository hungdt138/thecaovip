package com.hieugie.banthe.service.util;

import com.hieugie.banthe.domain.Authority;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.security.AuthoritiesConstants;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Common {
    public static String createMd5(String before) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(before.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter
                .printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAdmin(User user) {
        for(Authority authority: user.getAuthorities()) {
            if (authority.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public static Long getLong(Object object) {
        return object != null ? ((BigDecimal) object).longValue() : null;
    }

    public static Integer getInteger(Object object) {
        return object != null ? ((BigDecimal) object).intValue() : null;
    }

    public static BigDecimal getBigDecimal(Object object) {
        return object != null ? (BigDecimal) object : null;
    }
}
