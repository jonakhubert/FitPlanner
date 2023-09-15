import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-nav',
  templateUrl: './user-nav.component.html',
  styleUrls: ['./user-nav.component.scss']
})
export class UserNavComponent {
  
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  public logout() {
    this.authenticationService.logout().subscribe(
    {
      next: () => {
        this.router.navigate(['login']);
        this.toastr.success("You have been successfully logged out.", "Logout Successful");
      },
      error: () => {
        this.toastr.error("Something went wrong. Try again later.", "Error");
      }
    });
  }
}
