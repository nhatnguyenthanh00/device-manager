import { Component, inject, Input, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DeviceService } from '../../../core/services/device.service';
import { AdminService } from '../../../core/services/admin.service';
import { NewDevice } from '../../../models/new-device.model';
import { ToastrService } from 'ngx-toastr';
import { Device } from '../../../models/device.model';
import Swal from 'sweetalert2';
import { PageDevice } from '../../../models/page-device.model';
@Component({
  selector: 'app-device-manage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './device-manage.component.html',
  styleUrl: './device-manage.component.css',
})
export class DeviceManageComponent {
  @Input() getAll!: string;
  @Input() dataDeviceByAccount: PageDevice | null = null;
  @Output() deviceUpdated = new EventEmitter<any>();
  deviceService = inject(DeviceService);
  adminService = inject(AdminService);
  toastr = inject(ToastrService);

  devices: Device[] = [];
  usernames: any[] = [];
  search: string = '';
  categoryFilter: string = '';
  statusFilter: string = '';
  currentPage: number = 1;
  totalPages: number = 1;
  totalItems: number = 0;
  showCreateDeviceModal: boolean = false;
  showDetailsModal = false;
  newDevice: Partial<NewDevice> = {
    name: '',
    category: '',
    description: '',
    image: '',
  };
  updateDevice: Partial<NewDevice> = {
    name: '',
    category: '',
    description: '',
    image: '',
  };
  errMsg: string = '';

  selectedDevice: Device | null = null;
  selectedDeviceId: String = '';
  selectedDeviceUserName: String = '';
  selectedDeviceStatus: number | null = null;
  selectedOption: string | null = null;
  isDropdownOpen = false;

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  selectOption(option: string) {
    if(this.selectedDeviceUserName == option) this.selectedDeviceUserName = '';
    else{
      this.selectedDeviceUserName = option;
    }
    this.isDropdownOpen = false;
  }

