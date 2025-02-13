import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomePageService {

  constructor() { }
  private activeTabSource = new Subject<string>();
  private detailUserId = new BehaviorSubject<string | null>(null);
  activeTab$ = this.activeTabSource.asObservable();
  detailUserId$ = this.detailUserId.asObservable();

  setActiveTab(tabId: string) {
    this.activeTabSource.next(tabId);
  }

  setDetailUserId(userId: string) {
    this.detailUserId.next(userId);
  }

  getCurrentUserId(): string | null {
    return this.detailUserId.getValue();
  }
}
