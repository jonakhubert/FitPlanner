import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss']
})
export class UserDashboardComponent {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        if (this.authenticationService.isLoggedIn()) {
          this.authorize();
        }
      }
    });

    if(this.authenticationService.isLoggedIn())
      this.authorize();
  }

  private authorize() {
    this.authenticationService.authorize().subscribe({
      next: () => {},
      error: () => {
        this.toastr.info("Your session has expired.", "Info");
      }
    });
  }
}
