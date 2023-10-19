import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
  public loginForm!: FormGroup;
  public resetPasswordEmail!: string;
  public isEmailValid!: boolean;
  submitted = false;
  message = '';

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    if(this.authenticationService.isLoggedIn()) {
      this.router.navigate(['user']);
    }

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,3})$")]],
      password: ['', Validators.required]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.loginForm.invalid) {
      return;
    }

    this.authenticationService.login(this.loginForm.value).subscribe(
    {
      next: (response) => {
        console.log(response);
        this.router.navigate(['user']);
        this.toastr.success('Login successfully!');
      },
      error: (error) => {
        if(error.status === 404)
          this.message = this.loginForm.get('email')?.value + " doesn't exist.";
        else if(error.status === 400)
          this.message = "Invalid password.";
        else if(error.status === 403)
          this.message = error.error.message;
        else
          this.toastr.error("Something went wrong. Try again later.", "Error");
      }
    });
  }

  checkEmailValidation(event: string) {
    const value = event;
    const pattern = /^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,3})$/;
    this.isEmailValid = pattern.test(value);
    return this.isEmailValid;
  }

  sendResetPassword() {
    if(this.isEmailValid) {
      this.authenticationService.forgotPassword(this.resetPasswordEmail).subscribe(
        {
          next: (response) => {
            this.resetPasswordEmail = '';
            this.toastr.info("Link to reset your password has been sent.", "Info");
            const closeButton = document.getElementById("closeBtn");
            closeButton?.click();
          },
          error: (error) => {
            if(error.status === 404)
              this.toastr.error("Account with provided email doesn't exist.", "Error");
            else
              this.toastr.error("Something went wrong. Try again later.", "Error");
          }
        }
      )
    }
  }
}
