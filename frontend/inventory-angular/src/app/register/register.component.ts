import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router, RouterLink, RouterModule} from '@angular/router';
import {ApiService} from '../service/api.service';
import {firstValueFrom} from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,   // <-- add this
  imports: [FormsModule, CommonModule, RouterModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{
  formData:any;
  message:string | null =null;

  constructor(private apiService:ApiService,
              private router:Router) {
    this.formData ={
      email:'',
      name:'',
      phoneNumber:'',
      password:''
    };
  }

  async handleSubmit(){
    if (!this.formData.email || !this.formData.name || !this.formData.phoneNumber || !this.formData.password ){
      this.showMessage("All fields are required");
      return;
    }
    try{
  const response:any = await firstValueFrom(
    this.apiService.registerUser(this.formData)
  );
  if (response.status === 200){
    this.showMessage(response.message)
    console.log("Successfully registered");
    this.router.navigate(['/login'])
  }
    }catch (error:any){
      console.log(error)
      this.showMessage(error?.error?.message || error?.message || "Unable to register a user" + error)
    }
  }

  showMessage(message:string){
    this.message = message;
    setTimeout(()=>{
      this.message = null;
    },4000)
  }


  ngOnInit(): void {
  }

}
