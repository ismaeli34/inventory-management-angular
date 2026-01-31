import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule, NgIf} from '@angular/common';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {ApiService} from '../service/api.service';
import {firstValueFrom} from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    RouterLink,
    CommonModule,
    RouterLinkActive
  ],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  formData:any;
  message:string | null =null;

  ngOnInit(): void {
  }
  constructor(private apiService:ApiService,
              private router:Router) {

    this.formData ={
      email:'',
      password:''
    };

  }

 async handleSubmit(){
    if (!this.formData.email || !this.formData.password){
      this.showMessage("All fields are required");
      return;
    }
   try{
     const response:any = await firstValueFrom(
       this.apiService.loginUser(this.formData)
     );
     if (response.status === 200){
       this.apiService.encryptAndSaveToStorage('token',response.token)
       this.apiService.encryptAndSaveToStorage('role',response.role)
       this.showMessage(response.message)
       console.log("Successfully Logged In");
       this.router.navigate(['/dashboard'])
     }
   }catch (error:any){
     console.log(error)
     this.showMessage(error?.error?.message || error?.message || "Unable to login a user" + error)
   }

  }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = null;
    },4000)
  }


}
