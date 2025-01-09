import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule  } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'DM';
  constructor(private http: HttpClient) {
    this.http.get('https://jsonplaceholder.typicode.com/posts').subscribe({
      next: (data) => console.log('Response:', data),
      error: (err) => console.error('Error:', err),
    });
  }
}
