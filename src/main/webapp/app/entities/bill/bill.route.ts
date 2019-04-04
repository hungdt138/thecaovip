import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Bill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { BillComponent } from './bill.component';
import { BillDetailComponent } from './bill-detail.component';
import { BillUpdateComponent } from './bill-update.component';
import { BillDeletePopupComponent } from './bill-delete-dialog.component';
import { IBill } from 'app/shared/model/bill.model';
import { BillImportUpdateComponent } from 'app/entities/bill/bill-import-update.component';
import { BillOneComponent } from 'app/entities/bill/bill-one.component';

@Injectable({ providedIn: 'root' })
export class BillResolve implements Resolve<IBill> {
    constructor(private service: BillService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBill> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Bill>) => response.ok),
                map((bill: HttpResponse<Bill>) => bill.body)
            );
        }
        return of(new Bill());
    }
}

export const billRoute: Routes = [
    {
        path: '',
        component: BillComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,desc',
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: BillDetailComponent,
        resolve: {
            bill: BillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BillUpdateComponent,
        resolve: {
            bill: BillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'import-new',
        component: BillImportUpdateComponent,
        resolve: {
            bill: BillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'one',
        component: BillOneComponent,
        resolve: {
            bill: BillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BillDeletePopupComponent,
        resolve: {
            bill: BillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bills'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
