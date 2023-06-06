import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { ActivatedRoute } from '@angular/router';
import { UserDto } from 'src/app/dto/user.dto';

@Component({
  selector: 'app-external-followers',
  templateUrl: './external-followers.component.html',
  styleUrls: ['./external-followers.component.css']
})
export class ExternalFollowersComponent {

  constructor(private StoryService: StoryService, private userService: UserService, private route: ActivatedRoute) {}

  name: string | null ;
  username: string;
  followers: UserDto[] = [];
  
  ngOnInit(){
    this.name = this.route.snapshot.paramMap.get('user');
    console.log(this.name);
    this.username = String(this.name);
    console.log("Obteniendo información del usuario");
    this.userService.getUserByUsername(this.username)
    .subscribe({
      next:data => {
        console.log(data.data);
        this.userService.getFollowedUsers(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.followers = data.data;
          }
        })
      },
      error: (error) => console.log(error),
      },    
    );
  }

  
}
