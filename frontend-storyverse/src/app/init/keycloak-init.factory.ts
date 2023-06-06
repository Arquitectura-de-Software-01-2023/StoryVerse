import { KeycloakService } from "keycloak-angular";
import { environment } from "src/environments/environment.docker";

export function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: environment.KEYCLOAK_URL,
        realm: 'software',
        clientId: 'frontend',
        // redirectUri: 'http://localhost:4200/home',
      },
      initOptions: {
        // Si inicia sesión entra, caso contrario no
        onLoad: 'check-sso',
        checkLoginIframe: false
        }
      
    });
}