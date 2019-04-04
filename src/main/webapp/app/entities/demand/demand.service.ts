import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDemand } from 'app/shared/model/demand.model';

type EntityResponseType = HttpResponse<IDemand>;
type EntityArrayResponseType = HttpResponse<IDemand[]>;

@Injectable({ providedIn: 'root' })
export class DemandService {
    public resourceUrl = SERVER_API_URL + 'api/demands';

    constructor(protected http: HttpClient) {}

    create(demand: IDemand): Observable<EntityResponseType> {
        return this.http.post<IDemand>(this.resourceUrl, demand, { observe: 'response' });
    }

    update(demand: IDemand): Observable<EntityResponseType> {
        return this.http.put<IDemand>(this.resourceUrl, demand, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDemand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDemand[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
