export interface Profile {
    firstName: string;
    lastName: string;
    email: string;
    country: string;
    userName: string;
    isPrivate:  boolean;
    contributions: Map<number, number>;
}