import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoryService } from '../../service/story.service';
import { ChapterService } from '../../service/chapter.service';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent {

  logged: boolean;
  searchTitle: string | null;

  constructor(private route: ActivatedRoute, private StoryService: StoryService, private keycloakService: KeycloakService, private ChapterService: ChapterService) {
    this.keycloakService.isLoggedIn().then(
      (result) => {
        this.logged = result;
      }
    );
  }

  // Historias resultado de la busqueda
  storiesResult: any = [];

  ngOnInit() {
    this.searchTitle = this.route.snapshot.paramMap.get('searchTitle');
    console.log(this.searchTitle);
    if (this.searchTitle != null) {
      this.StoryService.getStorySearchList(this.searchTitle).subscribe({
        next:data=>{
          console.log(data.data);
          this.storiesResult = data.data;
          if (this.storiesResult.length > 0){
            this.obtenerCapitulos();
          }
        }
      })
    }
  }

  chapters: any = [];

  obtenerCapitulos() {
    this.storiesResult.forEach((story: any) => {
      this.ChapterService.getChapterList(story.storyId).subscribe({
        next: data => {
          this.chapters = data.data;
          story.cantChapters = this.chapters.length;
        }
      });
    });
  }

}
