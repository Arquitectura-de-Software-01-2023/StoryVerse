import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { ChapterService } from '../../service/chapter.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  constructor(private StoryService: StoryService, private userService: UserService, private ChapterService: ChapterService) {
  }
  
  UserId: string;
  writerId: number;
  ownStories: any = [];
  chapters: any = [];
  cantStories = "0";
  libraryStories="0";
  following = "0";
  user = {
    name: '',
    username: '',
    email: '',
    password: '',
    birthdate: '',
    urlPfp: '../../../assets/profile/pfp.svg',
    urlHeader: '../../../assets/profile/header.svg',
    description: ''
  };

  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        this.user.username = data.data.preferred_username;
        this.user.description = data.data.description;
        if (data.data.url_header.trim().length <= 0){
          this.user.urlHeader = "../../../assets/profile/header.svg";
        } else {
          this.user.urlHeader = data.data.url_header;
        }
        if (data.data.url_pfp.trim().length <= 0){
          this.user.urlPfp = "../../../assets/profile/pfp.svg";
        } else {
          this.user.urlPfp = data.data.url_pfp;
        }
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.ownStories = data.data;
            this.cantStories = data.data.length;
            if (this.ownStories.length > 0){
              this.obtenerCapitulos();
            }
          }
        })
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
      },
      error: (error) => console.log(error),
      },    
    )
  }

  obtenerCapitulos(){
    this.ownStories.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          this.chapters = data.data;
          story.cantChapters = this.chapters.length;
        }
      });
    });
  }
}
