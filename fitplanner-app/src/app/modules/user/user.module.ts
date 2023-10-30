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
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { AccountDetailsComponent } from './component/account-details/account-details.component';
import { NgChartsModule } from 'ng2-charts';
import { ProductSearchComponent } from './component/product-search/product-search.component';
import { DateSelectorComponent } from './component/date-selector/date-selector.component';
import { StrengthExerciseSearchComponent } from './component/strength-exercise-search/strength-exercise-search.component';
import { CardioExerciseSearchComponent } from './component/cardio-exercise-search/cardio-exercise-search.component';


@NgModule({
  declarations: [
    UserDashboardComponent,
    UserNavComponent,
    WorkoutComponent,
    DietComponent,
    AboutComponent,
    AccountSettingsComponent,
    PasswordChangeComponent,
    AccountDeleteComponent,
    AccountDetailsComponent,
    ProductSearchComponent,
    DateSelectorComponent,
    StrengthExerciseSearchComponent,
    CardioExerciseSearchComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    HttpClientModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    MatCardModule,
    NgChartsModule
  ]
})
export class UserModule { }
