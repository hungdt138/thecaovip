package com.hieugie.banthe.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A System.
 */
@Entity
@Table(name = "jhi_system")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class System  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "high_free_from")
    private Integer highFreeFrom;

    @Column(name = "hign_fee_to")
    private Integer hignFeeTo;

    @Min(0)
    @Max(31)
    @Column(name = "viettel_promotion_date")
    private Integer viettelPromotionDate;

    @Min(0)
    @Max(31)
    @Column(name = "vina_promotion_date")
    private Integer vinaPromotionDate;

    @Min(0)
    @Max(31)
    @Column(name = "mobi_promotion_date")
    private Integer mobiPromotionDate;

    @Column(name = "lock_viettel")
    private Boolean lockViettel;

    @Column(name = "lock_vina")
    private Boolean lockVina;

    @Column(name = "lock_mobi")
    private Boolean lockMobi;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_1")
    private Integer feePercentLv1;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_2")
    private Integer feePercentLv2;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_1_b")
    private Integer feePercentLv1b;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_2_b")
    private Integer feePercentLv2b;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_1_vina")
    private Integer feePercentLv1Vina;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_2_vina")
    private Integer feePercentLv2Vina;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_1_mobi")
    private Integer feePercentLv1Mobi;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "fee_percent_lv_2_mobi")
    private Integer feePercentLv2Mobi;


    @Column(name = "status")
    private Boolean status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHighFreeFrom() {
        return highFreeFrom;
    }

    public System highFreeFrom(Integer highFreeFrom) {
        this.highFreeFrom = highFreeFrom;
        return this;
    }

    public void setHighFreeFrom(Integer highFreeFrom) {
        this.highFreeFrom = highFreeFrom;
    }

    public Integer getHignFeeTo() {
        return hignFeeTo;
    }

    public System hignFeeTo(Integer hignFeeTo) {
        this.hignFeeTo = hignFeeTo;
        return this;
    }

    public void setHignFeeTo(Integer hignFeeTo) {
        this.hignFeeTo = hignFeeTo;
    }

    public Integer getFeePercentLv1() {
        return feePercentLv1;
    }

    public System feePercentLv1(Integer feePercentLv1) {
        this.feePercentLv1 = feePercentLv1;
        return this;
    }

    public void setFeePercentLv1(Integer feePercentLv1) {
        this.feePercentLv1 = feePercentLv1;
    }

    public Integer getFeePercentLv2() {
        return feePercentLv2;
    }

    public System feePercentLv2(Integer feePercentLv2) {
        this.feePercentLv2 = feePercentLv2;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setFeePercentLv2(Integer feePercentLv2) {
        this.feePercentLv2 = feePercentLv2;
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
        System system = (System) o;
        if (system.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), system.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "System{" +
            "id=" + getId() +
            ", highFreeFrom=" + getHighFreeFrom() +
            ", hignFeeTo=" + getHignFeeTo() +
            ", feePercentLv1=" + getFeePercentLv1() +
            ", feePercentLv2=" + getFeePercentLv2() +
            "}";
    }

    public System(@Min(value = 0) @Max(value = 100) Integer feePercentLv2) {
        this.feePercentLv2 = feePercentLv2;
    }

    public System() {
    }

    public Integer getViettelPromotionDate() {
        return viettelPromotionDate;
    }

    public void setViettelPromotionDate(Integer viettelPromotionDate) {
        this.viettelPromotionDate = viettelPromotionDate;
    }

    public Integer getVinaPromotionDate() {
        return vinaPromotionDate;
    }

    public void setVinaPromotionDate(Integer vinaPromotionDate) {
        this.vinaPromotionDate = vinaPromotionDate;
    }

    public Integer getMobiPromotionDate() {
        return mobiPromotionDate;
    }

    public void setMobiPromotionDate(Integer mobiPromotionDate) {
        this.mobiPromotionDate = mobiPromotionDate;
    }

    public Boolean getLockViettel() {
        return lockViettel;
    }

    public void setLockViettel(Boolean lockViettel) {
        this.lockViettel = lockViettel;
    }

    public Boolean getLockVina() {
        return lockVina;
    }

    public void setLockVina(Boolean lockVina) {
        this.lockVina = lockVina;
    }

    public Boolean getLockMobi() {
        return lockMobi;
    }

    public void setLockMobi(Boolean lockMobi) {
        this.lockMobi = lockMobi;
    }

    public Integer getFeePercentLv1Vina() {
        return feePercentLv1Vina;
    }

    public void setFeePercentLv1Vina(Integer feePercentLv1Vina) {
        this.feePercentLv1Vina = feePercentLv1Vina;
    }

    public Integer getFeePercentLv2Vina() {
        return feePercentLv2Vina;
    }

    public void setFeePercentLv2Vina(Integer feePercentLv2Vina) {
        this.feePercentLv2Vina = feePercentLv2Vina;
    }

    public Integer getFeePercentLv1Mobi() {
        return feePercentLv1Mobi;
    }

    public void setFeePercentLv1Mobi(Integer feePercentLv1Mobi) {
        this.feePercentLv1Mobi = feePercentLv1Mobi;
    }

    public Integer getFeePercentLv2Mobi() {
        return feePercentLv2Mobi;
    }

    public void setFeePercentLv2Mobi(Integer feePercentLv2Mobi) {
        this.feePercentLv2Mobi = feePercentLv2Mobi;
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
}
