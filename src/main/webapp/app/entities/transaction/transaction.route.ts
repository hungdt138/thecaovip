import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {User, UserRouteAccessService, UserService} from 'app/core';
import {Observable, of} from 'rxjs';
import {TransactionComponent} from './transaction.component';
import {TransactionUpdateComponent} from './transaction-update.component';

@Injectable({ providedIn: 'root' })
export class TransactionResolve implements Resolve<any> {
    constructor(private userService: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.userService.find(id);
        }
        return of(new User());
    }
}

export const transactionRoute: Routes = [
    {
        path: '',
        component: TransactionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/new',
        component: TransactionUpdateComponent,
        resolve: {
            user: TransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    }
    ,
    {
        path: 'new',
        component: TransactionUpdateComponent,
        resolve: {
            user: TransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    }
];
