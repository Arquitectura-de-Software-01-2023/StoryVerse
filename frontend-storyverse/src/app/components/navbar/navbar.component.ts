import { Component } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent {

  constructor(private userService: UserService, private keycloakService: KeycloakService) { }
  
  urlPfp: string;
  inputSearchTitle: string = "";

  ngOnInit(){
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        if (data.data.url_pfp.trim().length <= 0) {
          this.urlPfp = '../../../assets/profile/pfp.svg';
        } else {
          this.urlPfp = data.data.url_pfp;
        }
      }
    });
  };

  logout(){
    console.log("Cerrando sesión");
    this.keycloakService.logout('http://localhost:4200/');
  }

  
}