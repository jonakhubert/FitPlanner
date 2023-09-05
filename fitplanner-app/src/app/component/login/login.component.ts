import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
  public loginForm!: FormGroup;
  submitted = false;
  message = '';

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")]],
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
      },
      error: (error) => {
        if(error.statusCode === 404) {
          this.message = this.loginForm.get('email')?.value + " doesn't exist.";
        } else if(error.statusCode === 400) {
          this.message = "Invalid password.";
        }
      }
    });
  }
}
