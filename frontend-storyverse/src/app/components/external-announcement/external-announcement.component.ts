import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {UserService} from '../../service/user.service';
import {StoryService} from '../../service/story.service';
import { UserDto } from 'src/app/dto/user.dto';
import { AnnouncementDto } from 'src/app/dto/announcement.dto';
import { format } from 'date-fns';

@Component({
  selector: 'app-external-announcement',
  templateUrl: './external-announcement.component.html',
  styleUrls: ['./external-announcement.component.css']
})
export class ExternalAnnouncementComponent {

  // Variables
  user: UserDto;
  announcements: AnnouncementDto[] = [];
  txtAreaAnnouncement = '';
  library_private: boolean;
  cantStories = "0";
  libraryStories="0";
  following = "0";

  constructor(private route: ActivatedRoute, private userService:UserService, private StoryService: StoryService ) { }


  name: string | null ;
  username: string;
  ngOnInit(){
    this.name = this.route.snapshot.paramMap.get('user');
    console.log(this.name);
    this.username = String(this.name);
    console.log("Obteniendo información del usuario");
    this.userService.getUserByUsername(this.username)
    .subscribe({
      next:data => {
        console.log(data.data);
        this.user = data.data;
        if(this.user.url_header.trim().length <= 0){
          this.user.url_header = '../../../assets/profile/header.svg';
        }
        if(this.user.url_pfp.trim().length <= 0){
          this.user.url_pfp = '../../../assets/profile/pfp.svg';
        }
        this.userService.getAnnouncementList(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.announcements = data.data;
            // Voltear el arreglo para que el anuncio más reciente esté primero
            this.announcements.reverse();
          }
        })
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.cantStories = data.data.length;
          }
        })
        if (this.library_private){
          this.StoryService.getLibraryList(data.data.preferred_username).subscribe({
            next:data=>{
              console.log(data.data);
              this.libraryStories = data.data.length;
            }
          })
        }
        this.userService.getFollowers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.following = data.data.length;
          }
        })
      },
      error: (error) => console.log(error),
      },    
    )
  }

  obtenerFormatoFecha(fecha: Date): string {
    fecha = new Date(fecha);
    return format(fecha, 'dd-MM-yyyy HH:mm');
  }
}
