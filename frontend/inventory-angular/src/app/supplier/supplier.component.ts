import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ApiService} from '../service/api.service';
import {Router, RouterModule} from '@angular/router';

@Component({
  selector: 'app-supplier',
  imports: [CommonModule],
  templateUrl: './supplier.component.html',
  styleUrl: './supplier.component.css'
})
export class SupplierComponent implements OnInit{

  suppliers:any[]=[];
  message:string ='';

  constructor(private apiService:ApiService,
              private router:Router) {
  }

  ngOnInit(): void {
    this.getSuppliers();
    }

    getSuppliers():void{
    this.apiService.getAllSuppliers().subscribe({
      next:(res:any) =>{
        if (res.status ===200){
          this.suppliers = res.suppliers;
        }else{
          this.showMessage(res.message);
        }
      },
      error:(error) =>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get all categories "+ error);
      }
    })
    }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = '';
    },4000)
  }

  // Navigate to supplier page
  navigateToAddSupplierPage():void{
    this.router.navigate(['/add-supplier']);
  }

  // Navigate to edit supplier page
  navigateToEditSupplierPage(suppliedId:string):void{
    this.router.navigate([`/edit-supplier/${suppliedId}`]);
  }

  handleDeleteSupplier(supplierId:string){
    if (window.confirm("Are you sure you want to delete this supplier ?")){
      this.apiService.deleteSupplier(supplierId).subscribe({
        next:(res:any)=>{
          if (res.status ===200){
            this.showMessage("Supplier deleted successfully")
            this.getSuppliers(); // reload supplier
          }
        },
        error:(error)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to delete supplier" + error)
        }
      })
    }
  }

}
