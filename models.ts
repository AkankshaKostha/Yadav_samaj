// src/app/core/models.ts
export interface MatrimonialUser {
  id?: number;
  fullName: string;
  phone: string;
  email: string;
  dob: string;
  gender?: string;
  education?: string;
  address?: string;
  role?: string;
  membershipPlan?: 'Free' | '1_MONTH' | '1_YEAR' | 'LIFETIME';
  city?: string; // <-- make it optional (fixes TS2322)
  approved?: boolean;
  photoFileName?: string;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';

  // ✅ add optional age property (frontend-only)
  age?: number;
}

export interface MembershipUser {
  id?: number;
  fullName: string;
  dob?: string;
  city: string;
  approved?: boolean;
  photoFileName?: string;
  gender?: string;
}

export interface HappyStory {
  id?: number;
  coupleName: string;
  message: string;
  photoUrl?: string;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
}
export interface VerifyOtpResponse {
  verified: boolean;
  status?: 'PENDING' | 'ACTIVE' | string;
  paymentDone?: boolean;
  role?: 'ADMIN' | 'USER' | string;
  message?: string;
  fullName?: string; // ✅ add this
}


export interface HomeSection {
  id: number;
  title: string;
  description?: string;
  route?: string;
  type: string;
  active: boolean;
  imageUrl?: string;        // first image
  imageUrls?: string[];     // all images
}

export interface PaymentResponse {
  amount?: number;           // optional if sometimes not returned
  status?: 'PENDING' | 'PAID';
  message?: string;
  success?: boolean;
  details?: string;
  methodName?: string;
  payerEmail?: string;
  payerName?: string;
  onpayerdetailchange?: (event: any) => void;
  [key: string]: any;       // allow extra fields
}

export interface PremiumPlan {
  id?: number;
  name: string;
  price: number;
}
export interface CommunitySection {
  id?: number;
  title: string;
  description?: string;
  imageUrl?: string;
  route?: string;
  active?: boolean;
}

