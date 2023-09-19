import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { authGuard } from './guards/auth/auth.guard';
import { ResetPasswordComponent } from './component/reset-password/reset-password.component';
import { resetPasswordGuard } from './guards/resetpassword/reset-password.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'reset-password',
    canActivate: [resetPasswordGuard],
    component: ResetPasswordComponent
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'user',
    canActivate: [authGuard],
    loadChildren: () =>
      import('./modules/user/user.module').then(m => m.UserModule)
  },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
