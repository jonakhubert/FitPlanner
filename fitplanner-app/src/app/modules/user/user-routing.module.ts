import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDashboardComponent } from './component/user-dashboard/user-dashboard.component';
import { DietComponent } from './component/diet/diet.component';
import { WorkoutComponent } from './component/workout/workout.component';
import { AboutComponent } from './component/about/about.component';
import { AccountSettingsComponent } from './component/account-settings/account-settings.component';
import { PasswordChangeComponent } from './component/password-change/password-change.component';
import { AccountDeleteComponent } from './component/account-delete/account-delete.component';

const routes: Routes = [
  {
    path: '',
    component: UserDashboardComponent,
    children: [
      { path: 'about', component: AboutComponent },
      { path: 'diet', component: DietComponent },
      { path: 'workout', component: WorkoutComponent },
      { path: 'account-settings', component: AccountSettingsComponent },
      { path: 'change-password', component: PasswordChangeComponent },
      { path: 'delete-account', component: AccountDeleteComponent },
      { path: '', redirectTo: '/user/about', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
