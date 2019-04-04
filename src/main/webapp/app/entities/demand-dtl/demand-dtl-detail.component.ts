import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandDtl } from 'app/shared/model/demand-dtl.model';

@Component({
    selector: 'hg-demand-dtl-detail',
    templateUrl: './demand-dtl-detail.component.html'
})
export class DemandDtlDetailComponent implements OnInit {
    demandDtl: IDemandDtl;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demandDtl }) => {
            this.demandDtl = demandDtl;
        });
    }

    previousState() {
        window.history.back();
    }
}
