import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { BantheSharedLibsModule, BantheSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';
import { CurrencyMaskDirective } from 'app/shared/util/currency.directive';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { PointReplacerPipe } from 'app/shared/pipe/point-replacer.pipe';
import {PointReplacerPipeNoVND} from 'app/shared/pipe/point-replacer-novnd.pipe';

@NgModule({
    imports: [BantheSharedLibsModule, BantheSharedCommonModule, SweetAlert2Module, LoadingBarHttpClientModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, CurrencyMaskDirective, PointReplacerPipe, PointReplacerPipeNoVND],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        BantheSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        SweetAlert2Module,
        CurrencyMaskDirective,
        LoadingBarHttpClientModule,
        PointReplacerPipe,
        PointReplacerPipeNoVND
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BantheSharedModule {
    static forRoot() {
        return {
            ngModule: BantheSharedModule
        };
    }
}
