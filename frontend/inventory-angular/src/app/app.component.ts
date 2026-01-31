import {ChangeDetectorRef, Component} from '@angular/core';
import {Router, RouterLink, RouterModule, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {ApiService} from './service/api.service';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone:true,
  imports: [RouterOutlet, RouterLink, CommonModule,RouterModule, MatIconModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'inventory-angular';

  constructor(private apiService:ApiService,
              private router:Router,
              private cdr: ChangeDetectorRef) {
  }

  isSidebarOpen = false;

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  isAuth():boolean{
    return this.apiService.isAuthenticated();
  }

  isAdmin():boolean{
  return  this.apiService.isAdmin();
  }

  logout(){
    this.apiService.logout();
    this.router.navigate(['/login'])
    this.cdr.detectChanges();
  }



}
