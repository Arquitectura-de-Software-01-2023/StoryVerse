import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from "keycloak-angular";
import { KeycloakLoginOptions } from 'keycloak-js';

@Component({
  selector: 'app-navbarinvited',
  templateUrl: './navbarinvited.component.html',
  styleUrls: ['./navbarinvited.component.css']
})

export class NavbarinvitedComponent {
  
  constructor(private keycloakService: KeycloakService, private router: Router) { }
  
  keycloakLoginOptions: KeycloakLoginOptions = {
    redirectUri: 'http://localhost:4200/home'
  }

  inputSearchTitle: string = "";
    
  login(): void {
      this.keycloakService.login(this.keycloakLoginOptions);
  }
}
