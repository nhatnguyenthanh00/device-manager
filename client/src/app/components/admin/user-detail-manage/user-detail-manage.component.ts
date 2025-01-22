import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HomePageService } from '../../../core/services/home-page.service';
import { AdminService } from '../../../core/services/admin.service';
import { Subscription } from 'rxjs';
import { DeviceManageComponent } from '../device-manage/device-manage.component';
@Component({
  selector: 'app-user-detail-manage',
  standalone: true,
  imports: [DeviceManageComponent, CommonModule, FormsModule],
  templateUrl: './user-detail-manage.component.html',
  styleUrl: './user-detail-manage.component.css'
})
export class UserDetailManageComponent {
  listDevice: any;
  userId : string|null = null;
  name: string = '';
  username: string = '';
  gender: string = '';
  password: string = '';
  showPassword: boolean = false;
  newPassword: string = '';
  adminPassword: string = '';
  totalDevice: number = 0;
  role: string = '';
  private subscription: Subscription;
  constructor(private homePageService: HomePageService, private adminService: AdminService) {
    this.subscription = new Subscription();
  }
  ngOnInit() {
    this.subscription = this.homePageService.detailUserId$.subscribe((userId) => {
      this.userId = userId;
      this.getUserDetails();
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  togglePasswordVisibility(){
    this.showPassword = !this.showPassword;
  }

  getUserDetails(page: number = 1) {
    if(this.userId == null) return
    this.adminService.getUser(this.userId, page).subscribe((res)=>{
      if(res.errMsg){
        alert(res?.errMsg);
      }
      else{
        this.name = res?.data?.userDetail?.name;
        this.username = res?.data?.userDetail?.username;
        this.gender = res?.data?.userDetail?.gender;
        this.totalDevice = res?.data?.devices?.content?.length;
        this.listDevice = res?.data?.devices;
        this.role = res?.data?.userDetail?.role;
      }
    }
      
    )
  }

  onDeviceUpdated(event: { page: number }) {
    console.log('hay hay update')
    this.getUserDetails(event.page);
  }

  generatePassword() {
    const upperCaseChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const lowerCaseChars = 'abcdefghijklmnopqrstuvwxyz';
    const digits = '0123456789';
    const specialChars = '!@#$%^&*()';
    const allChars = upperCaseChars + lowerCaseChars + digits + specialChars;
  
    // Ensure at least one character from each required category
    const password = [
      upperCaseChars[Math.floor(Math.random() * upperCaseChars.length)], // One uppercase
      lowerCaseChars[Math.floor(Math.random() * lowerCaseChars.length)], // One lowercase
      digits[Math.floor(Math.random() * digits.length)], // One digit
      specialChars[Math.floor(Math.random() * specialChars.length)] // One special char
    ];
  
    // Fill the remaining characters with random choices from all available characters
    while (password.length < 8) {
      password.push(allChars[Math.floor(Math.random() * allChars.length)]);
    }
  
    // Shuffle the password to randomize the order
    this.newPassword = password
      .sort(() => Math.random() - 0.5)
      .join('');
  }


  onSubmit() {
  } // Logic to update user information console.log('User information updated:', this.user); }
}
