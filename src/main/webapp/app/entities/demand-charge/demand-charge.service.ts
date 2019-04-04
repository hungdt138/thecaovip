import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDemandCharge } from 'app/shared/model/demand-charge.model';

type EntityResponseType = HttpResponse<IDemandCharge>;
type EntityArrayResponseType = HttpResponse<IDemandCharge[]>;

@Injectable({ providedIn: 'root' })
export class DemandChargeService {
    public resourceUrl = SERVER_API_URL + 'api/demand-charges';

    constructor(protected http: HttpClient) {}

    create(demandCharge: IDemandCharge): Observable<any> {
        return this.http.post<any>(SERVER_API_URL + 'api/confirm', demandCharge, { observe: 'response' });
    }

    update(demandCharge: IDemandCharge): Observable<EntityResponseType> {
        return this.http.put<IDemandCharge>(this.resourceUrl, demandCharge, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDemandCharge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDemandCharge[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryHand(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDemandCharge[]>(this.resourceUrl + '/hand', { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDemandCharge[]>(this.resourceUrl + '/search', { params: options, observe: 'response' });
    }
}
