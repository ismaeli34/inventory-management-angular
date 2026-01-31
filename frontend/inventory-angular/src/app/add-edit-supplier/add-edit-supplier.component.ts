import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {ApiService} from '../service/api.service';
import {response} from 'express';

@Component({
  selector: 'app-add-edit-supplier',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './add-edit-supplier.component.html',
  styleUrl: './add-edit-supplier.component.css'
})
export class AddEditSupplierComponent implements OnInit{

  message:string ='';
  isEditing:boolean = false;
  supplierId:string | null = null;

  formData: any ={
    name: '',
    address: '',

  }
  constructor(private apiService:ApiService,
              private router:Router) {
  }

  ngOnInit(): void {
    this.supplierId = this.router.url.split('/')[2]; // extracting supplied id from url
    if (this.supplierId){
      this.isEditing =true;
      this.fetchSupplier();
    }
    }

    fetchSupplier():void{
    this.apiService.getSupplierById(this.supplierId!).subscribe({
      next:(response:any)=>{
        if (response.status ===200){
          this.formData ={
            name: response.supplier.name,
            address: response.supplier.address
          };
        }
      },
      error:(error) =>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get supplier by Id "+ error);
      }
    })
    }

    // HANDLE FORM SUBMISSION

  handleSubmit(){
    if (!this.formData.name || !this.formData.address){
      this.showMessage("All fields are necessary");
      return ;
    }
    // prepare data from submission
    const supplierData ={
      name: this.formData.name,
      address: this.formData.address
    }


    if (this.isEditing){
      this.apiService.updateSupplier(this.supplierId!, supplierData).subscribe({
        next:(res:any)=>{
          if (res.status ===200){
            this.showMessage("Supplier updated successfully")
            this.router.navigate(['/supplier'])
          }
        },
        error:(error)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to edit category" + error)
        }
      })
    }else{
      this.apiService.addSupplier(supplierData).subscribe({
        next:(res:any)=>{
          if (res.status ===200){
            this.showMessage("Supplier added successfully")
            this.router.navigate(['/supplier'])
          }
        },
        error:(error)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to add category" + error)
        }
      })
    }


  }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = '';
    },4000)
  }


}
