import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { User, UserService } from 'app/core';
import { SystemService } from 'app/admin/system/system.service';
import { SwalComponent } from '@toverux/ngx-sweetalert2';

@Component({
    selector: 'hg-user-mgmt-update',
    templateUrl: './create-account-update.component.html'
})
export class CreateAccountUpdateComponent implements OnInit {
    user: User;
    isSaving: boolean;
    @ViewChild('errorSwal') private errorSwal: SwalComponent;
    @ViewChild('success') private success: SwalComponent;
    errorMessage: string;
    confirmPassword: string;
    system: any;

    constructor(
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private systemService: SystemService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {

            this.systemService.findOne().subscribe(res => {
                if (user.body) {
                    this.user = user.body ? user.body : user;
                } else {
                    this.user.feePercentLv1b = res.body.feePercentLv1b;
                    this.user.feePercentLv1 = res.body.feePercentLv1;
                    this.user.feePercentLv2b = res.body.feePercentLv2b;
                    this.user.feePercentLv2 = res.body.feePercentLv2;
                }
                this.system = res.body;
            });
            this.user = {};

        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (!!this.user.id) {
            this.userService.updateBranch(this.user).subscribe(response => this.onSaveSuccess(response), res => this.onSaveError(res));
        } else {
            this.userService.createBranch(this.user).subscribe(response => this.onSaveSuccess(response), res => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.success.show();
    }

    private onSaveError(res) {
        console.log(res);
        if (res.error.errorKey === 'userexists') {
            this.errorMessage = 'Tên đăng nhập đã tồn tại';
        } else if (res.error.errorKey === 'invalidFeePercent1') {
            this.errorMessage = 'Chiết khẩu phải trả trước >= ' + res.error.title + '% và <= 100%';
        } else if (res.error.errorKey === 'invalidFeePercent2') {
            this.errorMessage = 'Chiết khẩu phải trả sau >= ' + res.error.title + '% và <= 100%';
        } else if (res.error.errorKey === 'invalidFeePercent1b') {
            this.errorMessage = 'Chiết khẩu phải trả trước trong cước >= ' + res.error.title + '% và <= 100%';
        } else if (res.error.errorKey === 'invalidFeePercent2b') {
            this.errorMessage = 'Chiết khẩu phải trả sau trong cước >= ' + res.error.title + '% và <= 100%';
        } else if (res.error.errorKey === 'invalidSystem') {
            this.errorMessage = 'Hệ thống lỗi, vui lòng liên hệ với quản trị viên';
        } else {
            this.errorMessage = 'Số điện thoại đã tồn tại';
        }
        this.errorSwal.show();
        this.isSaving = false;
    }
}
