import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';

import {ITransaction} from 'app/shared/model/transaction.model';
import {AccountService} from 'app/core';
import {ReportService} from './report.service';
import * as moment from 'moment';

@Component({
    selector: 'hg-report',
    templateUrl: './report.component.html'
})
export class ReportComponent implements OnInit {
    currentAccount: any;
    reports: any;
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    user: any;
    fromDate: any;
    toDate: any;

    constructor(
        protected reportService: ReportService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        const fd = this.fromDate ? moment(this.fromDate).format('YYYYMMDD') : '';
        const td = this.toDate ? moment(this.toDate).format('YYYYMMDD') : '';
        this.reportService.query({
            user: this.user ? this.user : '',
            fromDate: fd,
            toDate: td
            })
            .subscribe(
                (res: HttpResponse<ITransaction[]>) => this.paginateTransactions(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition() {
        this.router.navigate(['/report'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/report', {}
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
    }

    trackId(index: number, item: ITransaction) {
        return item.id;
    }

    protected paginateTransactions(data: any, headers: HttpHeaders) {
        this.reports = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
