import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import {
    BillComponent,
    BillDetailComponent,
    BillUpdateComponent,
    BillDeletePopupComponent,
    BillDeleteDialogComponent,
    billRoute,
    billPopupRoute,
    BillOneComponent
} from './';
import { BillImportUpdateComponent } from 'app/entities/bill/bill-import-update.component';

const ENTITY_STATES = [...billRoute, ...billPopupRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BillComponent,
        BillDetailComponent,
        BillUpdateComponent,
        BillDeleteDialogComponent,
        BillDeletePopupComponent,
        BillImportUpdateComponent,
        BillOneComponent
    ],
    entryComponents: [
        BillComponent,
        BillUpdateComponent,
        BillDeleteDialogComponent,
        BillDeletePopupComponent,
        BillImportUpdateComponent,
        BillOneComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheBillModule {}
