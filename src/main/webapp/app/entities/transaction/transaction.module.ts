import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import {
    TransactionComponent,
    TransactionUpdateComponent,
    transactionRoute
} from './';

const ENTITY_STATES = [...transactionRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TransactionComponent,
        TransactionUpdateComponent,
    ],
    entryComponents: [TransactionComponent, TransactionUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheTransactionModule {}
