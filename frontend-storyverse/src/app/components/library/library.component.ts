import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { ChapterService } from '../../service/chapter.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.css']
})
export class LibraryComponent {
  constructor(private StoryService: StoryService, private userService: UserService, private ChapterService: ChapterService) {

  }
  storyId: string;
  Id: Number;
  library_private: boolean = false;
  
  ngOnInit(){
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next:data => {
        console.log(data.data);
        this.library_private = data.data.library_private;
        this.StoryService.getLibraryList(data.data.preferred_username).subscribe({
          next:data=>{
            console.log(data.data);
            this.storiesLibrary = data.data;
            //Voltear el arreglo para que se muestren primero las historias más recientes
            this.storiesLibrary.reverse();
            if (this.storiesLibrary.length > 0){
              this.obtenerCapitulos();
            }
          }
        })
      },
      error: (error) => console.log(error),
    });
  }

  chapters: any = [];

  obtenerCapitulos() {
    this.storiesLibrary.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          this.chapters = data.data;
          story.cantChapters = this.chapters.length;
        }
      });
    });
  }

  // Historias de su biblioteca
  storiesLibrary: any = [];

  deleteStory(storyId: string) {
    this.Id = Number(storyId)
    console.log('Se elimino la historia con id: ' + storyId);
    this.StoryService.deleteStoryFromLibrary(this.Id).subscribe(
      res => {
        console.log(res);
        this.Reload();
      }
    );

  }
  //Recarga la pagina
  Reload(): void {
    window.location.reload();
  }

  //Actualizamo la vista
  refresh(storyId: number): void {
    this.StoryService.refresh(storyId)
      .subscribe(
        response => {
          console.log(response);
        }
      );
  }

}
