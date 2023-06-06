import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent {
  logged: boolean;
  constructor(private keycloakService: KeycloakService) {
    this.keycloakService.isLoggedIn().then(
      (result) => {
        this.logged = result;
      }
    );
  }
  
  categories = [
    {
      id: '1',
      name: 'Aventura',
      image: '../../../assets/categories/aventura.png',
    },
    {
      id: '2',
      name: 'Acción',
      image: '../../../assets/categories/accion.png',
    },
    {
      id: '3',
      name: 'Ciencia ficción',
      image: '../../../assets/categories/scifi.png',
    },
    {
      id: '4',
      name: 'Espiritual',
      image: '../../../assets/categories/espiritual.png',
    },
    {
      id: '5',
      name: 'Fantasía',
      image: '../../../assets/categories/fantasia.png',
    },
    {
      id: '6',
      name: 'Humor',
      image: '../../../assets/categories/humor.png',
    },
    {
      id: '7',
      name: 'Misterio',
      image: '../../../assets/categories/misterio.png',
    },
    {
      id: '8',
      name: 'Novela histórica',
      image: '../../../assets/categories/historia.png',
    },
    {
      id: '9',
      name: 'Novela juvenil',
      image: '../../../assets/categories/juvenil.png',
    },
    {
      id: '10',
      name: 'Paranormal',
      image: '../../../assets/categories/paranormal.png',
    },
    {
      id: '11',
      name: 'Romance',
      image: '../../../assets/categories/romance.png',
    },
    {
      id: '12',
      name: 'Suspenso',
      image: '../../../assets/categories/suspenso.png',
    },
    {
      id: '13',
      name: 'Terror',
      image: '../../../assets/categories/terror.png',
    },
    {
      id: '14',
      name: 'Vampiros',
      image: '../../../assets/categories/vampiros.png',
    },
    {
      id: '15',
      name: 'Otros',
      image: '../../../assets/categories/otro.png',
    }

  ]


}
