import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';
import { PageHomeComponent } from 'app/login/home.component';

export const errorRoute: Routes = [
    {
        path: 'error',
        component: ErrorComponent,
        data: {
            authorities: [],
            pageTitle: 'banthe'
        }
    },
    {
        path: 'accessdenied',
        component: ErrorComponent,
        data: {
            authorities: [],
            pageTitle: 'banthe',
            error403: true
        }
    },
    {
        path: '404',
        component: ErrorComponent,
        data: {
            authorities: [],
            pageTitle: 'banthe',
            error404: true
        }
    },
    {
        path: '**',
        redirectTo: '/404'
    },
    {
        path: 'page-login',
        component: PageHomeComponent,
        data: {
            authorities: [],
            pageTitle: 'error.title',
            error403: true
        }
    }
];
