export class Account {
    constructor(
        public activated: boolean,
        public authorities: string[],
        public email: string,
        public fullName: string,
        public langKey: string,
        public amount: number,
        public login: string,
        public imageUrl: string
    ) {}
}
