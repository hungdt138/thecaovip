import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BantheSharedModule } from 'app/shared';
import {
    ReportComponent,
    reportRoute
} from './';

const ENTITY_STATES = [...reportRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReportComponent,
    ],
    entryComponents: [ReportComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheReportModule {}