  ngOnInit(): void {
    this.getDevices();
    if(this.getAll == 'user-detail' || this.getAll == 'device-manager') this.getUsernames();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['dataDeviceByAccount'] && 
        changes['dataDeviceByAccount'].currentValue && 
        this.getAll == 'user-detail') {
      this.getDevicesOfUserDetails();
    }
  }

  getDevices(): void {
    if (this.getAll == 'device-manager') this.getAllDevices();
    else if (this.getAll == 'user-detail') this.getDevicesOfUserDetails();
    else if(this.getAll = 'my-device') this.getMyDevices();
  }

  getUsernames(): void {
    this.adminService.getUsernames().subscribe((response) => {
      console.log('In getUsernames', response?.data);
      if(response?.errMsg) this.toastr.error(response?.errMsg, 'Error');
      else this.usernames = response?.data;
    });
  }

  getAllDevices(): void {
    const params = {
      search: this.search,
      category: this.categoryFilter,
      status: this.statusFilter,
      page: this.currentPage,
    };
    this.deviceService.getAllDevice(params).subscribe((response) => {
      this.devices = response?.data.content;
      this.totalPages = response?.data?.totalPages; // Adjust depending on your response structure
      this.totalItems = response?.data?.totalItems;
    });
  }

  getMyDevices(): void {
    console.log('Hahaa',this.getAll);
    const params = {
      search: this.search,
      category: this.categoryFilter,
      status: this.statusFilter,
      page: this.currentPage,
    };
    this.deviceService.getMyDevice(params).subscribe((response) => {
      this.devices = response?.data.content;
      this.totalPages = response?.data?.totalPages; // Adjust depending on your response structure
      this.totalItems = response?.data?.totalItems;
    });
  }

  getDevicesOfUserDetails(): void {
    if (this.dataDeviceByAccount) {
      this.devices = this.dataDeviceByAccount.content;
      this.totalPages = this.dataDeviceByAccount.totalPages;
      this.totalItems = this.dataDeviceByAccount.totalItems;
    }
  }

  getStatusStr(device: Device): string {
    if (device.status === -1) return 'NA';
    if (device.status === 0) return `AT ${device.username}`;
    if (device.status === 1) return `RR ${device.username}`;
    return '';
  }

  onFilterChange() {
    this.currentPage = 1;
    this.getDevices();
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.deviceUpdated.emit({page: this.currentPage}); 
      this.getDevices();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.deviceUpdated.emit({page: this.currentPage}); 
      this.getDevices();
    }
  }

  onCreateNewDevice() {
    this.showCreateDeviceModal = true;
    this.newDevice = {
      name: '',
      category: '',
      description: '',
      image: '',
    };
  }

  closeModal() {
    this.showCreateDeviceModal = false;
  }

  onImageChange(event: any) {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        this.newDevice.image = reader.result as string;
      };
    }
  }

  validateInput(): boolean {
    if (!this.newDevice.name) {
      this.errMsg = 'Name is required';
      return false;
    } else {
      const trimmedName = this.newDevice.name.trim();
      this.newDevice.name = trimmedName;
    }

    // Gender validation
    if (!this.newDevice.category) {
      this.errMsg = 'Category is required';
      return false;
    }
    return true;
  }

  createDevice(event: Event) {
    event.preventDefault();
    if (this.validateInput()) {
      this.deviceService
        .addDevice(this.newDevice as NewDevice)
        .subscribe((response) => {
          if (response?.errMsg) {
            this.toastr.error(response?.errMsg, 'Error');
          } else {
            this.toastr.success('Device created successfully!', 'Success');
            this.getDevices();
            this.closeModal();
          }
        });
    }
  }

  viewDetails(device: Device) {

    console.log('In viewDetails', this.usernames);
    this.selectedDeviceId = device.id;
    this.selectedDeviceUserName =
      device.username == null ? '' : device.username;
    this.selectedDeviceStatus = device.status;

    this.updateDevice.name = device.name;
    this.updateDevice.category = device.category;
    this.updateDevice.description = device.description;
    this.updateDevice.image = device.image;
    this.showDetailsModal = true;
  }

  getStatusViewDetail(): string {
    if (this.selectedDeviceStatus === 0) return `AT`;
    if (this.selectedDeviceStatus === 1) return `RR`;
    return '';
  }

  closeModalDetails() {
    this.selectedDevice = null;
    this.showDetailsModal = false;
    this.isDropdownOpen = false;
  }

  onImageUpdateChange(event: any) {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        this.updateDevice.image = reader.result as string;
      };
    }
  }

  updateDeviceInfo(event: Event) {
    event.preventDefault();
    const payload = {
      deviceId: this.selectedDeviceId,
      name: this.updateDevice.name,
      description: this.updateDevice.description,
      category: this.updateDevice.category,
      image: this.updateDevice.image,
      userName: this.selectedDeviceUserName,
    };
    this.deviceService.updateDevice(payload).subscribe((response) => {
      if (response?.errMsg) {
        this.toastr.error(response?.errMsg, 'Error');
      } else {
        this.toastr.success('Device updated successfully!', 'Success');
        if(this.getAll == 'device-manager') this.getDevices()
        else if(this.getAll == 'user-detail') this.deviceUpdated.emit({page: this.currentPage}); 
        this.closeModalDetails();
      }
    });
  }

  // Delete Device
  deleteDevice(deviceId: string, deviceName: string) {
    Swal.fire({
      title: 'Are you sure?',
      text: `Do you want to delete device ${deviceName}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        // call API delete
        this.deviceService.deleteDevice(deviceId).subscribe((response) => {
          if (response?.errMsg) {
            this.toastr.error(response?.errMsg, 'Error');
          } else {
            this.toastr.success(`Delete user ${deviceName} succes!`, 'Success');
            this.deviceUpdated.emit({page: this.currentPage}); 
            this.getDevices();
          }
        });
      }
    });
  }

  requestReturnDevice(device: Device) {
    if(device.status == 1) {
      this.toastr.error('Device is already have request return!', 'Error');
      return;
    }

    Swal.fire({
      title: 'Are you sure?',
      text: `Do you want to return device ${device.name}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, return it!',
      cancelButtonText: 'No, cancel',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        this.deviceService.requestReturnDevice(device.id).subscribe((response) => {
          if (response?.errMsg) {
            this.toastr.error(response?.errMsg, 'Error');
          } else {
            this.toastr.success('Request return device successfully!', 'Success');
            this.deviceUpdated.emit({page: this.currentPage}); 
            this.getDevices();
          }
        });
      }
    })
  }

  acceptRequestReturnDevice(device: Device) {
    if(device.status != 1) {
      this.toastr.error('Device does not have request return!', 'Error');
      return;
    }

    Swal.fire({
      title: 'Are you sure?',
      text: `Do you want to accept return device ${device.name}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, accept!',
      cancelButtonText: 'No, cancel',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        this.deviceService.acceptReturnDevice(device.id).subscribe((response) => {
          if (response?.errMsg) {
            this.toastr.error(response?.errMsg, 'Error');
          } else {
            this.toastr.success('Accept return device successfully!', 'Success');
            this.deviceUpdated.emit({page: this.currentPage}); 
            this.getDevices();
          }
        });
      }
    })
  }
}
