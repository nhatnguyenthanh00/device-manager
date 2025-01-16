import { User } from './user.model';
export interface UserList {
    data : User[];
    totalItems : number;
    totalPages: number;
}