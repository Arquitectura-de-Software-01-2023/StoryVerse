import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from '../../service/user.service';
import { ChapterService } from '../../service/chapter.service';

@Component({
  selector: 'app-mystories',
  templateUrl: './mystories.component.html',
  styleUrls: ['./mystories.component.css']
})
export class MystoriesComponent {

  constructor(private StoryService: StoryService, private userService: UserService, private ChapterService: ChapterService) {
  }
  
  Id: number;
  username: string;
  myStories: any = [];
  chapters: any = [];

  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        this.username = data.data.preferred_username;
        this.StoryService.getStoryListByUsername(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.myStories = data.data;
            if (this.myStories.length > 0){
              this.obtenerCapitulos();
            }
          }
        })
      },
      error: (error) => console.log(error),
    });
  }

  obtenerCapitulos(){
    this.myStories.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          this.chapters = data.data;
          story.cantChapters = this.chapters.length;
        }
      });
    });
  }

  borrar(storyId: string){
    this.Id = Number(storyId);
    this.StoryService.deleteStory(this.Id).subscribe({
      next:data=>{
        //se vuelven a cargar las historias
        this.StoryService.getStoryListByUsername(this.username).subscribe({
          next:data=>{
            console.log(data.data);
            this.myStories = data.data;
          }
        });
      }
    });
  }	

  getChapterList(storyId: string){
    this.Id = Number(storyId);
    this.ChapterService.getChapterList(this.Id).subscribe({
      next:data=>{
        console.log(data.data);
        this.chapters = data.data;
      }
    });  
  }

}
