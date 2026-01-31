import {Component, OnInit} from '@angular/core';
import {ApiService} from '../service/api.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  user:any ="";
  message:string ="";

  constructor(private apiService:ApiService) {

    }

    ngOnInit(): void {
      this.fetchUserInfo();
    }

  fetchUserInfo(){
      this.apiService.getLoggedInUserInfo().subscribe({
        next:(res)=>{
          this.user = res;
        },
        error:(error)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to  show profile" + error)
        }
      })
  }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = '';
    },4000)
  }



}
