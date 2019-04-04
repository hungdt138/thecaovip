package com.hieugie.banthe.web.rest.errors;

public class PhoneNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "phoneexists");
    }
}
