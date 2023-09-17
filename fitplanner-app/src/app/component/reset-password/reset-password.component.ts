import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ResetPasswordRequest } from 'src/app/interface/reset-password-request';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
  
  resetPasswordForm!: FormGroup;
  email!: string;
  resetPasswordToken!: string;
  submitted = false;

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
    },
    {
      validator: this.confirmPasswordValidator('newPassword', 'confirmPassword')
    })

    this.activatedRoute.queryParams.subscribe(values => {
      this.email = values['email'];
      this.resetPasswordToken = values['token'];
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.resetPasswordForm.invalid)
      return;

    const resetPasswordRequest: ResetPasswordRequest = {
      email: this.email,
      resetPasswordToken: this.resetPasswordToken,
      newPassword: this.resetPasswordForm.controls['newPassword'].value
    };

    this.authenticationService.resetPassword(resetPasswordRequest).subscribe(
    {
      next: (response) => {
        this.router.navigate(['login']);
        this.toastr.success(response.confirmation_message, "Success");
      }, 
      error: () => {
        this.toastr.error("Something went wrong. Try again later.", "Error");
      }
    })
  }

  private confirmPasswordValidator(controlName: string, matchingControlName: string) {
    return(formGroup: FormGroup) => {
      const newPasswordControl = formGroup.controls[controlName];
      const confirmPasswordControl = formGroup.controls[matchingControlName];

      if(confirmPasswordControl.errors && !confirmPasswordControl.errors['confirmPasswordValidator'])
        return;

      if(newPasswordControl.value !== confirmPasswordControl.value)
        confirmPasswordControl.setErrors({ confirmPasswordValidator: true});
      else
        confirmPasswordControl.setErrors(null);
    }
  }
}
