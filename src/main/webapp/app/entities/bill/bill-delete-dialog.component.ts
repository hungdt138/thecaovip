import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { SwalComponent } from '@toverux/ngx-sweetalert2';

@Component({
    selector: 'hg-bill-delete-dialog',
    templateUrl: './bill-delete-dialog.component.html'
})
export class BillDeleteDialogComponent {
    bill: IBill;
    @ViewChild('success') private success: SwalComponent;
    constructor(protected billService: BillService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'billListModification',
                content: 'Deleted an bill'
            });
            this.success.show();
        });
    }
}

@Component({
    selector: 'hg-bill-delete-popup',
    template: ''
})
export class BillDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bill }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BillDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.bill = bill.bill;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/bill', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/bill', { outlets: { popup: null } }]);
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
