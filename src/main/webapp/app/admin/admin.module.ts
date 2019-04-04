import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BantheSharedModule } from 'app/shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JhiMetricsMonitoringComponent,
    JhiHealthModalComponent,
    JhiHealthCheckComponent,
    JhiConfigurationComponent,
    JhiDocsComponent
} from './';
import {SystemComponent} from 'app/admin/system/system.component';
import {SystemUpdateComponent} from 'app/admin/system/system-update.component';
import {SystemDetailComponent} from 'app/admin/system/system-detail.component';

@NgModule({
    imports: [
        BantheSharedModule,
        RouterModule.forChild(adminState)
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserMgmtDetailComponent,
        UserMgmtUpdateComponent,
        UserMgmtDeleteDialogComponent,
        LogsComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiMetricsMonitoringComponent,
        SystemComponent,
        SystemUpdateComponent,
        SystemDetailComponent
    ],
    entryComponents: [UserMgmtDeleteDialogComponent, JhiHealthModalComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheAdminModule {}
