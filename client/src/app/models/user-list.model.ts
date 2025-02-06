import { User } from './user.model';
export interface UserList {
    content : User[];
    totalItems : number;
    totalPages: number;
}