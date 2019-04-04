import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import {
    DemandChargeComponent,
    DemandChargeVinaComponent,
    DemandChargeUpdateComponent,
    demandChargeRoute,
    DemandChargeAllComponent
} from './';

const ENTITY_STATES = [...demandChargeRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DemandChargeComponent,
        DemandChargeUpdateComponent,
        DemandChargeVinaComponent,
        DemandChargeAllComponent
    ],
    entryComponents: [
        DemandChargeComponent,
        DemandChargeVinaComponent,
        DemandChargeUpdateComponent,
        DemandChargeAllComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheDemandChargeModule {}
