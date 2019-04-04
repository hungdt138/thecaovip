import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBill } from 'app/shared/model/bill.model';

type EntityResponseType = HttpResponse<IBill>;
type EntityArrayResponseType = HttpResponse<IBill[]>;

@Injectable({ providedIn: 'root' })
export class BillService {
    public resourceUrl = SERVER_API_URL + 'api/bills';

    constructor(protected http: HttpClient) {}

    create(bill: any): Observable<EntityResponseType> {
        return this.http.post<IBill>(this.resourceUrl, bill, { observe: 'response' });
    }

    update(bill: any): Observable<EntityResponseType> {
        return this.http.put<IBill>(this.resourceUrl, bill, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBill>(`${this.resourceUrl}/charge/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBill[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getSheetName(file: any): Observable<EntityResponseType> {

        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        const url = this.resourceUrl + '/read-sheet-name';
        return this.http.post<any>(url, formData, { observe: 'response' });
    }

    readSheet(file: any, sheet: any) {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        const url = this.resourceUrl + '/read-sheet';
        formData.append('sheetNumber', sheet);
        return this.http.post<any>(url, formData, { observe: 'response' });
    }
}
