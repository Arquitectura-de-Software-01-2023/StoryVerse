import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { ActivatedRoute } from '@angular/router';
import { UserDto } from 'src/app/dto/user.dto';

@Component({
  selector: 'app-external-following',
  templateUrl: './external-following.component.html',
  styleUrls: ['./external-following.component.css']
})
export class ExternalFollowingComponent {
  constructor(private StoryService: StoryService, private userService: UserService, private route: ActivatedRoute) {

  }
  user : UserDto;
  name: string | null ;
  username: string;
  followings : UserDto[] = [];

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
        this.userService.getFollowedUsers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.followings = data.data;
          }
        })
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.cantStories = data.data.length;
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
  cantStories = "0";
  libraryStories="0";
  following = "0";
  
}
