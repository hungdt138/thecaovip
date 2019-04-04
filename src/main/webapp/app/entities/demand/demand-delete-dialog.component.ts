import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from './demand.service';

@Component({
    selector: 'hg-demand-delete-dialog',
    templateUrl: './demand-delete-dialog.component.html'
})
export class DemandDeleteDialogComponent {
    demand: IDemand;

    constructor(protected demandService: DemandService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.demandService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'demandListModification',
                content: 'Deleted an demand'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'hg-demand-delete-popup',
    template: ''
})
export class DemandDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DemandDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.demand = demand;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/demand', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/demand', { outlets: { popup: null } }]);
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
