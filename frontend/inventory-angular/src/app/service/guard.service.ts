import { Injectable } from '@angular/core';
import {ApiService} from './api.service';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
// Guards in Angular are used to control access to routes and manage navigation
// in a secure and organized way.
// Guards are like traffic lights for your app routes, deciding who can go,
// who must stop, and where they should be redirected.
export class GuardService implements CanActivate {

  constructor(private apiService: ApiService,
              private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    const requireAdmin = route.data['requiresAdmin'] || false;
    // if page needs an admin -> if user is admin they can access
    if(requireAdmin){
    if (this.apiService.isAdmin()){
      return  true;
    }else {
      this.router.navigate(['/login'], {
          queryParams: {returnUrl: state.url}
        }
      );
      return false;
    }
    }

    // if page does not need an admin -> any loggedin user can access
    else{
      if (this.apiService.isAuthenticated()){
        return true;
      }else{
        this.router.navigate(['/login'],{
            queryParams:{returnUrl: state.url }
          }
        );
        return  false; // deny access
      }
    }
    }

}
