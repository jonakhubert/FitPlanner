import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition/nutrition.service';
import { Product } from '../../interface/product';

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
  servingSize: number = 0;
  selectedMeal: string = '';

  constructor(
    private nutritionService: NutritionService
  ) {}

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
  }
}
