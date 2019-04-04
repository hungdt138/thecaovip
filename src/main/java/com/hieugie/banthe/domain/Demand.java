package com.hieugie.banthe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.hieugie.banthe.domain.enumeration.NhaMang;

/**
 * A Demand.
 */
@Entity
@Table(name = "demand")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Demand extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_account")
    private String account;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private NhaMang type;

    @Column(name = "charged_amount", precision = 10, scale = 2)
    private BigDecimal chargedAmount;

    @ManyToOne
    private Bill bill;

    @Column(name = "name")
    private String name;

    @Column(name = "service_type")
    private Integer serviceType;

    /**
     * 1 - Số điện thoại sai định dạng
     * 2 - số điện thoại đã vượt quá số lần nạp sai
     * 3 - khóa nạp
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "invalid_date")
    private LocalDate invalidDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public Demand account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = Strings.isNullOrEmpty(account) ? null : account.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Demand amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public NhaMang getType() {
        return type;
    }

    public Demand type(NhaMang type) {
        this.type = type;
        return this;
    }

    public void setType(NhaMang type) {
        this.type = type;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public Demand chargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
        return this;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getName() {
//        return name != null ? new String(BaseEncoding.base64().decode(name)) : null;
        return name;
    }

    public void setName(String name) {
//        this.name = BaseEncoding.base64().encode(name.getBytes());
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(LocalDate invalidDate) {
        this.invalidDate = invalidDate;
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
        Demand demand = (Demand) o;
        if (demand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Demand{" +
            "id=" + getId() +
            ", account='" + getAccount() + "'" +
            ", amount=" + getAmount() +
            ", type='" + getType() + "'" +
            ", chargedAmount=" + getChargedAmount() +
            "}";
    }

    public Demand(String account, BigDecimal amount) {
        this.account = account;
        this.amount = amount;
    }

    public Demand() {
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }
}
