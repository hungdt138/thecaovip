import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {BantheSharedModule} from 'app/shared';
import {
    CreateAccountComponent,
    CreateAccountRoute,
    CreateAccountUpdateComponent,
} from './';

const ENTITY_STATES = [...CreateAccountRoute];

@NgModule({
    imports: [BantheSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CreateAccountComponent,
        CreateAccountUpdateComponent
    ],
    entryComponents: [
        CreateAccountComponent,
        CreateAccountUpdateComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheCreateAccountModule {}
