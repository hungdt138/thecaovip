import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISystem, System } from 'app/shared/model/system.model';
import { SystemService } from './system.service';
import { SystemComponent } from 'app/admin';
import { SystemDetailComponent } from 'app/admin';
import { SystemUpdateComponent } from 'app/admin';

@Injectable({ providedIn: 'root' })
export class SystemResolve implements Resolve<ISystem> {
    constructor(private service: SystemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISystem> {
        return this.service.query().pipe(
            filter((response: HttpResponse<System>) => response.ok),
            map((system: HttpResponse<System>) => system.body)
        );
    }
}

export const systemRoute: Routes = [
    {
        path: 'system',
        component: SystemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            defaultSort: 'id,asc',
            pageTitle: 'Systems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'system/:id/view',
        component: SystemDetailComponent,
        resolve: {
            system: SystemResolve
        },
        data: {
            pageTitle: 'Systems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'system/new',
        component: SystemUpdateComponent,
        resolve: {
            system: SystemResolve
        },
        data: {
            pageTitle: 'Systems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'system/:id/edit',
        component: SystemUpdateComponent,
        resolve: {
            system: SystemResolve
        },
        data: {
            pageTitle: 'Systems'
        },
        canActivate: [UserRouteAccessService]
    }
];
