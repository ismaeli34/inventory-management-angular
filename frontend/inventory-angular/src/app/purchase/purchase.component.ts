import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../service/api.service';

@Component({
  selector: 'app-purchase',
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase.component.html',
  styleUrl: './purchase.component.css'
})
export class PurchaseComponent implements OnInit{

  products : any[]=[];
  suppliers: any[]=[];
  productId:string = ""
  supplierId:string =""
  description:string =""
  quantity:string=""
  message:string=""
    constructor(private apiService:ApiService) {
    }

    ngOnInit(): void {
    this.fetchProductAndSupplier();
    }

  fetchProductAndSupplier():void{
    this.apiService.getAllProducts().subscribe({
      next:(res:any)=>{
        if (res.status ===200){
          this.products = res.products
        }
      },
      error:(error)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get products" + error)
      }

    });

    this.apiService.getAllSuppliers().subscribe({
      next:(res:any)=>{
        if (res.status ===200){
          this.suppliers = res.suppliers;
        }
      },

      error:(error)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get suppliers" + error)
      }

    })

  }

  // Handle form submission

  handleSubmit(){
    if (!this.productId || !this.supplierId || !this.quantity ){
      this.showMessage("Please fill all fields")
      return;
    }
    const body ={
     productId : this.productId,
      supplierId: this.supplierId,
      quantity: parseInt(this.quantity,10),
      description: this.description
    }

    this.apiService.purchaseProduct(body).subscribe({
      next:(res:any)=>{
        if (res.status ===200){
          this.showMessage(res.message)
          this.resetForm();
        }
      },

      error:(error)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to  purchase product" + error)
      }

    })
  }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = '';
    },4000)
  }

  resetForm():void{
    this.productId ="";
    this.supplierId ="";
    this.description ="";
    this.quantity ="";
  }


}

