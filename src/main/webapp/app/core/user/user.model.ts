export interface IUser {
    id?: any;
    login?: string;
    fullName?: string;
    phoneNumber?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    feePercentLv1?: number;
    feePercentLv2?: number;
    feePercentLv1b?: number;
    feePercentLv2b?: number;
    amount?: number;
    availableAmount?: number;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public fullName?: string,
        public phoneNumber?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public feePercentLv1?: number,
        public feePercentLv2?: number,
        public feePercentLv1b?: number,
        public feePercentLv2b?: number,
        public amount?: number,
        public availableAmount?: number
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.fullName = fullName ? fullName : null;
        this.phoneNumber = phoneNumber ? phoneNumber : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.amount = amount ? amount : 0;
        this.availableAmount = availableAmount ? availableAmount : 0;
        this.feePercentLv1 = feePercentLv1 ? feePercentLv1 : 100;
        this.feePercentLv2 = feePercentLv2 ? feePercentLv2 : 100;
        this.feePercentLv1b = feePercentLv1b ? feePercentLv1b : 100;
        this.feePercentLv2b = feePercentLv2b ? feePercentLv2b : 100;
    }
}
