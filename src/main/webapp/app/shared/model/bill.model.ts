export const enum NhaMang {
    VTT = 'VTT',
    VNP = 'VNP',
    VMS = 'VMS'
}

export interface IBill {
    chargeType?: number;
    highPriority?: boolean;
    id?: number;
    amount?: number;
    type?: NhaMang;
    status?: number;
    name?: string;
    moreFee?: boolean;
    chargedAmount?: number;
    partnerId?: number;
    account?: number;
}

export class Bill implements IBill {
    constructor(
        public id?: number,
        public amount?: number,
        public type?: NhaMang,
        public status?: number,
        public name?: string,
        public highPriority?: boolean,
        public chargeType?: number
    ) {
        this.status = this.status || 0;
        this.amount = 0;
        this.name = undefined;
        this.highPriority = true;
        this.chargeType = 1;
    }
}
