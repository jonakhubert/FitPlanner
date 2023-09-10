import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  
  registerForm!: FormGroup;
  submitted = false;
  alertMessage = '';
  confirmationMessage = '';

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    if(this.authenticationService.isLoggedIn()) {
      this.router.navigate(['user']);
    }
    
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      email: ['', [Validators.required, Validators.pattern("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.registerForm.invalid) {
      return;
    }

    this.authenticationService.register(this.registerForm.value).subscribe(
    {
      next: (response) => {
        console.log(response);
        this.alertMessage = '';
        this.confirmationMessage = response.confirmation_message;
      },
      error: (error) => {
        if(error.statusCode === 409) {
          this.confirmationMessage = '';
          this.alertMessage = this.registerForm.get('email')?.value + " already exists in database.";
        }
        else if(error.statusCode === 500) {
          this.confirmationMessage = '';
          this.alertMessage = "Something went wrong. Try again later.";
        }
      }
    });
  }
}
