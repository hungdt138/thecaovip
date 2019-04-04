import { NgModule } from '@angular/core';

import { BantheSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BantheSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BantheSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BantheSharedCommonModule {}
