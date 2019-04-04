package com.hieugie.banthe.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.hieugie.banthe.domain.enumeration.Price;

/**
 * A DemandDtl.
 */
@Entity
@Table(name = "demand_dtl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DemandDtl extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "charge_quantity")
    private Integer chargeQuantity;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private Demand demand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public DemandDtl denomination(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public DemandDtl quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Demand getDemand() {
        return demand;
    }

    public DemandDtl demand(Demand demand) {
        this.demand = demand;
        return this;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public Integer getChargeQuantity() {
        return chargeQuantity;
    }

    public void setChargeQuantity(Integer chargeQuantity) {
        this.chargeQuantity = chargeQuantity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
        DemandDtl demandDtl = (DemandDtl) o;
        if (demandDtl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demandDtl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DemandDtl{" +
            "id=" + getId() +
            ", price='" + getPrice() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }

    public DemandDtl() {
        this.chargeQuantity = 0;
    }
}
