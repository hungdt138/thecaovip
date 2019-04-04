import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITransaction } from 'app/shared/model/transaction.model';
import {IOtp, Otp} from 'app/shared/model/otp.model';

type EntityResponseType = HttpResponse<Otp>;

@Injectable({ providedIn: 'root' })
export class OtpService {
    public resourceUrl = SERVER_API_URL + 'api/p/otp/send';

    constructor(protected http: HttpClient) {}

    generateOtp(otp): Observable<EntityResponseType> {
        return this.http.post<IOtp>(this.resourceUrl, otp, { observe: 'response' });
    }
}
