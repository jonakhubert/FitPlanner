import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ChangePasswordRequest } from 'src/app/modules/user/interface/change-password-request';

@Component({
  selector: 'app-password-change',
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.scss']
})
export class PasswordChangeComponent {

  passwordChangeForm!: FormGroup;
  submitted = false;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.passwordChangeForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
    },
    {
      validator: this.confirmPasswordValidator('newPassword', 'confirmPassword')
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.passwordChangeForm.invalid)
      return;

    const resetPasswordRequest: ChangePasswordRequest = {
      email: localStorage.getItem("userEmail")!,
      password: this.passwordChangeForm.controls['newPassword'].value
    };

    this.userService.changePassword(resetPasswordRequest).subscribe(
    {
      next: (response) => {
        this.router.navigate(['login']);
        this.toastr.success(response.confirmation_message, "Info");
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
