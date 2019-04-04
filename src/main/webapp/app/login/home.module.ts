import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import { pageLoginCustomerRoute } from 'app/login/home.route';
import { PageHomeComponent } from 'app/login/home.component';

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild([pageLoginCustomerRoute])],
    declarations: [PageHomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BanthePageHomeModule {}
