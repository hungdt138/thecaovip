import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ISystem } from 'app/shared/model/system.model';
import { SystemService } from './system.service';
import { SwalComponent } from '@toverux/ngx-sweetalert2';

@Component({
    selector: 'hg-system-update',
    templateUrl: './system-update.component.html'
})
export class SystemUpdateComponent implements OnInit {
    system: ISystem;
    isSaving: boolean;
    @ViewChild('success') private success: SwalComponent;

    constructor(protected systemService: SystemService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ system }) => {
            this.system = system;
            this.system.id = null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.systemService
            .create(this.system)
            .subscribe((res: HttpResponse<ISystem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.success.show();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
