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

  createDevice(event: Event) {
    event.preventDefault();
    if(this.newDeviceForm.valid){
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
