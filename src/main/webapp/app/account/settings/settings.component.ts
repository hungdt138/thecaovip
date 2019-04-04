import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core';

@Component({
    selector: 'hg-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];

    constructor(private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
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
            fullName: account.fullName,
            amount: account.amount,
            availableAmount: account.availableAmount,
            login: account.login
        };
    }
}
