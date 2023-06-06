import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ChapterService} from '../../service/chapter.service';
import { Router } from '@angular/router';
import { UserDto } from 'src/app/dto/user.dto';
import { UserService } from 'src/app/service/user.service';
import { AwsSesService } from 'src/app/service/aws-ses.service';
import { StoryService } from 'src/app/service/story.service';

@Component({
  selector: 'app-newchapter',
  templateUrl: './newchapter.component.html',
  styleUrls: ['./newchapter.component.css']
})
export class NewchapterComponent {

  id: string | null;
  Id: number;
  storyTitle: string | null;
  user: UserDto;
  followers: any[];

  constructor(private route: ActivatedRoute,
              private ChapterService:ChapterService,
              private router: Router,
              private userService: UserService,
              private awsSesService: AwsSesService,
              private storyService: StoryService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('storyId');
    this.Id = Number(this.id);
    this.storyTitle = this.route.snapshot.paramMap.get('storyTitle');
    // Obtener la informacion del usuario
    this.userService.getUserInfo().subscribe({
      next: data => {
        console.log(data.data);
        this.user = data.data;
      }
    });
  }

  //Campos del formulario
  inputTitle: string;
  txtAreaContent: string;

  saveChapter(){
    console.log("Capítulo guardado");
    console.log(this.id);
    console.log(this.inputTitle);
    console.log(this.txtAreaContent);
    if(this.inputTitle == null || this.txtAreaContent == null) {
      alert('Debe llenar todos los campos');
    }else {
      this.ChapterService.createChapter(this.Id, this.inputTitle ,this.txtAreaContent, new Date(), true);
      console.log('Capitulo guardada');
      this.userService.getFollowers(this.user.preferred_username).subscribe({
        next:data=>{
          this.followers = data.data;
          this.storyService.getLibraryUsersEmail(this.Id).subscribe({
            next: data => {
              this.followers = this.followers.concat(data.data);
              this.followers = [...new Set(this.followers)];
              console.log(this.followers);
              //Mandar un mensaje a todos los followers
              if (this.followers.length > 0) {
                this.followers.forEach((follower: any) => {
                  this.awsSesService.sendMessage(
                    follower.email, 
                    "storyverse27@gmail.com", 
                    "Nuevo anuncio de " + this.user.preferred_username, 
                    "¡Excelentes noticias! "+this.user.preferred_username+" ha agregado un nuevo capítulo a la historia '"+this.storyTitle +"'. ¡Descubre los nuevos giros que se revelan en esta fascinante historia!"
                    ).subscribe({
                    next: data => {
                      console.log(data);
                      this.awsSesService.createNotification(follower.preferred_username, "Nuevo anuncio de " + this.user.preferred_username, "¡Excelentes noticias! "+this.user.preferred_username+" ha agregado un nuevo capítulo a la historia '"+this.storyTitle +"'. ¡Descubre los nuevos giros que se revelan en esta fascinante historia!").subscribe({
                        next: data => {
                          console.log(data);
                        }
                      });
                      if (this.followers.indexOf(follower) == this.followers.length - 1) {
                        this.router.navigate(['/mystories']);
                      }
                    }
                  });
                });
              } else {
                this.router.navigate(['/mystories']);
              }
            }
          });
        }
      });
    }
  }

}
