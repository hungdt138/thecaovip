import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {User, UserRouteAccessService, UserService} from 'app/core';
import {Observable, of} from 'rxjs';
import {ReportComponent} from './report.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<any> {
    constructor(private userService: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.userService.find(id);
        }
        return of(new User());
    }
}

export const reportRoute: Routes = [
    {
        path: '',
        component: ReportComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    }
];
