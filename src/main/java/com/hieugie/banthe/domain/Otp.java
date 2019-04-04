package com.hieugie.banthe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import com.hieugie.banthe.domain.enumeration.Action;

/**
 * A Otp.
 */
@Entity
@Table(name = "otp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Otp  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "action")
    private Action action;

    @ManyToOne
    @JsonIgnoreProperties("otps")
    private User user;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean resend;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Otp code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public Otp expiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
        return this;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Action getAction() {
        return action;
    }

    public Otp action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public Otp user(User user) {
        this.user = user;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getResend() {
        return resend;
    }

    public void setResend(Boolean resend) {
        this.resend = resend;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Otp otp = (Otp) o;
        if (otp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), otp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Otp{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", expiredDate='" + getExpiredDate() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
