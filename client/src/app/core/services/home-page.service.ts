import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class HomePageService {

  constructor() { }
  private activeTabSource = new Subject<string>();
  activeTab$ = this.activeTabSource.asObservable();

  setActiveTab(tabId: string) {
    this.activeTabSource.next(tabId);
  }
}
