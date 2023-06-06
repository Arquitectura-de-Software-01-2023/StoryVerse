import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { environment } from 'src/environments/environment.docker';

@Injectable({
  providedIn: 'root'
})
export class ChapterService {

  private storyUrl = `${environment.STORY_URL}/api/v1/story`;
    private gatewayUrl = `${environment.GATEWAY_URL}`;


  constructor(private http: HttpClient) { }
  
  // Funcion para crear un capitulo
  public createChapter(storyId: number, title:string, description:string,publicationDate:Date, status: boolean ) {
    const header = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    //'Authorization': 'Bearer $token',
    };
    const body = {
      'storyId': storyId,
      'title': title,
      'description': description,
      'publicationDate': publicationDate,
      'status': status
    }
    this.http.post(`${this.gatewayUrl}/${this.storyUrl}/chapter/new`, body, {headers: header}).subscribe(data => { });
    
  }

  // Funcion para editar un capitulo
  public editChapter(chapterId: number, storyId: number, title:string, description:string,publicationDate:Date, status: boolean ) {
      const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      //'Authorization': 'Bearer $token',
      };
      const body = {
        'chapterId': chapterId,
        'storyId': storyId,
        'title': title,
        'description': description,
        'publicationDate': publicationDate,
        'status': status
      }
      this.http.put(`${this.gatewayUrl}/${this.storyUrl}/chapter`, body, {headers: header}).subscribe(data => { });

  }

  // Funcion para obtener un capitulo
  public getChapter(chapterId: number): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/chapter?chapterId=${chapterId}`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/chapter?chapterId=${chapterId}`);
  }

  // Funcion para obtener todos los capitulos de una historia
  public getChapterList(storyId: number): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/${storyId}/chapter/list`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/${storyId}/chapter/list`);
  }

  //Funcion para publicar un comentario de un capitulo
  public publishComment(chapterId: number, comment: string,publicationDate:Date, status: boolean) {
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      //'Authorization': 'Bearer $token',
      };
      const body = {
        'userId': "x", //Cambiar por el id del usuario logeado
        'chapterId': chapterId,
        'description': comment,
        'date': publicationDate,
        'status': status
      }
    this.http.post(`${this.gatewayUrl}/${this.storyUrl}/chapter/comment/new`, body, {headers: header}).subscribe(data => { });
  }

  // Funcion para obtener todos los comentarios de un capitulo
  public getCommentList(chapterId: number): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/chapter/${chapterId}/comment/list`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/chapter/${chapterId}/comment/list`);
  }
  // Funcion para eliminar un capitulo
  public deleteChapter(chapterId: number) {
    //return this.http.delete(`${this.storyUrl}/chapter/${chapterId}`);
    return this.http.delete(`${this.gatewayUrl}/${this.storyUrl}/chapter?chapterId=${chapterId}`);
  }

}


