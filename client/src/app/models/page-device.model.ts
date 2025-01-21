import { Device } from "./device.model";
export interface PageDevice {
    content: Device[];
    totalItems: number;
    totalPages: number;
}