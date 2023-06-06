import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { UserDto } from 'src/app/dto/user.dto';

@Component({
  selector: 'app-following',
  templateUrl: './following.component.html',
  styleUrls: ['./following.component.css']
})
export class FollowingComponent {

  constructor(private StoryService: StoryService, private userService: UserService) {  }
  
  // Datos del usuario
  storyId: string;
  Id: Number;
  user : UserDto;
  followings : UserDto[] = [];
  cantStories: string;
  libraryStories: string;
  following: string;
  
  ngOnInit() {
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
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
        this.userService.getFollowedUsers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.followings = data.data;
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

  unfollow(preferred_username: string) {
    this.followings = this.followings.filter((following: any) => following.preferred_username !== preferred_username);
    console.log( preferred_username);
    this.userService.unfollowUser(preferred_username).subscribe({
      next: data => {
        console.log('Se dejo de seguir al usuario');
      }
    });
  }

}
