import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakLoginOptions } from 'keycloak-js';

@Component({
  selector: 'app-invited',
  templateUrl: './invited.component.html',
  styleUrls: ['./invited.component.css']
})
export class InvitedComponent {
    keycloakLoginOptions: KeycloakLoginOptions = {
        redirectUri: 'http://localhost:4200/home'
    }
    constructor(private keycloakService: KeycloakService) { }

    login(): void {
        this.keycloakService.login(this.keycloakLoginOptions);
    }
}
