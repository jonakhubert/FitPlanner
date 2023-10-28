import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition/nutrition.service';
import { Product } from '../../interface/product';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FoodItemRequest } from '../../interface/food-item-request';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.scss']
})
export class ProductSearchComponent {

  products: Product[] = [];
  searchQuery = '';
  showNoResultsMessage: boolean = false;
  selectedProduct: Product | null = null;
  modalQuantity: number = 0;
  selectedMeal: string = '';
  date: string = '';
  foodItemForm!: FormGroup;
  submitted: boolean = false;

  constructor(
    private nutritionService: NutritionService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.selectedMeal = params['meal'];
      this.date = params['date'];
    })
  }

  searchProducts(name: string) {
    this.nutritionService.getProducts(name).subscribe(
    {
      next: (response) => {
        console.log(response);
        if (response && response.length > 0) {
          this.products = response;
          this.showNoResultsMessage = false;
        } else {
          this.products = [];
          this.showNoResultsMessage = true;
        }

        this.selectedProduct = null;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  selectProduct(product: Product) {
    this.selectedProduct = product;

    this.foodItemForm = this.formBuilder.group({
      quantity: [0, [Validators.required, Validators.min(1)]],
      meal: [this.selectedMeal, Validators.required]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.foodItemForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedProduct && this.foodItemForm) {
      const request: FoodItemRequest = {
        name: this.selectedProduct.name,
        calories: this.calculateCalories(this.foodItemForm.get('quantity')!.value),
        protein: this.calculateProtein(this.foodItemForm.get('quantity')!.value),
        fat: this.calculateFat(this.foodItemForm.get('quantity')!.value),
        carbs: this.calculateCarbs(this.foodItemForm.get('quantity')!.value),
        quantity: this.foodItemForm.get('quantity')!.value
      }
      
      this.nutritionService.addFoodItem(email, this.date, this.foodItemForm.get('meal')!.value, request).subscribe(
      {
        next: (response) => {
          this.router.navigate(['/user/diet']);
          this.toastr.success(response.confirmation_message, "Success");
        },
        error: (error) => {
          console.log(error)
        }
      });
    }
  }

  onQuantityChange(event: number): void {
    this.modalQuantity = event;
  }

  calculateCalories(quantity: number): number {
    if(this.selectedProduct)
      return parseFloat(((quantity * this.selectedProduct!.calories) / 100).toFixed(2));

      return 0;
  }

  calculateProtein(quantity: number): number {
    if(this.selectedProduct)
      return parseFloat(((quantity * this.selectedProduct!.protein) / 100).toFixed(2));
    
    return 0;
  }

  calculateFat(quantity: number): number {
    if(this.selectedProduct)
      return parseFloat(((quantity * this.selectedProduct!.fat) / 100).toFixed(2));

    return 0;
  }

  calculateCarbs(quantity: number): number {
    if(this.selectedProduct)
      return parseFloat(((quantity * this.selectedProduct!.carbs) / 100).toFixed(2));

    return 0;
  }

  resetModalQuantity(): void {
    this.modalQuantity = 0;
  }
}
