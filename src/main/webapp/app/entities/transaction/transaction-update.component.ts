import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { Account, AccountService, IUser, UserService } from 'app/core';
import { OtpService } from 'app/shared/auth/otp.service';
import { SwalComponent } from '@toverux/ngx-sweetalert2';
import {forEach} from '@angular/router/src/utils/collection';

@Component({
    selector: 'hg-transaction-update',
    templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
    @ViewChild('errorSwal') private errorSwal: SwalComponent;
    @ViewChild('success') private success: SwalComponent;
    transaction: ITransaction;
    isSaving: boolean;
    account: any;
    users: IUser[];
    isRegister: boolean;
    errorMessage: String;
    amount: number;
    isAdmin: boolean;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected transactionService: TransactionService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute,
        private accountService: AccountService,
        private otpService: OtpService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.account = {};
        this.transaction = {};
        this.isRegister = false;
        this.isAdmin = false;
        this.accountService.identity().then((account: Account) => {
            this.account = account;
            for (let i = 0; i < this.account.authorities.length; i++) {
                if (account.authorities[i] === 'ROLE_ADMIN') {
                    this.isAdmin = true;
                    break;
                }
            }
        });
        this.activatedRoute.data.subscribe(({ user }) => {
            this.userService
                .getBranchUser({ pageable: 999 })
                .pipe(
                    filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                    map((response: HttpResponse<IUser[]>) => response.body)
                )
                .subscribe(
                    (res: IUser[]) => {
                        this.users = res;
                        if (user && user.body) {
                            this.users.forEach(user1 => {
                                if (user1.login === user.body.login) {
                                    this.transaction = { toUser: user1 };
                                }
                            });
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        });
        this.userService.getAmount().subscribe(res => {
            this.amount = res.body;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.transactionService
            .create(this.transaction)
            .subscribe((res: HttpResponse<ITransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.success.show();
    }

    protected onSaveError(error) {
        this.isSaving = false;
        if (error.error.errorKey === 'invalidOtp') {
            this.errorMessage = 'Mã OTP không chính xác';
        } else if (error.error.errorKey === 'invalidAmount') {
            this.errorMessage = 'Số tiền không đủ để chuyển khoản';
        } else if (error.error.errorKey === 'noneOtp') {
            this.errorMessage = 'Bạn chưa cài đặt mã OTP';
        } else if (error.error.errorKey === 'expiredOtp') {
            this.errorMessage = 'Mã OTP đã hết hạn!';
        } else if (error.error.errorKey === 'expiredAmount') {
            this.errorMessage = 'Số tiền cần chuyển phải là bội của 1.000 VND!';
        }
        this.errorSwal.show();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    generateOtp(resend) {
        const otp = { code: this.transaction.otp, action: 0, resend };
        this.isSaving = true;
        this.otpService.generateOtp(otp).subscribe(
            () => {
                this.isRegister = true;
                this.isSaving = false;
            },
            () => {}
        );
    }
}
