import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDemandDtl } from 'app/shared/model/demand-dtl.model';
import { DemandDtlService } from './demand-dtl.service';
import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from 'app/entities/demand';

@Component({
    selector: 'hg-demand-dtl-update',
    templateUrl: './demand-dtl-update.component.html'
})
export class DemandDtlUpdateComponent implements OnInit {
    demandDtl: IDemandDtl;
    isSaving: boolean;

    demands: IDemand[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected demandDtlService: DemandDtlService,
        protected demandService: DemandService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demandDtl }) => {
            this.demandDtl = demandDtl;
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
        if (this.demandDtl.id !== undefined) {
            this.subscribeToSaveResponse(this.demandDtlService.update(this.demandDtl));
        } else {
            this.subscribeToSaveResponse(this.demandDtlService.create(this.demandDtl));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandDtl>>) {
        result.subscribe((res: HttpResponse<IDemandDtl>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDemandById(index: number, item: IDemand) {
        return item.id;
    }
}
