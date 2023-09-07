import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDashboardComponent } from './component/user-dashboard/user-dashboard.component';
import { DietComponent } from './component/diet/diet.component';
import { WorkoutComponent } from './component/workout/workout.component';
import { AboutComponent } from './component/about/about.component';

const routes: Routes = [
  {
    path: '',
    component: UserDashboardComponent,
    children: [
      { path: 'about', component: AboutComponent },
      { path: 'diet', component: DietComponent },
      { path: 'workout', component: WorkoutComponent },
      { path: '', redirectTo: '/user/about', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
