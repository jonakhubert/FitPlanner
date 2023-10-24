import { Component } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { User } from '../../interface/user';
import { ToastrService } from 'ngx-toastr';
import { UserDetailsRequest } from '../../interface/user-details-request';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss']
})
export class AccountDetailsComponent {
  
  accountForm!: FormGroup;
  user: User | undefined;
  submitted = false;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.getUser();

    this.accountForm = this.formBuilder.group({
      firstName: [{value: '', disabled: true}, [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      lastName: [{value: '', disabled: true}, [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
      email: [{value: '', disabled: true}, [Validators.required, Validators.pattern("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")]],
      height: ['', [Validators.required, Validators.min(0)]],
      weight: ['', [Validators.required, Validators.min(0)]],
      goal: ['', Validators.required],
      activity_level: ['', Validators.required]
    }, { validators: this.bmiValidator });
  }

  onSubmit() {
    this.submitted = true;

    if(this.accountForm.invalid)
      return;

    const request: UserDetailsRequest = {
      firstName: this.accountForm.get('firstName')!.value,
      lastName: this.accountForm.get('lastName')!.value,
      height: this.accountForm.get('height')!.value,
      weight: this.accountForm.get('weight')!.value,
      goal: this.accountForm.get('goal')!.value,
      activity_level: this.accountForm.get('activity_level')!.value
    }

    this.userService.updateUserDetails(this.accountForm.get('email')!.value, request).subscribe({
      next: (response) => {
        this.toastr.success("User updated successfully.", "Success");
        this.ngOnInit();
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  private getUser() {
    const email = localStorage.getItem('userEmail');

    if(email) {
      this.userService.getUser(email).subscribe(
      {
        next: (response) => {
          this.user = response;
          this.accountForm.patchValue({
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            email: this.user.email,
            height: this.user.nutritionInfo.height,
            weight: this.user.nutritionInfo.weight,
            goal: this.user.nutritionInfo.goal,
            activity_level: this.user.nutritionInfo.activity_level
          })
        },
        error: (error) => {
          this.toastr.error("The User Service cannot be reached. Try again later.", "Error")
          console.log(error);
        }
      });  
    }
  }
  
  private bmiValidator: Validators = (control: AbstractControl): ValidationErrors | null => {
    const weight = control.get('weight')?.value;
    const height = control.get('height')?.value;
  
    if(weight && height) {
      const bmi = this.calculateBMI(weight, height);
      console.log(bmi);
      if(bmi < 10 || bmi > 40)
        return { invalidBMI: true };  
    }
  
    return null;
  }

  private calculateBMI(weight: number, height: number): number {
    const heightInMeters = height / 100;
    return weight / (heightInMeters * heightInMeters);
  }
}
