import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { UserDashboardComponent } from './component/user-dashboard/user-dashboard.component';
import { UserNavComponent } from './component/user-nav/user-nav.component';
import { WorkoutComponent } from './component/workout/workout.component';
import { DietComponent } from './component/diet/diet.component';
import { AboutComponent } from './component/about/about.component';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [
    UserDashboardComponent,
    UserNavComponent,
    WorkoutComponent,
    DietComponent,
    AboutComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    HttpClientModule,
    HttpClientModule
  ]
})
export class UserModule { }
