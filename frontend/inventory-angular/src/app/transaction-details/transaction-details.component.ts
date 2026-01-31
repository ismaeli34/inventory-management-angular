import {Component, OnInit} from '@angular/core';
import {ApiService} from '../service/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-transaction-details',
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './transaction-details.component.html',
  styleUrl: './transaction-details.component.css'
})
export class TransactionDetailsComponent implements OnInit{

  constructor(private apiService:ApiService,
              private route: ActivatedRoute,
              private router:Router
              ) {
  }
  transactionId:string | null ="";
  transaction:any = null;
  status:string ="";
  message:string ="";

    ngOnInit(): void {
      // extract transaction id from routes
      this.route.params.subscribe(params => {
        this.transactionId = params['transactionId'];
        this.getTransactionDetails();
      })
    }

  getTransactionDetails(){
      if (this.transactionId){
        this.apiService.getTransactionById(this.transactionId).subscribe({
          next:(transactionData:any) =>{
            if (transactionData.status ===200){
              this.transaction = transactionData.transaction
              this.status = this.transaction.status
            }
        },       error: (error) => {
            this.showMessage(
              error?.error?.message ||
              error?.message ||
              'Unable to get  transaction id' + error
            );
          },
        })
      }
  }

  // SHOW ERROR
  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
    }, 4000);
  }

  //UPDATE STATUS
  handleUpdateStatus():void{
    if (this.transactionId && this.status) {
      this.apiService.updateTransactionStatus(this.transactionId, this.status).subscribe({
        next:(result)=>{
          this.router.navigate(['/transaction'])
        },
        error:(error)=>{
          this.showMessage(
            error?.error?.message ||
            error?.message ||
            'Unable to Update a Transaction ' + error
          );
        }
      })
    }
  }


}
