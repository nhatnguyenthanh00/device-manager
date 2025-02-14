import { Component } from '@angular/core';
import { Router, ActivatedRoute  } from '@angular/router';
import { ROUTES } from '../../../core/constants';
@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrl: './error-page.component.css'
})
export class ErrorPageComponent {
  constructor(private router: Router, private route: ActivatedRoute ) {}
  data: any;
  ngOnInit() {
    this.data = {
      ...this.data,
      ...this.route.snapshot.data['notFoundData']
    };
  }

  goToLogin() {
    this.router.navigate(['/'+ROUTES.LOGIN]);
  }

}
