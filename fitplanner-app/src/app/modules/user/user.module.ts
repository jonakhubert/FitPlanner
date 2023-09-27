import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { UserDashboardComponent } from './component/user-dashboard/user-dashboard.component';
import { UserNavComponent } from './component/user-nav/user-nav.component';
import { WorkoutComponent } from './component/workout/workout.component';
import { DietComponent } from './component/diet/diet.component';
import { AboutComponent } from './component/about/about.component';
import { HttpClientModule } from '@angular/common/http';
import { AccountSettingsComponent } from './component/account-settings/account-settings.component';
import { PasswordChangeComponent } from './component/password-change/password-change.component';
import { AccountDeleteComponent } from './component/account-delete/account-delete.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    UserDashboardComponent,
    UserNavComponent,
    WorkoutComponent,
    DietComponent,
    AboutComponent,
    AccountSettingsComponent,
    PasswordChangeComponent,
    AccountDeleteComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    HttpClientModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class UserModule { }
