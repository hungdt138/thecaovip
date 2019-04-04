import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DemandDtl } from 'app/shared/model/demand-dtl.model';
import { DemandDtlService } from './demand-dtl.service';
import { DemandDtlComponent } from './demand-dtl.component';
import { DemandDtlDetailComponent } from './demand-dtl-detail.component';
import { DemandDtlUpdateComponent } from './demand-dtl-update.component';
import { DemandDtlDeletePopupComponent } from './demand-dtl-delete-dialog.component';
import { IDemandDtl } from 'app/shared/model/demand-dtl.model';

@Injectable({ providedIn: 'root' })
export class DemandDtlResolve implements Resolve<IDemandDtl> {
    constructor(private service: DemandDtlService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDemandDtl> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DemandDtl>) => response.ok),
                map((demandDtl: HttpResponse<DemandDtl>) => demandDtl.body)
            );
        }
        return of(new DemandDtl());
    }
}

export const demandDtlRoute: Routes = [
    {
        path: '',
        component: DemandDtlComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'DemandDtls'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DemandDtlDetailComponent,
        resolve: {
            demandDtl: DemandDtlResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandDtls'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DemandDtlUpdateComponent,
        resolve: {
            demandDtl: DemandDtlResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandDtls'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DemandDtlUpdateComponent,
        resolve: {
            demandDtl: DemandDtlResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandDtls'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const demandDtlPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DemandDtlDeletePopupComponent,
        resolve: {
            demandDtl: DemandDtlResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DemandDtls'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
