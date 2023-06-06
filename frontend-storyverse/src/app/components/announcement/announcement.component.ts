import { Component } from '@angular/core';
import { UserService } from '../../service/user.service';
import { StoryService } from '../../service/story.service';
import { UserDto } from 'src/app/dto/user.dto';
import { AnnouncementDto } from 'src/app/dto/announcement.dto';
import { format } from 'date-fns';
import { AwsSesService } from 'src/app/service/aws-ses.service';

@Component({
  selector: 'app-announcement',
  templateUrl: './announcement.component.html',
  styleUrls: ['./announcement.component.css']
})
export class AnnouncementComponent {
  constructor(private userService: UserService, private StoryService: StoryService, private awsSesService: AwsSesService) {

  }

  // Variables
  user: UserDto;
  announcements : AnnouncementDto[] = [];
  txtAreaAnnouncement = '';
  cantStories = "0";
  libraryStories="0";
  following = "0";
  followers: any = [];

  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        this.user = data.data;
        this.user.url_header = data.data.url_header;
        this.user.url_pfp = data.data.url_pfp;
        this.user.email = data.data.email;
        this.userService.getAnnouncementList(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.announcements = data.data;
            //Voltear el arreglo para que el anuncio más reciente esté primero
            this.announcements.reverse();
          }
        })

        //obtener datos del usuario de biblioteca, seguidores e historias
        this.StoryService.getLibraryList(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.libraryStories = data.data.length;
          }
        })
        this.userService.getFollowers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.following = data.data.length;
            this.followers = data.data;
          }
        })
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.cantStories = data.data.length;
          }
        })
      },
      error: (error) => console.log(error),
      },    
    )
  }
  
  addAnnouncement() {
    console.log(this.txtAreaAnnouncement);
    this.userService.createAnnouncement(this.txtAreaAnnouncement).subscribe({
      next: data => {
        console.log(data);
        //Mandar un mensaje a todos los followers
        if (this.followers.length > 0) {
          this.followers.forEach((follower: any) => {
            this.awsSesService.sendMessage(
              follower.email, 
              "storyverse27@gmail.com", 
              "Nuevo anuncio de " + this.user.preferred_username, this.txtAreaAnnouncement).subscribe({
              next: data => {
                console.log(data);
                this.awsSesService.createNotification(follower.preferred_username, "Nuevo anuncio de " + this.user.preferred_username, this.txtAreaAnnouncement).subscribe({
                  next: data => {
                    console.log(data);
                  }
                });
                if (this.followers.indexOf(follower) == this.followers.length - 1) {
                  window.location.reload();
                }
              }
            })
          });
        } else {
          window.location.reload();
        }
      },
      error: (error) => console.log(error),
    });
  }

  deleteAnnouncement(announcementId: number) {
    this.userService.deleteAnnouncement(announcementId).subscribe({
      next: data => {
        console.log("Anuncio eliminado");
        window.location.reload();
      }
    })

  }

  obtenerFormatoFecha(fecha: Date): string {
    fecha = new Date(fecha);
    return format(fecha, 'dd-MM-yyyy HH:mm');
  }

  
}
