// src/app/core/premium-plans.ts
export interface PremiumPlan {
  label: string;
  value: string;
  amount: number;
  description: string;
}

export const membershipPlans: PremiumPlan[] = [
  { label: '1 Month', value: '1_MONTH', amount: 500, description: 'Access all premium features for 1 month.' },
  { label: '1 Year', value: '1_YEAR', amount: 5000, description: 'Enjoy premium features for a full year at a discounted price.' },
  { label: 'Lifetime', value: 'LIFETIME', amount: 10000, description: 'Unlimited access to all premium features forever.' }
];
