import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBill } from 'app/shared/model/bill.model';
import {Demand} from 'app/shared/model/demand.model';
import {DemandCharge} from 'app/shared/model/demand-charge.model';

@Component({
    selector: 'hg-bill-detail',
    templateUrl: './bill-detail.component.html'
})
export class BillDetailComponent implements OnInit {
    bill: IBill;
    demandCharges: DemandCharge[];

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bill }) => {
            this.bill = bill.bill;
            this.demandCharges = bill.demandCharges;
        });
    }

    previousState() {
        window.history.back();
    }
}
