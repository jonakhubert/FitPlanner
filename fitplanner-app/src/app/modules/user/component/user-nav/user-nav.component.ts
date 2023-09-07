import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-nav',
  templateUrl: './user-nav.component.html',
  styleUrls: ['./user-nav.component.scss']
})
export class UserNavComponent {
  
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  public logout() {
    this.authenticationService.logout().subscribe(
    {
      next: () => {
        this.router.navigate(['login']);
      }
    });
  }
}
