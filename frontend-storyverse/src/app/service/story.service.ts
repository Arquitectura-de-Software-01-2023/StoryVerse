import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { environment } from 'src/environments/environment.docker';

@Injectable({
  providedIn: 'root'
})
export class StoryService {

  private storyUrl = `${environment.STORY_URL}/api/v1/story`;
  private gatewayUrl = `${environment.GATEWAY_URL}`;
  
  constructor(private http: HttpClient) { }

  // Funcion para crear una historia
  public createStory(categoryId: string,title: string,  urlBookCover: string, description: string, audiencyId: string, publicationDate: Date, vote: number, status: boolean, tags: string[]): Observable<any> {
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      //'Authorization': 'Bearer $token',
    };
    const body = {
      'writerId': null,
      'categoryId': categoryId,
      'title': title,
      'urlCover': urlBookCover,
      'description': description,
      'audience': audiencyId,
      'language': 'Español',
      'publicationDate': publicationDate,
      'vote': vote,
      'status': status,
      'tags': tags
    };
    return this.http.post(`${this.gatewayUrl}/${this.storyUrl}/new`, body, {headers: header});
  }

  // Funcion para editar una historia
  public editStory(storyId: number, writerId:number, categoryId: number,title: string,  urlBookCover: string, description: string, audiencyId: string, publicationDate: Date, vote: number, status: boolean) {
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      //'Authorization': 'Bearer $token',
    };
    const body = {
      'storyId': storyId,
      'writerId': writerId,
      'categoryId': categoryId,
      'title': title,
      'urlCover': urlBookCover,
      'description': description,
      'audience': audiencyId,
      'language': 'Español',
      'publicationDate': publicationDate,
      'vote': vote,
      'status': status
    };
    this.http.put(`${this.gatewayUrl}/${this.storyUrl}`, body, {headers: header}).subscribe(data => { });
  }

  // Funcion para obtener las historias que no son del usuario
  public getStoryList(): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/list?&page=0&size=16`);
  }

  // Funcion para obtener historias dado un usuario
  public getStoryListByUsername(username: string): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/list/yours?writerId=${writerId}&page=0&size=10`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/${username}?page=0&size=30`);
  }

  // Funcion para obtener historias dado un id
  public getStory(storyId: number): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}?storyId=${storyId}`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}?storyId=${storyId}`);
  }

  // Funcion para obtener historias dado un string
  public getStorySearchList(search: String): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/list/title?title=${search}&page=0&size=10`);
    //return this.http.get<any>(`http://localhost:8111/ms-story/api/v1/story/list/title?title=${search}&page=0&size=10`);

  }

  // Funcion para borrar una historia
  public deleteStory(storyId: number) {
    //return this.http.delete(`${this.storyUrl}?storyId=${storyId}`);
    return this.http.delete(`${this.gatewayUrl}/${this.storyUrl}?storyId=${storyId}`);
  }

  // Funcion para agregar una historia a mi biblioteca
  public addStoryToLibrary(storyId: number) {
    // return this.http.post(`${this.storyUrl}/library?userId=Abcd1234&storyId=${storyId}`, null);
    return this.http.post(`${this.gatewayUrl}/${this.storyUrl}/library?storyId=${storyId}`, null);
  }

  // Funcion para obtener id de escritor
  public getWriterId(userId:String): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/writerId?userId=${userId}`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/writerId?userId=${userId}`);
  }
  // Funcion para obtener historias dada por una categoria
  public getStoryCategoryList(page: number = 0, size:number=10, categoryId: number): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/list/yours?writerId=${writerId}&page=0&size=10`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/list/category?categoryId=${categoryId}&page=${page}&size=${size}`)
  }

  // Funcion para obtener historias dada por un username (biblioteca)
  public getLibraryList(username: string): Observable<any>{
    //return this.http.get<any>(`${this.storyUrl}/list/yours?writerId=${writerId}&page=0&size=10`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/library?username=${username}`)
  }

  // Funcion para eliminar una historia de mi biblioteca
  public deleteStoryFromLibrary(storyId: Number) {
    //return this.http.delete(`${this.storyUrl}/library?userId=Abcd1234&storyId=${storyId}`);
    return this.http.delete(`${this.gatewayUrl}/${this.storyUrl}/library?storyId=${storyId}`);
  }


  // Funcion para crear tag de una historia
  public createTag(storyId: number, tag: string) {
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      //'Authorization': 'Bearer $token',
    };
    const body = tag;
    this.http.post(`${this.gatewayUrl}/${this.storyUrl}/tags?storyId=${storyId}`, body, {headers: header}).subscribe(data => { });
  }
  // Funcion para actualizar tag de una historia
  public updateTag(storyId: number, tag: string[]) {
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    const body = tag;
    this.http.put(`${this.gatewayUrl}/${this.storyUrl}/tags?storyId=${storyId}`, body, {headers: header}).subscribe(data => { });
  }
  //Obterner tags de una historia
  public getTags(storyId: number): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/tags?storyId=${storyId}`);
  }
  //Votar por una historia
  public voteStory(storyId: number) {
    return this.http.put(`${this.gatewayUrl}/${this.storyUrl}/vote?storyId=${storyId}`, null);
  }

  //Actualizar la vista - rabbitmq
  public refresh(storyId: number): Observable<any> {
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/library/refresh?storyId=${storyId}`);
  }
  
  // Obtener los usuarios que guardaron la historia en la biblioteca
  public getLibraryUsersEmail(storyId: number): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/library/users/email?storyId=${storyId}`);
  }

  //Obtener la cantidad de libros en una categoria
  public getCategoryCount(categoryId: number): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/category/count?categoryId=${categoryId}`);
  }
}
