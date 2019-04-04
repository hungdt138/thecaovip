import { IDemand } from 'app/shared/model/demand.model';

export const enum Denomination {
    DENO10 = 'DENO10',
    DENO20 = 'DENO20',
    DENO50 = 'DENO50',
    DENO100 = 'DENO100',
    DENO200 = 'DENO200',
    DENO500 = 'DENO500',
    DENO1000 = 'DENO1000'
}

export interface IDemandCharge {
    id?: number;
    denomination?: Denomination;
    code?: string;
    serial?: string;
    demand?: IDemand;
    price?: number;
    inputValue?: number;
    realValue?: number;
    status?: number;
}

export class DemandCharge implements IDemandCharge {
    constructor(
        public id?: number,
        public denomination?: Denomination,
        public code?: string,
        public serial?: string,
        public demand?: IDemand,
        public price?: number
    ) {}
}
