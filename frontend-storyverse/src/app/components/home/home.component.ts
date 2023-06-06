import { Component } from '@angular/core';
import { UserService } from '../../service/user.service';
import { UserDto } from 'src/app/dto/user.dto';
import { StoryService } from 'src/app/service/story.service';
import { StoryDto } from 'src/app/dto/story.dto';
import { AwsSesService } from 'src/app/service/aws-ses.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor( private userService: UserService, private storyService: StoryService, private awsSesService: AwsSesService) {
    
  }
  stories: StoryDto[] = [];
  user: UserDto;

  //Asignar historias a las variables
  storiesPopular: StoryDto[] = []; //5
  storyMostPopular : StoryDto; //1
  tagStoryMostPopular = [];
  storiesToRemember : StoryDto[] = []; //5
  storiesNew : StoryDto[] = []; //5
  categories = ['Aventura','Acción','Ciencia ficción','Espiritual','Fantasía','Humor','Misterio','Novela histórica','Novela juvenil','Paranormal','Romance','Suspenso','Terror','Vampiros','Otros'];

  //Para verificación de usuario
  fechaCreacion: Date;
  fechaActual: Date = new Date();

  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        this.user= data.data;
        this.awsSesService.verifyUser(this.user.preferred_username).subscribe({
          next: data => {
            this.fechaCreacion = new Date(data[0].createdTimestamp);
            console.log("Fecha de creacion del usuario: "+this.fechaCreacion);
            var diferenciaTiempo = this.fechaActual.getTime() - this.fechaCreacion.getTime();
            //Hace mas de un minuto
            if(diferenciaTiempo <= 60000){
              console.log("Hace menos de un minuto");
              this.awsSesService.sendVerificationMessage(this.user.email).subscribe({
                next: data => {
                  console.log("Mensaje de verificación enviado");
                }
              });
            } else {
              console.log("Hace más de un minuto");
            }
          }
        });
      },
      error: (error) => console.log(error),
      },    
    );

    // Obtenemos todas las historias
    this.storyService.getStoryList()
    .subscribe({
      next:data => {
        this.stories = data.data;
        console.log(this.stories);
        //Asignamos historias aleatorias a las variables
        this.storiesPopular = this.stories.slice(0,5);
        this.storiesToRemember = this.stories.slice(5,10);
        this.storiesNew = this.stories.slice(10,15);
        this.storyMostPopular = this.stories[15];

        this.obtenerTags(this.storyMostPopular.storyId);
      }
    })
  }
  
  getCategoryName(id: number){
    return this.categories[id-1];
  }

  obtenerTags(id: number){
    // Tags de la historia
    this.storyService.getTags(id).subscribe({
      next: data => {
        console.log("Tags de la historia");
        console.log(data.data);
        
        this.tagStoryMostPopular = data.data.map((tag: { name: any }) => tag.name);
        console.log(this.tagStoryMostPopular);
      }
    });
  }

  colors = ['#b2d9e8', '#f9d7d6', '#c6ded4', '#c7bad8']
}