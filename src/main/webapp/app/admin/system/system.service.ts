import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISystem } from 'app/shared/model/system.model';

type EntityResponseType = HttpResponse<ISystem>;
type EntityArrayResponseType = HttpResponse<ISystem[]>;

@Injectable({ providedIn: 'root' })
export class SystemService {
    public resourceUrl = SERVER_API_URL + 'api/systems';

    constructor(protected http: HttpClient) {}

    create(system: ISystem): Observable<EntityResponseType> {
        return this.http.post<ISystem>(this.resourceUrl, system, { observe: 'response' });
    }

    update(system: ISystem): Observable<EntityResponseType> {
        return this.http.put<ISystem>(this.resourceUrl, system, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISystem>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findOne(): Observable<EntityResponseType> {
        return this.http.get<ISystem>(`${this.resourceUrl}/get-one`, { observe: 'response' });
    }
}
