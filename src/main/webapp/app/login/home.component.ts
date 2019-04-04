import { Component, AfterViewInit, Renderer, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { AccountService } from 'app/core';
import { UserService } from 'app/core/user/user.service';
import { HttpClient } from '@angular/common/http';
import swal from 'sweetalert2';

@Component({
    selector: 'hg-page-login',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class PageHomeComponent implements OnInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    values: string[];
    isLogin: boolean;
    showPass: boolean;

    publicIP: string;
    response: string;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private principal: AccountService,
        private userService: UserService,
        private router: Router,
        private http: HttpClient
    ) {
        this.credentials = {};
        this.isLogin = false;
        this.publicIP = '';
        this.response = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            if (account) {
                this.isLogin = true;
                if (this.isLogin) {
                    this.router.navigate(['/co']);
                }
            }
        });
    }

    cancel() {
        this.credentials = {
            username: null,
            password: null,
            rememberMe: true
        };
        this.authenticationError = false;
    }

    login() {
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe
            })
            .then(() => {
                this.principal.identity().then(account => {
                    this.navigateLogin(account);
                });
                this.authenticationError = false;
                if (this.router.url === '/register' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                    this.router.navigate(['']);
                }

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                const redirect = this.stateStorageService.getUrl();
                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                }
            })
            .catch(() => {
                this.authenticationError = true;
                swal({
                    title: 'Có lỗi sảy ra',
                    text: 'Thông tin đăng nhập không chính xác',
                    type: 'error'
                });
            });
    }

    navigateLogin(account) {
        if (account !== null || account !== undefined) {
            this.router.navigate(['/bill/one']);
        }
    }

    register() {
        this.router.navigate(['/register']);
    }

    requestResetPassword() {
        this.router.navigate(['/reset', 'request']);
    }
}
