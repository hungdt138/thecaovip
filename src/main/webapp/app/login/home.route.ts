import { Route, Routes } from '@angular/router';
import { PageHomeComponent } from 'app/login/home.component';

export const pageLoginCustomerRoute: Route = {
    path: 'page-login',
    component: PageHomeComponent,
    data: {
        defaultSort: 'id,asc',
        pageTitle: 'Thẻ vip'
    }
};
