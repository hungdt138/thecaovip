import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert2';

import { AccountService } from 'app/core';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'hg-settings',
    templateUrl: './authentication.component.html'
})
export class AuthenticationComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    twoFa: any;
    code: string;
    imageToShow: any;
    isAuth: boolean;

    constructor(private accountService: AccountService, private http: HttpClient) {}

    ngOnInit() {
        this.twoFa = {};
        this.accountService.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.accountService.get2fa().subscribe(res => {
            this.twoFa = res.body;
            this.imageToShow = 'data:image/png;base64,' + this.twoFa.url;
        });
        this.accountService.isAuth().subscribe(res => {
            console.log(res.body);
            this.isAuth = res.body;
        });
    }

    save() {
        this.accountService.save(this.settingsAccount).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.accountService.identity(true).then(account => {
                    this.settingsAccount = this.copyAccount(account);
                });
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
            }
        );
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            fullName: account.fullName,
            langKey: account.langKey,
            login: account.login,
            imageUrl: account.imageUrl,
            isAuth: account.isAuth
        };
    }

    confirm2fa() {
        this.accountService.confirm2fa({ code: this.code, privateKey: this.twoFa.privateKey }).subscribe(
            res => {
                swal({
                    title: 'Chúc mừng!',
                    text: 'Bạn đã cài đặt bảo mật 2 lớp thành công',
                    type: 'success'
                });
                this.settingsAccount.isAuth = true;
            },
            res => {
                if (res.error.errorKey === 'invalidCode') {
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'Mã xác nhận không đúng',
                        type: 'error'
                    });
                } else if (res.error.errorKey === 'created') {
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'Tài khoản của bạn đã tạo bảo mật 2 lớp rồi',
                        type: 'error'
                    });
                }
            }
        );
    }

    cancel2fa() {
        this.accountService.cancel2fa({ code: this.code }).subscribe(
            res => {
                swal({
                    title: 'Chúc mừng!',
                    text: 'Bạn đã hủy cài đặt bảo mật 2 lớp thành công',
                    type: 'success'
                });
                this.settingsAccount.isAuth = false;
            },
            res => {
                if (res.error.errorKey === 'invalidCode') {
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'Mã xác nhận không đúng',
                        type: 'error'
                    });
                } else if (res.error.errorKey === 'notCreated') {
                    swal({
                        title: 'Có lỗi sảy ra',
                        text: 'Tài khoản của bạn chưa tạo bảo mật 2 lớp rồi',
                        type: 'error'
                    });
                }
            }
        );
    }
}
