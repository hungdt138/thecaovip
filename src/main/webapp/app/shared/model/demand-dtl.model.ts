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

export interface IDemandDtl {
    id?: number;
    denomination?: Denomination;
    quantity?: number;
    demand?: IDemand;
    price?: number;
}

export class DemandDtl implements IDemandDtl {
    constructor(public id?: number, public denomination?: Denomination, public quantity?: number, public demand?: IDemand) {}
}
