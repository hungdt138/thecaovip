import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IBill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { Demand, IDemand } from 'app/shared/model/demand.model';
import { SwalComponent } from '@toverux/ngx-sweetalert2';
import swal from 'sweetalert2';

@Component({
    selector: 'hg-bill-import-update',
    templateUrl: './bill-import-update.component.html'
})
export class BillImportUpdateComponent implements OnInit {
    @ViewChild('errorSwal') private errorSwal: SwalComponent;
    @ViewChild('success') private success: SwalComponent;
    bill: IBill;
    isSaving: boolean;
    demands: IDemand[];
    demand: Demand;
    index: number;
    errorMessage: string;
    file: any;
    sheets: any;
    sheet: any;

    constructor(protected billService: BillService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.index = 0;
        this.demands = [];
        this.demand = {};
        this.isSaving = false;
        this.file = {};
        this.sheets = [];
        this.bill = {chargeType: 2};
        this.sheet = {id: 0};
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

    fileChange(event): void {
        const fileList: FileList = event.target.files;
        // this.sheets = [];
        // this.sheet = null;
        if (fileList.length > 0) {
            this.file = fileList[0];
            // Đọc tên các sheet
            this.billService.getSheetName(this.file).subscribe(
                res => {
                    this.sheets = res.body;
                },
                error => {
                    console.log(error);
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'File không đúng định dạng',
                        type: 'error'
                    });
                }
            );
        }
    }

    readFile() {
        this.billService.readSheet(this.file, this.sheet.id).subscribe(
            res => {
                this.demands = res.body.demands;
                this.bill.amount = res.body.bill.amount;
            },
            error => {
                swal({
                    title: 'Có lỗi sảy ra',
                    text: 'File không đúng định dạng',
                    type: 'error'
                });
            }
        );
    }

    changeHighPriority(value) {
        this.bill.chargeType = value;
    }
}
