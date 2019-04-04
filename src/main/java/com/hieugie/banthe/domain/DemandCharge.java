package com.hieugie.banthe.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.io.BaseEncoding;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.hieugie.banthe.domain.enumeration.Price;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * A DemandCharge.
 */
@Entity
@Table(name = "demand_charge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DemandCharge  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "serial")
    private String serial;

    @Column(name = "input_value")
    private BigDecimal inputValue;

    @Column(name = "real_value")
    private BigDecimal realValue;

    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "message")
    private String message;

    /**
     * 0- đợi nạp
     * 1- nạp thành công
     * 2- nạp lỗi
     * 3- nạp thành công(nhưng có vấn đề)
     */
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private DemandDtl demandDtl;

    @ManyToOne
    private Demand demand;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NhaMang type;

    @Column(name = "start_date")
    @JsonIgnore
    private Instant startDate;

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

    public DemandCharge code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSerial() {
        return serial;
    }

    public DemandCharge serial(String serial) {
        this.serial = serial;
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public DemandDtl getDemandDtl() {
        return demandDtl;
    }

    public DemandCharge demand(DemandDtl demandDtl) {
        this.demandDtl = demandDtl;
        return this;
    }

    public void setDemandDtl(DemandDtl demandDtl) {
        this.demandDtl = demandDtl;
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
        DemandCharge demandCharge = (DemandCharge) o;
        if (demandCharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demandCharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public BigDecimal getInputValue() {
        return inputValue;
    }

    public void setInputValue(BigDecimal inputValue) {
        this.inputValue = inputValue;
    }

    public BigDecimal getRealValue() {
        return realValue;
    }

    public void setRealValue(BigDecimal realValue) {
        this.realValue = realValue;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DemandCharge{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", serial='" + serial + '\'' +
            ", inputValue=" + inputValue +
            ", realValue=" + realValue +
            ", partnerId=" + partnerId +
            ", requestId='" + requestId + '\'' +
            ", demandDtl=" + demandDtl +
            '}';
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public NhaMang getType() {
        return type;
    }

    public void setType(NhaMang type) {
        this.type = type;
    }

    public String getMessage() {
//        if (message != null) {
//            try {
//                return new String(BaseEncoding.base64().decode(message));
//            } catch (Exception ignored) {
//
//            }
//        }
        return message;
    }

    public void setMessage(String message) {
//        try {
//            this.message = BaseEncoding.base64().encode(message.getBytes());
//        } catch (Exception ex) {
            this.message = message;
//        }
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }
}
