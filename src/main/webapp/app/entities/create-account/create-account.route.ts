import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';

import {User, UserRouteAccessService, UserService} from 'app/core';
import {CreateAccountComponent} from 'app/entities/create-account/create-account.component';
import {CreateAccountUpdateComponent} from 'app/entities/create-account/create-account-update.component';

@Injectable({ providedIn: 'root' })
export class CreateAccountResolve implements Resolve<any> {
    constructor(private service: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['login'] ? route.params['login'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new User();
    }
}

export const CreateAccountRoute: Routes = [
    {
        path: '',
        component: CreateAccountComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Danh sách đại lý',
            defaultSort: 'id,asc'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CreateAccountUpdateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tạo mới đại lý'
        },
        resolve: {
            user: CreateAccountResolve
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':login/edit',
        component: CreateAccountUpdateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cập nhật thông tin đại lý'
        },
        resolve: {
            user: CreateAccountResolve
        },
        canActivate: [UserRouteAccessService]
    }
];
