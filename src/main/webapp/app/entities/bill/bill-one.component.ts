import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { IBill, NhaMang } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { IDemand } from 'app/shared/model/demand.model';
import { SwalComponent } from '@toverux/ngx-sweetalert2';
import swal from 'sweetalert2';
import {AccountService} from 'app/core';
import {JhiParseLinks} from 'ng-jhipster';

@Component({
    selector: 'hg-bill-one',
    templateUrl: './bill-one.component.html',
    styleUrls: ['bill.css']
})
export class BillOneComponent implements OnInit {
    @ViewChild('errorSwal') private errorSwal: SwalComponent;
    @ViewChild('success') private success: SwalComponent;
    @ViewChild('confirmSwal') private confirmSwal: SwalComponent;
    bill: IBill;
    bill2: IBill;
    isSaving: boolean;
    demand: IDemand;
    demand2: IDemand;
    index: number;
    errorMessage: string;
    selected = false;
    currentAccount: any;
    bills: IBill[];
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    isOne = true;

    constructor(protected billService: BillService, protected activatedRoute: ActivatedRoute,
                protected accountService: AccountService,
                protected parseLinks: JhiParseLinks) {}

    ngOnInit() {
        this.index = 0;
        this.demand = {};
        this.demand2 = {serviceType: 1};
        this.bill = {};
        this.bill2 = {};
        this.isSaving = false;
        this.loadAll();
        this.loadAcc();
    }

    loadAcc() {
        this.accountService.fetch().subscribe(account => {
            this.currentAccount = account.body;
            console.log(this.currentAccount);
        });
    }

    previousState() {
        this.selected = false;
    }

    save() {
        if (!this.demand.account) {
            this.errorMessage = 'Bạn chưa nhập tài khoản cần nạp tiền';
            this.errorSwal.show();
            return;
        } else if (this.demand.account && this.demand.account.length !== 10) {
            this.errorMessage = 'Độ dài số điện thoại phải bằng 10 kí tự';
            this.errorSwal.show();
            return;
        } else if (!this.demand.amount || this.demand.amount % 10000 !== 0) {
            this.errorMessage = 'Số tiền nạp phải lớn hơn 0 và là bội của 10.000 VNĐ';
            this.errorSwal.show();
            return;
        }
        this.isOne = true;
        this.confirmSwal.show();
    }

    save2() {
        if (!this.demand2.account) {
            this.errorMessage = 'Bạn chưa nhập tài khoản cần nạp tiền';
            this.errorSwal.show();
            return;
        } else if (!this.demand2.amount || this.demand2.amount % 10000 !== 0) {
            this.errorMessage = 'Số tiền nạp phải lớn hơn 0 và là bội của 10.000 VNĐ';
            this.errorSwal.show();
            return;
        }
        this.isOne = false;
        this.confirmSwal.show();
    }

    saveData() {
        if (this.isOne) {
            this.isSaving = true;
            this.bill.chargeType = 1;
            this.billService
                .create({ bill: this.bill, demands: [this.demand]})
                .subscribe((res: HttpResponse<IBill>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
        } else {
            this.isSaving = true;
            this.bill2.chargeType = 2;
            this.billService
                .create({ bill: this.bill2, demands: [this.demand2] })
                .subscribe((res: HttpResponse<IBill>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
        }
    }

    protected onSaveSuccess() {
        this.loadAcc();
        this.loadAll();
        this.isSaving = false;
        this.bill = {type: this.bill.type};
        this.bill2 = {type: this.bill2.type};
        this.demand2 = {};
        this.demand = {};
        this.success.show();
    }

    protected onSaveError(res) {
        this.isSaving = false;
        if (res.error.errorKey === 'lockBill') {
            swal({
                title: 'Có lỗi sảy ra',
                text: 'Hệ thông hiện tại đang ngừng nhận đơn hàng của nhà mạng này!',
                type: 'error'
            });
        } else if (res.error.errorKey === 'login') {
            swal({
                title: 'Có lỗi sảy ra',
                text: 'Hết phiên làm việc, vui lòng đăng nhập lại!',
                type: 'error'
            });
        } else if (res.error.errorKey === 'invalidAmount') {
            swal({
                title: 'Có lỗi sảy ra',
                text: 'Số dư trong tài khoản không đủ để tạo đơn hàng này!',
                type: 'error'
            });
        }
    }

    changeType(vtt: string) {
        this.bill.type = vtt === 'VTT' ? NhaMang.VTT : vtt === 'VNP' ? NhaMang.VNP : NhaMang.VMS;
        this.bill2.type = vtt === 'VTT' ? NhaMang.VTT : vtt === 'VNP' ? NhaMang.VNP : NhaMang.VMS;
        this.selected = true;
    }

    isLetter() {
        if (this.demand2.account && this.demand2.account.match(/[a-z]/i)) {
            this.demand2.serviceType = 4;
        }
    }

    loadAll() {
        this.billService
            .query({
                page: 0,
                size: 10,
                sort: ['id, desc'],
            })
            .subscribe(
                (res: HttpResponse<IBill[]>) => this.paginateBills(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    protected paginateBills(data: IBill[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.bills = data;
    }

    protected onError(errorMessage: string) {
    }
}
