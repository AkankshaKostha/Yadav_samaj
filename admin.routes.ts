import { Routes } from '@angular/router';
import { AdminComponent } from './admin';
import { DashboardComponent } from './dashboard/dashboard';
import { PaymentsComponent } from './payments/payments';
import { AdminHappyStoryComponent } from './admin-happy-stories/admin-happy-stories';
import { Matrimonial_MembersComponent } from './members/matrimonial-members/matrimonial-members';
import { Membership_MembersComponent } from './members/membership-members/membership-members';
import { HomeSectionsComponent } from './home-sections/home-sections';
import { SupportComponent } from './support/support';
import { WalletComponent } from './wallet/wallet';
import { MatrimonialSectionsComponent } from './matrimonial-sections/matrimonial-sections';
import { MembershipSectionsComponent } from './membership-sections/membership-sections';
import { AdminFaqsComponent } from './faq/faq';
import { AdminCommunityComponent } from './community/community';
import { LeadershipAdminComponent } from './leadership-admin-component/leadership-admin-component';
import { TeamAdminComponent } from './admin-happy-stories/admin-team/admin-team';
import { AdminSearchComponent } from './admin-search/admin-search';
import { AdminDonationComponent } from './admin-donation/admin-donation';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },

      {
        path: 'members',
        children: [
          { path: 'matrimonial', component: Matrimonial_MembersComponent },
          { path: 'membership', component: Membership_MembersComponent },
        ],
      },
 { path: 'faq', component: AdminFaqsComponent },
 { path: 'community', component: AdminCommunityComponent },
      { path: 'payments', component: PaymentsComponent },
      { path: 'happy-stories', component: AdminHappyStoryComponent },
      { path: 'banners', component: HomeSectionsComponent },  // ✅ fixed name
      { path: 'support', component: SupportComponent },
      { path: 'wallet', component: WalletComponent },
{ path: 'matrimonial-sections', component: MatrimonialSectionsComponent },
{ path: 'membership-sections', component: MembershipSectionsComponent },
{ path: 'leadership', component: LeadershipAdminComponent },
{ path: 'admin-team', component: TeamAdminComponent },
{ path: 'admin-search', component: AdminSearchComponent },
{ path: 'admin-donation', component: AdminDonationComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },


    ],
  },
];
