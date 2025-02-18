import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomePageService {

  constructor() { }
  private activeTabSource = new BehaviorSubject<string>('profile');
  activeTab$ = this.activeTabSource.asObservable();

  setActiveTab(tabId: string) {
    this.activeTabSource.next(tabId);
  }

  resetActiveTab() {
    this.activeTabSource.next('profile'); // Reset về 'profile' khi logout
  }

}
