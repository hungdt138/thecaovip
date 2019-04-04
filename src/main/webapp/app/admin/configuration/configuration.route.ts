import { Route } from '@angular/router';

import { JhiConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'hg-configuration',
    component: JhiConfigurationComponent,
    data: {
        pageTitle: 'Configuration'
    }
};
