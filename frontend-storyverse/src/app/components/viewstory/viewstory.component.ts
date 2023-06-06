import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { ActivatedRoute } from '@angular/router';
import { ChapterService } from '../../service/chapter.service';
import { UserService } from '../../service/user.service';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-viewstory',
  templateUrl: './viewstory.component.html',
  styleUrls: ['./viewstory.component.css']
})
export class ViewstoryComponent {

  logged: boolean;
  router: any;
  
  constructor(private keycloakService: KeycloakService,private route: ActivatedRoute, private StoryService: StoryService, private ChapterService: ChapterService, private userService: UserService) {
    this.keycloakService.isLoggedIn().then(
      (result) => {
        this.logged = result;
      }
    );
  
  }
  
  id: string | null;
  Id: number
  v: number;
  writerId: number;
  categories = [' ','Aventura','Acción','Ciencia ficción','Espiritual','Fantasía','Humor','Misterio','Novela histórica','Novela juvenil','Paranormal','Romance','Suspenso','Terror','Vampiros','Otros'];
  storyMostPopular: any = {};
  tagStoryMostPopular = [];
  colors = ['#b2d9e8', '#f9d7d6', '#c6ded4', '#c7bad8'];
  Chapters: any = [];
  infowriter = { username: '', email: '', url_pfp: '' };

  ngOnInit(){
    this.id = this.route.snapshot.paramMap.get('storyId');
    this.Id = Number(this.id);
    console.log("storyId: "+this.Id);

    this.StoryService.getStory(this.Id).subscribe({
      next:data=>{
        console.log("Informacion de la historia");
        console.log(data.data);
        this.storyMostPopular = data.data;
        this.v = this.storyMostPopular.categoryId;
        this.writerId = data.data.writerId;
        this.storyMostPopular.categoria = this.categories[this.v];
        // Obtenemos el userId del escritor
        if(this.logged == true){
          this.userService.getUserId(this.writerId).subscribe({
            next:data=>{
            console.log("UserId: "+data.data);
              this.userService.getUser(data.data).subscribe({
                next:data=>{
                  console.log("Informacion del escritor");
                  console.log(data.data);
                  this.infowriter.username = data.data.preferred_username;
                  this.infowriter.email = data.data.email;
                  if (data.data.url_pfp.trim().length <= 0) {
                    this.infowriter.url_pfp = '../../../assets/profile/pfp.svg';
                  } else {
                    this.infowriter.url_pfp = data.data.url_pfp;
                  }
                }
              });
           }
          });
        }
      }
    });
    // Tags de la historia
    this.StoryService.getTags(this.Id).subscribe({
      next: data => {
        console.log("Tags de la historia");
        console.log(data.data);
        
        this.tagStoryMostPopular = data.data.map((tag: { name: any }) => tag.name);
        console.log(this.tagStoryMostPopular);
      }
    });
    // Capitulos de la historia
    this.ChapterService.getChapterList(this.Id).subscribe({
      next:data=>{
        console.log(data.data);
        this.Chapters = data.data;
      }
    });  
  }

  Agregar(){
    console.log(this.Id);
    console.log("Historia agregada a la biblioteca")
    this.StoryService.addStoryToLibrary(this.Id).subscribe({
      next:data=>{
        console.log(data);
        alert("Historia agregada a la biblioteca");
      }
    });
  }

  Votar() {
    //Si no está logueado, no puede votar
    if (!this.logged) {
      alert("Debe iniciar sesión para votar");
    }else{
      console.log("Historia votada")
      this.StoryService.voteStory(this.Id).subscribe({
        next:data=>{
          console.log(data);
        }
      });
      location.reload();
    }
    
  }

}
