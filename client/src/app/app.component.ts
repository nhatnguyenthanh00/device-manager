import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule  } from '@angular/forms';
import { ToastrService} from 'ngx-toastr';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    FormsModule,
    CommonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [ToastrService]
})
export class AppComponent {
  title = 'Lucifer';
  constructor(private toastr: ToastrService) {}
}
