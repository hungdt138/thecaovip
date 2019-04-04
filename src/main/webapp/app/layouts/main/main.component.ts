import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { Title } from '@angular/platform-browser';

@Component({
    selector: 'hg-main',
    templateUrl: './main.component.html',
    styleUrls: ['main.css']
})
export class JhiMainComponent implements OnInit {
    show: boolean;
    constructor(private titleService: Title, private router: Router) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'bantheApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.show = false;
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
            if (event instanceof NavigationError && event.error.status === 404) {
                this.router.navigate(['/page-login']);
            }
        });
        this.router.events.subscribe(val => {
            if (val instanceof NavigationEnd) {
                this.show = val['url'] !== '/page-login';
            }
        });
    }
}
