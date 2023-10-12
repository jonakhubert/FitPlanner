import { Component } from '@angular/core';
import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-nutrition-info',
  templateUrl: './nutrition-info.component.html',
  styleUrls: ['./nutrition-info.component.scss']
})
export class NutritionInfoComponent {
  
  constructor(
    private userService: UserService
  ) {}

  
}
