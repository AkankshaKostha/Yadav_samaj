import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { MembershipUser } from '../models/MembershipUser';



@Injectable({ providedIn: 'root' })
export class CurrentUserService {
  user$ = new BehaviorSubject<MembershipUser | null>(null);

  setUser(user: MembershipUser) {
    this.user$.next(user);
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  getUser(): MembershipUser | null {
    const data = localStorage.getItem('currentUser');
    return data ? JSON.parse(data) : null;
  }

  clearUser() {
    this.user$.next(null);
    localStorage.removeItem('currentUser');
  }
}
