import { Component, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DeviceService } from '../../services/device.service';
@Component({
  selector: 'app-create-device',
  templateUrl: './create-device.component.html',
  styleUrl: './create-device.component.css',
})
export class CreateDeviceComponent {
  @Output() deviceCreated = new EventEmitter<void>();
  @Output() closeModal = new EventEmitter<void>();
  constructor(private deviceService: DeviceService, private toastr: ToastrService) {}

  showCreateDeviceModal: boolean = false; // Controls visibility of the device creation modal
  newDeviceForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(2)]),
    category: new FormControl('', Validators.required),
    description: new FormControl(''),
    image: new FormControl(''),
  });
  errCreateMsg: { [key: string]: string } = {};

  onImageChange(event: any) {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      const reader = new FileReader();

      reader.readAsDataURL(file);

      reader.onloadend = () => {
        this.newDeviceForm.get('image')?.setValue(reader.result as string);      
      };
    }
  }

  validateForm(): boolean {
    this.errCreateMsg = {};
    for (const key in this.newDeviceForm.controls) {
      const control = this.newDeviceForm.get(key);
      if (control?.invalid) {
        if (control.errors?.['required']) {
          this.errCreateMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} is required.`;
        } else if (control.errors?.['minlength']) {
          this.errCreateMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} must be at least ${
            control.errors['minlength'].requiredLength
          } characters.`;
        }
      }
    }
    return Object.keys(this.errCreateMsg).length === 0;
  }

  createDevice(event: Event) {
    event.preventDefault();
    if(this.validateForm()){
      const newDevice = this.newDeviceForm.getRawValue();
      this.deviceService.addDevice(newDevice).subscribe({
        next: (response) => {
          this.toastr.success('Device created successfully!', 'Success');
          this.deviceCreated.emit();
          this.closeModal.emit();
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        },
      });
    }
  }
}
