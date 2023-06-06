import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../service/user.service';
import { StoryService } from '../../service/story.service';
import { ChapterService } from 'src/app/service/chapter.service';
import { UserDto } from 'src/app/dto/user.dto';
import { StoryDto } from 'src/app/dto/story.dto';

@Component({
  selector: 'app-external-profile',
  templateUrl: './external-profile.component.html',
  styleUrls: ['./external-profile.component.css']
})

export class ExternalProfileComponent {
  constructor(private route: ActivatedRoute, private UserService: UserService, private StoryService: StoryService, private ChapterService: ChapterService) { }

  // Datos del usuario
  name: string | null ;
  username: string;
  siguiendo: boolean = false;
  user : UserDto;
  stories : any[] = [];

  ngOnInit() {
    this.name = this.route.snapshot.paramMap.get('user');
    console.log(this.name);
    this.username = String(this.name);
    this.UserService.getUserByUsername(this.username).subscribe({
      next: data => {
        console.log(data.data);
        this.user = data.data;
        if(this.user.url_header.trim().length <= 0){
          this.user.url_header = '../../../assets/profile/header.svg';
        }
        if(this.user.url_pfp.trim().length <= 0){
          this.user.url_pfp = '../../../assets/profile/pfp.svg';
        }
        this.UserService.follows(this.username).subscribe({
          next: data => {
            console.log(data.data);
          
            this.siguiendo = data.data;
          }
        });
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.stories = data.data;
            this.cantStories = data.data.length;
            if (this.stories.length > 0){
              this.obtenerCapitulos();
            }
          }
        })
        if (this.user.library_private){
          this.StoryService.getLibraryList(data.data.preferred_username).subscribe({
            next:data=>{
              console.log(data.data);
              this.libraryStories = data.data.length;
            }
          })
        }
        this.UserService.getFollowers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.following = data.data.length;
          }
        }) 
      }
    });
  }

  obtenerCapitulos(){
    this.stories.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          story.cantChapters = data.data.length;
        }
      });
    });
  }

  seguir(){
    this.UserService.followUser(this.username).subscribe({
      next: data => {
        console.log('Siguiendo');
        alert('Siguiendo a ' + this.username);
        window.location.reload();
      }
    });
  }

  dejarDeSeguir(){
    this.UserService.unfollowUser(this.username).subscribe({
      next: data => {
        console.log('Dejando de seguir');
        alert('Dejaste de seguir a ' + this.username);
        window.location.reload();
      }
    });
  }

  cantStories = "0";
  libraryStories="0";
  following = "0";
}
