import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoryService } from '../../service/story.service';
import { ChapterService } from '../../service/chapter.service';
import { KeycloakService } from 'keycloak-angular';
import { PageEvent } from '@angular/material/paginator';


@Component({
  selector: 'app-category-result',
  templateUrl: './category-result.component.html',
  styleUrls: ['./category-result.component.css']
})
export class CategoryResultComponent {

  logged: boolean;
  categoryId: string | null;
  Id: number;
  length: number = 10;
  pageSize: number = 10;
  pageIndex: number = 0;

  constructor(private route: ActivatedRoute, private StoryService: StoryService, private keycloakService: KeycloakService, private ChapterService: ChapterService) {
    this.keycloakService.isLoggedIn().then(
      (result) => {
        this.logged = result;
      }
    );
  }

  // Historias resultado de la busqueda
  storiesResult: any = [];
  categories = ['Aventura','Acción','Ciencia ficción','Espiritual','Fantasía','Humor','Misterio','Novela histórica','Novela juvenil','Paranormal','Romance','Suspenso','Terror','Vampiros','Otros'];
  title = "";
  chapters: any = [];

  ngOnInit() {
    this.categoryId = this.route.snapshot.paramMap.get('categoryId');
    this.Id = Number(this.categoryId);
    this.title = this.categories[this.Id-1];
    this.StoryService.getCategoryCount(this.Id)
    .subscribe({
      next:(data: { data: number; }) => {
        console.log(data.data);
        this.length = data.data;
      }
    })

    this.StoryService.getStoryCategoryList(this.pageIndex, this.pageSize, this.Id).subscribe({
      next:data=>{
        console.log(data.data);
        this.storiesResult = data.data;
        if (this.storiesResult.length > 0){
          this.obtenerCapitulos();
        }
      }
    })
  }

  obtenerCapitulos(){
    this.storiesResult.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          this.chapters = data.data;
          story.cantChapters = this.chapters.length;
        }
      });
    });
  }

  pageEvent: PageEvent;
  handlePageEvent(e: PageEvent){
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;

    //Se obtienen las historias de la página actual
    this.StoryService.getStoryCategoryList(this.pageIndex,this.pageSize,Number(this.categoryId)).subscribe({
      next:data=>{
        console.log(data.data);
        this.storiesResult = data.data;
      },
      error: (error) => console.log(error),
    })
  }

}
