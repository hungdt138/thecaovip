import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#BantheTransactionModule'
            },
            {
                path: 'demand',
                loadChildren: './demand/demand.module#BantheDemandModule'
            },
            {
                path: 'demand-dtl',
                loadChildren: './demand-dtl/demand-dtl.module#BantheDemandDtlModule'
            },
            {
                path: 'demand-charge',
                loadChildren: './demand-charge/demand-charge.module#BantheDemandChargeModule'
            },
            {
                path: 'create-account',
                loadChildren: './create-account/create-account.module#BantheCreateAccountModule'
            },
            {
                path: 'bill',
                loadChildren: './bill/bill.module#BantheBillModule'
            },
            {
                path: 'report',
                loadChildren: './report/report.module#BantheReportModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheEntityModule {}
