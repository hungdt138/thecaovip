import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IBill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { IDemand } from 'app/shared/model/demand.model';
import { SwalComponent } from '@toverux/ngx-sweetalert2';
import swal from 'sweetalert2';

@Component({
    selector: 'hg-bill-update',
    templateUrl: './bill-update.component.html'
})
export class BillUpdateComponent implements OnInit {
    @ViewChild('errorSwal') private errorSwal: SwalComponent;
    @ViewChild('success') private success: SwalComponent;
    bill: IBill;
    isSaving: boolean;
    demands: IDemand[];
    demand: IDemand;
    index: number;
    errorMessage: string;

    constructor(protected billService: BillService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.index = 0;
        this.demands = [];
        this.demand = {};
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ bill }) => {
            this.bill = bill;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.billService
            .create({ bill: this.bill, demands: this.demands })
            .subscribe((res: HttpResponse<IBill>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    protected onSaveSuccess() {
        this.isSaving = false;
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

    addDemand() {
        if (!this.demand.account) {
            this.errorMessage = 'Bạn chưa nhập tài khoản cần nạp tiền';
            this.errorSwal.show();
            return;
        } else if (!this.demand.amount || this.demand.amount % 10000 !== 0) {
            this.errorMessage = 'Số tiền nạp phải lớn hơn 0 và là bội của 10.000 VNĐ';
            this.errorSwal.show();
            return;
        }
        if (this.bill.chargeType === 1 && this.demand.account.length !== 10) {
            this.errorMessage = 'Số điện thoại phải đủ 10 kí tự';
            this.errorSwal.show();
            return;
        }
        this.index++;
        const copy = { ...this.demand };
        copy.id = this.index;
        this.demands.push(copy);
        this.demand = {};
        this.bill.amount += copy.amount;
    }

    deleteDemand(demand) {
        this.demands.splice(this.demands.indexOf(demand), 1);
        this.bill.amount -= demand.amount;
    }

    changeHighPriority(value) {
        this.bill.chargeType = value;
    }
}
