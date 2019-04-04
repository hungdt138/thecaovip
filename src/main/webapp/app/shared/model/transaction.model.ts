import { IUser } from 'app/core/user/user.model';

export interface ITransaction {
    otp?: any;
    id?: number;
    amount?: number;
    fromUser?: IUser;
    toUser?: IUser;
    action?: number
}

export class Transaction implements ITransaction {
    constructor(public id?: number, public amount?: number, public fromUser?: IUser, public toUser?: IUser) {}
}
