import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ChapterService} from '../../service/chapter.service';


@Component({
  selector: 'app-edit-chapter',
  templateUrl: './edit-chapter.component.html',
  styleUrls: ['./edit-chapter.component.css']
})
export class EditChapterComponent {

  storyId: string | null;
  chapterId: string | null;
  NstoryId: number;
  NchapterId: number;

  constructor(private route: ActivatedRoute,
              private ChapterService: ChapterService) { }

  ngOnInit() {
    this.storyId = this.route.snapshot.paramMap.get('storyId');
    this.chapterId = this.route.snapshot.paramMap.get('chapterId');
    this.NstoryId = Number(this.storyId);
    this.NchapterId = Number(this.chapterId);
    this.ChapterService.getChapter(this.NchapterId).subscribe({
      next:data=>{
        console.log(data.data);
        this.chapter = data.data;
        this.inputTitle = this.chapter.title;
        this.txtAreaContent = this.chapter.description;
      }
    });
  }

  //Información del capítulo
  chapter = {
    title: "",
    description:''
  }
  //Campos del formulario
  inputTitle: string = this.chapter.title;
  txtAreaContent: string = this.chapter.description;

  saveChangesChapter(){
    console.log(this.storyId);
    console.log(this.chapterId);
    console.log(this.inputTitle);
    console.log(this.txtAreaContent);
    if(this.inputTitle == null || this.txtAreaContent == null) {
      alert('Debe llenar todos los campos');
    }else {
      console.log('Capitulo guardada');
      this.ChapterService.editChapter(this.NchapterId,this.NstoryId, this.inputTitle, this.txtAreaContent, new Date(), true);
      alert('Capítulo editado');
      window.location.href = "/mystories";
    }
  }
  eliminarCapitulo(){
    console.log("Eliminando capítulo");
    this.ChapterService.deleteChapter(this.NchapterId).subscribe({
      next:data=>{
        alert('Capítulo eliminado');
        this.myStories();
      }
    });
  }
  //Funcion para volver al myStories
  myStories(){
    window.location.href = "/mystories";
  }
}
