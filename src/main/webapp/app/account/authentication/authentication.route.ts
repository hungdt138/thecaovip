import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { AuthenticationComponent } from 'app/account';

export const authenticationRoute: Route = {
    path: 'authentication',
    component: AuthenticationComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Settings'
    },
    canActivate: [UserRouteAccessService]
};
