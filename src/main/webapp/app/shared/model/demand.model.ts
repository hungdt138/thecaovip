import { IUser } from 'app/core/user/user.model';

export const enum NhaMang {
    VIETTEL = 'VIETTEL',
    VINA = 'VINA',
    MOBI = 'MOBI'
}

export interface IDemand {
    id?: number;
    account?: string;
    amount?: number;
    type?: NhaMang;
    chargedAmount?: number;
    user?: IUser;
    name?: string;
    serviceType?: number;
}

export class Demand implements IDemand {
    constructor(
        public id?: number,
        public account?: string,
        public amount?: number,
        public type?: NhaMang,
        public chargedAmount?: number,
        public user?: IUser,
        public name?: string
    ) {}
}
