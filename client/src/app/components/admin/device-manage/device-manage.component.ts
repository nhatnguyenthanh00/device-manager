import {
  Component,
  inject,
  Input,
  SimpleChanges,
  Output,
  EventEmitter,
} from '@angular/core';
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
  @Input() getAll!: string; // Determines the type of device data being displayed (user-detail, device-manager, my-device)
  @Input() dataDeviceByAccount: PageDevice | null = null; // Holds data related to devices associated with a specific account
  @Output() deviceUpdated = new EventEmitter<any>(); // Emits an event when a device is updated (for pagination)

  // Service injections
  deviceService = inject(DeviceService);
  adminService = inject(AdminService);
  toastr = inject(ToastrService);

  devices: Device[] = []; // Stores the list of devices to display
  usernames: any[] = []; // Stores the list of usernames for filtering devices
  search: string = ''; // Holds the search term
  categoryFilter: string = ''; // Holds the selected category filter
  statusFilter: string = ''; // Holds the selected status filter
  currentPage: number = 1; // Tracks the current page of the device list
  totalPages: number = 1; // Total number of pages for pagination
  totalItems: number = 0; // Total number of items (devices) for pagination
  showCreateDeviceModal: boolean = false; // Controls visibility of the device creation modal
  showDetailsModal: boolean = false; // Controls visibility of the device details modal
  // New and updated device data
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
  errMsg: string = ''; // Error message for validation

  selectedDeviceId: String = ''; // Holds the selected device's ID for editing or deletion
  selectedDeviceUserName: String = ''; // Holds the username associated with the selected device
  selectedDerviceUserId: string | null = null;
  selectedDeviceStatus: number | null = null; // Holds the status of the selected device
  selectedOption: string | null = null; // For handling dropdown selection
  isDropdownOpen = false; // Controls dropdown visibility

  /**
   * Toggles the dropdown visibility
   */
  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  /**
   * Handles the selection of a username from the dropdown
   * @param option The selected username
   */
  selectOption(option: any) {
    if (this.selectedDeviceUserName === option.username) {
      this.selectedDeviceUserName = '';
      this.selectedDerviceUserId = null;
    } else {
      this.selectedDeviceUserName = option.username;
      this.selectedDerviceUserId = option.id;
    }
    this.isDropdownOpen = false;
  }

  ngOnInit(): void {
    this.getDevices();
    if (this.getAll == 'user-detail' || this.getAll == 'device-manager')
      this.getUsernames();
  }

  /**
   * Handles changes to the input dataDeviceByAccount
   *
   * @param changes The changes to the input dataDeviceByAccount
   */
  ngOnChanges(changes: SimpleChanges) {
    if (
      changes['dataDeviceByAccount'] &&
      changes['dataDeviceByAccount'].currentValue &&
      this.getAll == 'user-detail'
    ) {
      this.getDevicesOfUserDetails();
    }
  }

  /**
   * Gets the devices based on the value of the getAll property.
   * If getAll is 'device-manager', it calls the getAllDevices method.
   * If getAll is 'user-detail', it calls the getDevicesOfUserDetails method.
   * If getAll is 'my-device', it calls the getMyDevices method.
   */
  getDevices(): void {
    if (this.getAll == 'device-manager') {
      this.getAllDevices();
    } else if (this.getAll == 'user-detail') {
      this.getDevicesOfUserDetails();
    } else if (this.getAll == 'my-device') {
      this.getMyDevices();
    }
  }

  /**
   * Fetches the list of usernames from the backend.
   * This method is called when the component is initialized and when the
   * input dataDeviceByAccount has changed.
   */
  getUsernames(): void {
    this.adminService.getUsernames().subscribe({
      next: (response) => {
        this.usernames = response;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      },
    });
  }

  /**
   * Fetches all devices based on the current search, category, status, and page filters.
   * Updates the devices, total pages, and total items properties with the fetched data.
   */
  getAllDevices(): void {
    const params = {
      search: this.search,
      category: this.categoryFilter,
      status: this.statusFilter,
      page: this.currentPage,
    };

    this.deviceService.getAllDevice(params).subscribe({
      next: (response) => {
        this.devices = response?.content;
        this.totalPages = response?.totalPages;
        this.totalItems = response?.totalItems;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
        this.devices = [];
        this.totalPages = 1;
        this.totalItems = 0;
      },
    });
  }

  /**
   * Fetches devices owned by the current user based on the current search,
   * category, status, and page filters. Updates the devices, total pages,
   * and total items properties with the fetched data.
   */
  getMyDevices(): void {
    const params = {
      search: this.search,
      category: this.categoryFilter,
      status: this.statusFilter,
      page: this.currentPage,
    };

    this.deviceService.getMyDevice(params).subscribe({
      next: (response) => {
        this.devices = response?.content;
        this.totalPages = response?.totalPages;
        this.totalItems = response?.totalItems;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
        this.devices = [];
        this.totalPages = 1;
        this.totalItems = 0;
      },
    });
  }

  /**
   * Fetches the list of devices associated with the user whose data is
   * provided in the dataDeviceByAccount input property.
   * Updates the devices, total pages, and total items properties with the
   * fetched data.
   */
  getDevicesOfUserDetails(): void {
    if (this.dataDeviceByAccount) {
      this.devices = this.dataDeviceByAccount.content;
      this.totalPages = this.dataDeviceByAccount.totalPages;
      this.totalItems = this.dataDeviceByAccount.totalItems;
    }
  }

  /**
   * Converts the status of the device to a human-readable string.
   * @param device The device whose status should be converted.
   * @returns A string representing the status of the device. 'NA' for not assigned,
   * 'AT <username>' for assigned to <username>, 'RR <username>' for request return
   * to <username>.
   */
  getStatusStr(device: Device): string {
    if (device.status === -1) return 'NA';
    if (device.status === 0) return `AT ${device.username}`;
    if (device.status === 1) return `RR ${device.username}`;
    return '';
  }

  /**
   * Triggers the getDevices() function when the user selects a new option in the
   * search, category, or status filters. Also resets the current page to 1.
   */
  onFilterChange() {
    this.currentPage = 1;
    this.getDevices();
  }

  /**
   * Triggers the getDevices() function when the user navigates to the previous page.
   * Also decrements the current page number.
   */
  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.deviceUpdated.emit({ page: this.currentPage });
      this.getDevices();
    }
  }

  /**
   * Triggers the getDevices() function when the user navigates to the next page.
   * Also increments the current page number.
   */
  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.deviceUpdated.emit({ page: this.currentPage });
      this.getDevices();
    }
  }

  /**
   * Triggers the creation of a new device by showing a modal to enter the
   * device's name, category, description, and image.
   */
  onOpenModalCreateNewDevice() {
    this.showCreateDeviceModal = true;
    // Reset the values of the new device
    this.newDevice = {
      name: '',
      category: '',
      description: '',
      image: '',
    };
  }

  /**
   * Hides the modal to create a new device.
   */
  closeModal() {
    // Reset the modal
    this.showCreateDeviceModal = false;
  }

  /**
   * Handles the change event for the image input.
   * Reads the selected file and updates the newDevice's image property with the base64 string.
   *
   * @param event The change event triggered by the file input
   */
  onImageChange(event: any, type: string) {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      const reader = new FileReader();

      reader.readAsDataURL(file);

      reader.onloadend = () => {
        if (type === 'create') this.newDevice.image = reader.result as string;
        else if (type === 'update')
          this.updateDevice.image = reader.result as string;
        // this.newDevice.image = reader.result as string;
      };
    }
  }

  /**
   * Validates the input in the create new device modal.
   * If any of the input is invalid, sets the error message and returns false.
   * Otherwise, trims the name and returns true.
   * @returns boolean
   */
  validateInput(): boolean {
    if (!this.newDevice.name) {
      this.errMsg = 'Name is required';
      return false;
    } else {
      const trimmedName = this.newDevice.name.trim();
      this.newDevice.name = trimmedName;
    }

    if (!this.newDevice.category) {
      this.errMsg = 'Category is required';
      return false;
    }
    return true;
  }

  /**
   * Handles the creation of a new device.
   * Validates the input, submits the device data to the server, and updates the device list on success.
   *
   * @param event The form submission event
   */
  createDevice(event: Event) {
    event.preventDefault();

    if (this.validateInput()) {
      this.deviceService.addDevice(this.newDevice as NewDevice).subscribe({
        next: (response) => {
          this.toastr.success('Device created successfully!', 'Success');
          this.getDevices();
          this.closeModal();
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        },
      });
    }
  }

  /**
   * Shows the details of a device in a modal dialog.
   * Sets the 'selectedDeviceId', 'selectedDeviceUserName', 'selectedDeviceStatus',
   * and 'updateDevice' properties to the device's values.
   * And then opens the modal dialog.
   *
   * @param device The device to show details
   */
  viewDetails(device: Device) {
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

  /**
   * Converts the status of the device to a human-readable string to be used in the details view.
   * 'AT' for assigned to, 'RR' for request return.
   * @returns A string representing the status of the device.
   */
  getStatusViewDetail(): string {
    if (this.selectedDeviceStatus === 0) return 'AT';
    if (this.selectedDeviceStatus === 1) return 'RR';
    return '';
  }

  /**
   * Closes the device details modal and resets related states.
   */
  closeModalDetails() {
    this.showDetailsModal = false;
    this.isDropdownOpen = false;
  }

  /**
   * Handles the update of a device's information.
   * Validates the input, submits the updated device data to the server, and updates the device list on success.
   *
   * @param event The form submission event
   */
  updateDeviceInfo(event: Event) {
    event.preventDefault();
    const payload = {
      id: this.selectedDeviceId,
      name: this.updateDevice.name,
      description: this.updateDevice.description,
      category: this.updateDevice.category,
      image: this.updateDevice.image,
      // userName: this.selectedDeviceUserName,
      userId: this.selectedDerviceUserId,
    };

    this.deviceService.updateDevice(payload).subscribe({
      next: (response) => {
        this.toastr.success('Device updated successfully!', 'Success');
        if (this.getAll == 'device-manager') this.getDevices();
        else if (this.getAll == 'user-detail')
          this.deviceUpdated.emit({ page: this.currentPage });
        this.closeModalDetails();
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      },
    });
  }

  /**
   * Deletes a device by its ID.
   * Shows a confirmation dialog before deleting the device.
   * If confirmed, calls the delete device API and updates the device list on success.
   * @param deviceId The ID of the device to delete
   * @param deviceName The name of the device to delete
   */
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

        this.deviceService.deleteDevice(deviceId).subscribe({
          next: (response) => {
            this.toastr.success(
              `Delete device ${deviceName} succes!`,
              'Success'
            );
            this.deviceUpdated.emit({ page: this.currentPage });
            this.getDevices();
          },
          error: (err) => {
            this.toastr.error(err.error?.errMsg, 'Error');
          },
        });
      }
    });
  }

  /**
   * Requests to return a device.
   * Shows a confirmation dialog before submitting the request.
   * If confirmed, calls the request return device API and updates the device list on success.
   * @param device The device to request return
   */
  requestReturnDevice(device: Device) {
    if (device.status == 1) {
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
        this.deviceService.requestReturnDevice(device.id).subscribe({
          next: (response) => {
            this.toastr.success(
              'Request return device successfully!',
              'Success'
            );
            this.deviceUpdated.emit({ page: this.currentPage });
            this.getDevices();
          },
          error: (err) => {
            this.toastr.error(err.error?.errMsg, 'Error');
          },
        });
      }
    });
  }

  /**
   * Accepts the return request for a device.
   * Validates the device status before showing a confirmation dialog.
   * If confirmed, calls the accept return device API and updates the device list on success.
   *
   * @param device The device for which to accept the return request
   */
  acceptRequestReturnDevice(device: Device) {
    if (device.status != 1) {
      this.toastr.error('Device does not have request return!', 'Error');
      return;
    }

    Swal.fire({
      title: 'Are you sure?',
      text: `Do you want to accept return device ${device.name}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, accept!',
      cancelButtonText: 'No, refuse!',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        this.deviceService.acceptReturnDevice(device.id).subscribe({
          next: (response) => {
            this.toastr.success(
              'Accept return device successfully!',
              'Success'
            );
            this.deviceUpdated.emit({ page: this.currentPage });
            this.getDevices();
          },
          error: (err) => {
            this.toastr.error(err.error?.errMsg, 'Error');
          },
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        this.deviceService.refuseReturnDevice(device.id).subscribe({
          next: (response) => {
            this.toastr.success(
              'Refuse return device successfully!',
              'Success'
            );
            this.deviceUpdated.emit({ page: this.currentPage });
            this.getDevices();
          },
          error: (err) => {
            this.toastr.error(err.error?.errMsg, 'Error');
          },
        });
      }
    });
  }

  canDelete(device: Device): boolean {
    // Điều kiện để cho phép xóa, ví dụ: chỉ Admin mới được xóa
    return device.status === -1;
  }

  canAccpetReturn(device: Device): boolean {
    // Điều kiện để cho phép xóa, ví dụ: chỉ Admin mới được xóa
    return device.status === 1;
  }

  canRequestReturn(device: Device): boolean {
    // Điều kiện để cho phép xóa, ví dụ: chỉ Admin mới được xóa
    return device.status === 0;
  }
}
