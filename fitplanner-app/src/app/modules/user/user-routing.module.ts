import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDashboardComponent } from './component/user-dashboard/user-dashboard.component';
import { DietComponent } from './component/diet/diet.component';
import { WorkoutComponent } from './component/workout/workout.component';
import { AboutComponent } from './component/about/about.component';
import { AccountSettingsComponent } from './component/account-settings/account-settings.component';
import { PasswordChangeComponent } from './component/password-change/password-change.component';
import { AccountDeleteComponent } from './component/account-delete/account-delete.component';
import { userGuard } from './guards/user/user.guard';
import { AccountDetailsComponent } from './component/account-details/account-details.component';
import { ProductSearchComponent } from './component/product-search/product-search.component';
import { searchGuard } from './guards/search/search.guard';
import { StrengthExerciseSearchComponent } from './component/strength-exercise-search/strength-exercise-search.component';

const routes: Routes = [
  {
    path: '',
    component: UserDashboardComponent,
    children: [
      { path: 'about', component: AboutComponent, canActivate: [userGuard] },
      { path: 'diet', component: DietComponent, canActivate: [userGuard] },
      { path: 'diet/search', component: ProductSearchComponent, canActivate: [userGuard, searchGuard] },
      { path: 'workout', component: WorkoutComponent, canActivate: [userGuard] },
      { path: 'workout/strength-exercise/search', component: StrengthExerciseSearchComponent, canActivate: [userGuard, searchGuard] },
      { path: 'account-settings', component: AccountSettingsComponent, canActivate: [userGuard] },
      { path: 'account-settings/change-password', component: PasswordChangeComponent, canActivate: [userGuard] },
      { path: 'account-settings/delete-account', component: AccountDeleteComponent, canActivate: [userGuard] },
      { path: 'account-settings/account-details', component: AccountDetailsComponent, canActivate: [userGuard] },
      { path: '', redirectTo: '/user/about', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
