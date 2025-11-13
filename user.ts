import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

// Literal type for user type
export type UserType = 'matrimonial' | 'membership';
export type UserStatus = 'PENDING' | 'APPROVED' | 'REJECTED';


// Base User interface

export interface User {
  id?: number;
  fullName: string;
  phone: string;
  email?: string;
  dob?: string;
  gender?: string;
  education?: string;
  membershipType?: string;
  membershipPlan?: string;
  approved?: boolean;
  membershipPaid?: boolean;
  role?: 'ADMIN' | 'USER';
  photoFileName?: string;
  status?: UserStatus;
  city?: string;      // added
  religion?: string;  // added

  type?: 'matrimonial' | 'membership'; // <- Add this
}


// AdminUser adds type info for admin views
export type AdminUser = User & { type: UserType };

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8081/api';

  private loggedInUserSubject = new BehaviorSubject<User | null>(
    JSON.parse(localStorage.getItem('loggedInUser') || 'null')
  );
  loggedInUser$ = this.loggedInUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  // ================== OTP ==================
  sendOtp(phone: string, type: UserType): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${type}/send-otp`,
      { phone },
      { responseType: 'text' } // important to avoid JSON parse errors
    );
  }

  verifyOtp(phone: string, otp: string, type: UserType) {
  const url = type === 'membership'
    ? 'http://localhost:8081/api/membership/verify-otp'
    : 'http://localhost:8081/api/matrimonial/verify-otp';
  return this.http.post<User>(url, { phone, otp }); // <-- typed as User
}

  // ================== Registration ==================
  register(user: User, photo: File | null, type: UserType): Observable<User> {
    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(user)], { type: 'application/json' }));
    if (photo) formData.append('photo', photo);
    return this.http.post<User>(`${this.baseUrl}/${type}/register`, formData);
  }

  // ================== Payment ==================
  markPaymentDone(id: number, type: UserType): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${type}/payment/${id}`, {});
  }

  // ================== Logged-in user state ==================
  setLoggedInUser(user: User) {
    this.loggedInUserSubject.next(user);
    localStorage.setItem('loggedInUser', JSON.stringify(user));
  }

  getLoggedInUser(): User | null {
    return this.loggedInUserSubject.value;
  }

  logout() {
    this.loggedInUserSubject.next(null);
    localStorage.removeItem('loggedInUser');
  }

  // ================== Fetch users ==================
  getAllUsers(type: UserType): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/${type}/all`);
  }

  getPendingUsers(type: UserType): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/${type}/pending`);
  }

  approveUser(id: number, type: UserType): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${type}/approve/${id}`, {});
  }

  rejectUser(id: number, type: UserType): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${type}/reject/${id}`, {});
  }

  // ================== NEW: Get user by phone ==================
getUserByPhone(
  phone: string,
  type: UserType
): Observable<User> {
  if (type === 'membership') {
    return this.http.get<User>(`${this.baseUrl}/membership/status/${phone}`);
  } else {
    return this.http.get<User>(`${this.baseUrl}/matrimonial/status/${phone}`);
  }
}
getUserById(id: number, type: 'matrimonial' | 'membership') {
  return this.http.get<User>(`${this.baseUrl}/${type}/${id}`);
}
}
