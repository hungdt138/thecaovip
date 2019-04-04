import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystem } from 'app/shared/model/system.model';

@Component({
    selector: 'hg-system-detail',
    templateUrl: './system-detail.component.html'
})
export class SystemDetailComponent implements OnInit {
    system: ISystem;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ system }) => {
            this.system = system;
        });
    }

    previousState() {
        window.history.back();
    }
}
