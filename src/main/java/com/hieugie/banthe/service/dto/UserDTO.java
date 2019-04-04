package com.hieugie.banthe.service.dto;

import com.google.common.base.Strings;
import com.hieugie.banthe.config.Constants;

import com.hieugie.banthe.domain.Authority;
import com.hieugie.banthe.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 255)
    private String fullName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 6)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    @NotBlank
    private String phoneNumber;

    private Integer feePercentLv1;

    private Integer feePercentLv2;

    private Integer feePercentLv1b;
    private Integer feePercentLv2b;


    private BigDecimal amount;

    private BigDecimal availableAmount;

    private BigDecimal chargeAmount;

    private Boolean isAuth;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.phoneNumber = user.getPhoneNumber();
        this.feePercentLv1 = user.getFeePercentLv1();
        this.feePercentLv2 = user.getFeePercentLv2();
        this.feePercentLv1b = user.getFeePercentLv1b();
        this.feePercentLv2b = user.getFeePercentLv2b();
        this.amount = user.getAmount();
        this.isAuth = !Strings.isNullOrEmpty(user.getPrivateKey());
        this.availableAmount = user.getAvailableAmount();
        this.chargeAmount = user.getChargeAmount();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getFeePercentLv1() {
        return feePercentLv1;
    }

    public void setFeePercentLv1(Integer feePercentLv1) {
        this.feePercentLv1 = feePercentLv1;
    }

    public Integer getFeePercentLv2() {
        return feePercentLv2;
    }

    public void setFeePercentLv2(Integer feePercentLv2) {
        this.feePercentLv2 = feePercentLv2;
    }

    public Integer getFeePercentLv1b() {
        return feePercentLv1b;
    }

    public void setFeePercentLv1b(Integer feePercentLv1b) {
        this.feePercentLv1b = feePercentLv1b;
    }

    public Integer getFeePercentLv2b() {
        return feePercentLv2b;
    }

    public void setFeePercentLv2b(Integer feePercentLv2b) {
        this.feePercentLv2b = feePercentLv2b;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", fullName='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            '}';
    }
}
