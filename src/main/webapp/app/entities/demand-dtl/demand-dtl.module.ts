import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import {
    DemandDtlComponent,
    DemandDtlDetailComponent,
    DemandDtlUpdateComponent,
    DemandDtlDeletePopupComponent,
    DemandDtlDeleteDialogComponent,
    demandDtlRoute,
    demandDtlPopupRoute
} from './';

const ENTITY_STATES = [...demandDtlRoute, ...demandDtlPopupRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DemandDtlComponent,
        DemandDtlDetailComponent,
        DemandDtlUpdateComponent,
        DemandDtlDeleteDialogComponent,
        DemandDtlDeletePopupComponent
    ],
    entryComponents: [DemandDtlComponent, DemandDtlUpdateComponent, DemandDtlDeleteDialogComponent, DemandDtlDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheDemandDtlModule {}
