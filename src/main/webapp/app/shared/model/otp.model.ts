import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export const enum Action {
    TRANSFER = 'TRANSFER'
}

export interface IOtp {
    id?: number;
    code?: string;
    expiredDate?: Moment;
    action?: Action;
    user?: IUser;
}

export class Otp implements IOtp {
    constructor(public id?: number, public code?: string, public expiredDate?: Moment, public action?: Action, public user?: IUser) {}
}
