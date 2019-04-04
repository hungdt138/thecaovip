package com.hieugie.banthe.web.rest.dto;

import com.hieugie.banthe.domain.Bill;
import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.domain.DemandCharge;

import java.util.List;

public class BillDTO {
    private Bill bill;
    private List<Demand> demands;
    private List<DemandCharge> demandCharges;

    public BillDTO(Bill bill, List<Demand> demands) {
        this.bill = bill;
        this.demands = demands;
    }

    public BillDTO(List<DemandCharge> demandCharges, Bill bill) {
        this.bill = bill;
        this.demandCharges = demandCharges;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

    public BillDTO() {
    }

    @Override
    public String toString() {
        return "BillDTO{" +
            "bill=" + bill +
            ", demands=" + demands +
            '}';
    }

    public List<DemandCharge> getDemandCharges() {
        return demandCharges;
    }

    public void setDemandCharges(List<DemandCharge> demandCharges) {
        this.demandCharges = demandCharges;
    }
}
