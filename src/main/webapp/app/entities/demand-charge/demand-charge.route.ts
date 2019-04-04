import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DemandCharge } from 'app/shared/model/demand-charge.model';
import { DemandChargeService } from './demand-charge.service';
import { DemandChargeComponent } from './demand-charge.component';
import { DemandChargeUpdateComponent } from './demand-charge-update.component';
import { IDemandCharge } from 'app/shared/model/demand-charge.model';
import {DemandChargeVinaComponent} from './demand-charge-vina.component';
import {DemandChargeAllComponent} from 'app/entities/demand-charge/demand-charge-all.component';

@Injectable({ providedIn: 'root' })
export class DemandChargeResolve implements Resolve<IDemandCharge> {
    constructor(private service: DemandChargeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDemandCharge> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DemandCharge>) => response.ok),
                map((demandCharge: HttpResponse<DemandCharge>) => demandCharge.body)
            );
        }
        return of(new DemandCharge());
    }
}

export const demandChargeRoute: Routes = [
    {
        path: '',
        component: DemandChargeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,desc',
            pageTitle: 'DemandCharges'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hand',
        component: DemandChargeVinaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,desc',
            pageTitle: 'Thẻ vina nạp tay'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DemandChargeUpdateComponent,
        resolve: {
            demandCharge: DemandChargeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandCharges'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DemandChargeUpdateComponent,
        resolve: {
            demandCharge: DemandChargeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandCharges'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'all',
        component: DemandChargeAllComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_GAME'],
            defaultSort: 'id,desc',
            pageTitle: 'Thẻ vina nạp tay'
        },
        canActivate: [UserRouteAccessService]
    }
];
