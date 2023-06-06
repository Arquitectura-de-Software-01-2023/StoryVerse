import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { UserDto } from 'src/app/dto/user.dto';

@Component({
  selector: 'app-followers',
  templateUrl: './followers.component.html',
  styleUrls: ['./followers.component.css']
})
export class FollowersComponent {

  constructor(private StoryService: StoryService, private userService: UserService) {

  }
  storyId: string;
  Id: Number;

  // Followers
  followers: UserDto[] = [];
  
  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        this.userService.getFollowers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.followers = data.data;
          }
        })
      },
      error: (error) => console.log(error),
      },    
    )
  }

}