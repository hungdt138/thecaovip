import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core';
import { PasswordService } from './password.service';
import swal from 'sweetalert2';

@Component({
    selector: 'hg-password',
    templateUrl: './password.component.html'
})
export class PasswordComponent implements OnInit {
    doNotMatch: string;
    error: string;
    success: string;
    account: any;
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;

    constructor(private passwordService: PasswordService, private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.account = account;
        });
    }

    changePassword() {
        if (this.newPassword !== this.confirmPassword) {
            this.error = null;
            this.success = null;
            this.doNotMatch = 'ERROR';
        } else {
            this.doNotMatch = null;
            this.passwordService.save(this.newPassword, this.currentPassword).subscribe(
                () => {
                    this.error = null;
                    this.success = 'OK';
                    swal({
                        title: 'Chúc mừng',
                        text: 'Bạn đã thay đổi mật khẩu thành công!',
                        type: 'success'
                    });
                    this.newPassword = '';
                    this.confirmPassword = '';
                    this.currentPassword = '';
                },
                error => {
                    console.log(error);
                    this.success = null;
                    this.error = 'ERROR';
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'Mật khẩu hiện tại không chính xác',
                        type: 'error'
                    });
                }
            );
        }
    }
}
