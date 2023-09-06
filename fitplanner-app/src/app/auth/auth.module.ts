import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { AuthComponent } from './auth.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    RegisterComponent,
    LoginComponent,
    AuthComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class AuthModule { }
