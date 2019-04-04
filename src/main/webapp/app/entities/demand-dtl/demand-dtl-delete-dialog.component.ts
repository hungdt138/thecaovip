import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDemandDtl } from 'app/shared/model/demand-dtl.model';
import { DemandDtlService } from './demand-dtl.service';

@Component({
    selector: 'hg-demand-dtl-delete-dialog',
    templateUrl: './demand-dtl-delete-dialog.component.html'
})
export class DemandDtlDeleteDialogComponent {
    demandDtl: IDemandDtl;

    constructor(
        protected demandDtlService: DemandDtlService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.demandDtlService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'demandDtlListModification',
                content: 'Deleted an demandDtl'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'hg-demand-dtl-delete-popup',
    template: ''
})
export class DemandDtlDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demandDtl }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DemandDtlDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.demandDtl = demandDtl;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/demand-dtl', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/demand-dtl', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
