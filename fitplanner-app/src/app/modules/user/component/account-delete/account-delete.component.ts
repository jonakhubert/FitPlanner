import { Component } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-account-delete',
  templateUrl: './account-delete.component.html',
  styleUrls: ['./account-delete.component.scss']
})
export class AccountDeleteComponent {

  constructor(
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  delete() {
    const email = localStorage.getItem('userEmail');
    const closeButton = document.getElementById("closeBtn");

    this.userService.deleteAccount(email!).subscribe(
    {
      next: () => {
        closeButton?.click();
        this.router.navigate(['login']);
        this.toastr.info("Your account has been permanently deleted.", "Info");
      },
      error: () => {
        this.toastr.error("Something went wrong. Try again later.", "Error");
      }
    });
    }
}
