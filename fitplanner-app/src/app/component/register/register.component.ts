import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegisterRequest } from 'src/app/interface/register-request';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  
  public registerForm!: FormGroup;

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validator: this.confirmationValidator })
  }

  private confirmationValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;

    if(password !== confirmPassword)
      formGroup.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    else
      formGroup.get('confirmPassword')?.setErrors(null);
  }

  private extractRegisterRequest(formGroup: FormGroup): RegisterRequest {
    return {
      firstName: this.registerForm.get('firstName')?.value,
      lastName: this.registerForm.get('lastName')?.value,
      email: this.registerForm.get('email')?.value,
      password: this.registerForm.get('password')?.value,
    };
  }

  public register() {
    console.log(this.registerForm?.value);
    this.authenticationService.register(this.extractRegisterRequest(this.registerForm)).subscribe((response) => {
      console.log(response);
    })
  }
}
