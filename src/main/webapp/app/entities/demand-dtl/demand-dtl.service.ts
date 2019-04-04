import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDemandDtl } from 'app/shared/model/demand-dtl.model';

type EntityResponseType = HttpResponse<IDemandDtl>;
type EntityArrayResponseType = HttpResponse<IDemandDtl[]>;

@Injectable({ providedIn: 'root' })
export class DemandDtlService {
    public resourceUrl = SERVER_API_URL + 'api/demand-dtls';

    constructor(protected http: HttpClient) {}

    create(demandDtl: IDemandDtl): Observable<EntityResponseType> {
        return this.http.post<IDemandDtl>(this.resourceUrl, demandDtl, { observe: 'response' });
    }

    update(demandDtl: IDemandDtl): Observable<EntityResponseType> {
        return this.http.put<IDemandDtl>(this.resourceUrl, demandDtl, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDemandDtl>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDemandDtl[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
