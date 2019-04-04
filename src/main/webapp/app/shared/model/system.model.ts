export interface ISystem {
    id?: number;
    highFreeFrom?: number;
    hignFeeTo?: number;
    feePercentLv1?: number;
    feePercentLv2?: number;
    feePercentLv1b?: number;
    feePercentLv2b?: number;
    viettelPromotionDate?: number;
    mobiPromotionDate?: number;
    vinaPromotionDate?: number;
    lockVina?: boolean;
    lockMobi?: boolean;
    lockViettel?: boolean;
    feePercentLv1Vina?: number;
    feePercentLv2Vina?: number;
    feePercentLv1Mobi?: number;
    feePercentLv2Mobi?: number;
}

export class System implements ISystem {
    constructor(
        public id?: number,
        public highFreeFrom?: number,
        public hignFeeTo?: number,
        public feePercentLv1?: number,
        public feePercentLv2?: number
    ) {}
}
