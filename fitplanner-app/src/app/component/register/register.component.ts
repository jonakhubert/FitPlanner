import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
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
  verificationMessage = '';

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
    
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      height: [null, [Validators.required, Validators.min(0)]],
      weight: [null, [Validators.required, Validators.min(0)]],
      goal: [null, Validators.required],
      email: ['', [Validators.required, Validators.pattern("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    }, { validators: this.bmiValidator })
  }

  onSubmit() {
    this.submitted = true;

    if(this.registerForm.invalid)
      return;

    this.authenticationService.register(this.registerForm.value).subscribe(
    {
      next: (response) => {
        console.log(response);
        this.router.navigate(['login']);
        this.toastr.success("User has been registered.", "Success");
        this.toastr.info(response.confirmation_message, "Info");
      },
      error: (error) => {
        if(error.status === 409) {
          this.verificationMessage = '';
          this.alertMessage = this.registerForm.get('email')?.value + " already exists in database.";
        }
        else {
          console.log(error);
          this.verificationMessage = '';
          this.toastr.error("Something went wrong. Try again later.", "Error");
        }
      }
    });
  }

  bmiValidator: Validators = (control: AbstractControl): ValidationErrors | null => {
    const weight = control.get('weight')?.value;
    const height = control.get('height')?.value;
  
    if(weight && height) {
      const bmi = this.calculateBMI(weight, height);
      console.log(bmi);
      if(bmi < 10 || bmi > 40)
        return { invalidBMI: true };  
    }
  
    return null;
  };

  calculateBMI(weight: number, height: number): number {
    const heightInMeters = height / 100;
    return weight / (heightInMeters * heightInMeters);
  }
}
