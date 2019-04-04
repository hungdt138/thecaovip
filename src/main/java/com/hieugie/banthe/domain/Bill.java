package com.hieugie.banthe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.hieugie.banthe.domain.enumeration.NhaMang;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bill extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "charged_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal chargedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NhaMang type;

    /**
     * 0 - hủy
     * 1 - đang nạp
     * 2 - đã nạp đủ
     * 3 - Nạp sai quá nhiều lần
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "name")
    private String name;

    @Column(name = "more_fee")
    private Boolean moreFee;

    @Column(name = "high_priority_order")
    private Boolean highPriority;

    @ManyToOne
    @JsonIgnoreProperties("bills")
    private User user;

    /**
     * 1 - Chuyển thành công
     * -1 - chuyển thất bại
     * -2 - sai chữ ký
     * -3 - hủy đơn thất bại
     * -4 - sai định dạng
     */
    @Column(name = "tran_status")
    private Integer tranStatus;

    /**
     * thứ tự ưu tiên đơn hàng
     * 5 mức ưu tiêu:
     * mức 1 là của admin
     * mức 2 là của nạp lẻ, nạp trả trước.
     * mức 3 là nạp trả sau ưu tiên
     * mức 4 là nạp trả sau thường.
     * mức 5 là nạp siêu rẻ.
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 1 - Nạp thẻ trả trước
     * 2 - Nạp thẻ trả sau
     */
    @Column(name = "charge_type")
    private Integer chargeType;

    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "charge_error")
    private Integer chargeError;

    @Column(name = "message")
    private String message;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Bill amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public NhaMang getType() {
        return type;
    }

    public Bill type(NhaMang nhaMang) {
        this.type = nhaMang;
        return this;
    }

    public void setType(NhaMang nhaMang) {
        this.type = nhaMang;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
//        return name != null ? new String(BaseEncoding.base64().decode(name)) : null;
        return name;
    }

    public void setName(String name) {
//        this.name = BaseEncoding.base64().encode(name.getBytes());
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public Boolean getMoreFee() {
        return moreFee;
    }

    public void setMoreFee(Boolean moreFee) {
        this.moreFee = moreFee;
    }

    public Integer getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(Integer tranStatus) {
        this.tranStatus = tranStatus;
    }

    public Boolean getHighPriority() {
        return highPriority;
    }

    public void setHighPriority(Boolean highPriority) {
        this.highPriority = highPriority;
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
        Bill bill = (Bill) o;
        if (bill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", nhaMang='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getChargeError() {
        return chargeError;
    }

    public void setChargeError(Integer chargeError) {
        this.chargeError = chargeError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
