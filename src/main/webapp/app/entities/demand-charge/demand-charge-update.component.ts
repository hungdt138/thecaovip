import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IDemandCharge } from 'app/shared/model/demand-charge.model';
import { DemandChargeService } from './demand-charge.service';
import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from 'app/entities/demand';
import swal from 'sweetalert2';

@Component({
    selector: 'hg-demand-charge-update',
    templateUrl: './demand-charge-update.component.html'
})
export class DemandChargeUpdateComponent implements OnInit {
    demandCharge: IDemandCharge;
    isSaving: boolean;

    demands: IDemand[];

    constructor(
        protected demandChargeService: DemandChargeService,
        protected demandService: DemandService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demandCharge }) => {
            this.demandCharge = demandCharge;
        });
        this.demandService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDemand[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDemand[]>) => response.body)
            )
            .subscribe((res: IDemand[]) => (this.demands = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.demandChargeService.create(this.demandCharge).subscribe((res: HttpResponse<IDemandCharge>) => this.onSaveSuccess(res),
            (res: HttpErrorResponse) => this.onSaveError(res));
    }

    protected onSaveSuccess(res) {
        console.log(res);
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError(res) {
        console.log(res);
        swal({
            title: 'Có lỗi sảy ra',
            text: 'Không thể cập nhật thẻ này!',
            type: 'error'
        });
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
    }

}
