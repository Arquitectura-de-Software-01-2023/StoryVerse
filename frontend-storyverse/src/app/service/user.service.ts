import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.docker';
import { HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private storyUrl = `${environment.STORY_URL}/api/v1/story`;
  private gatewayUrl = `${environment.GATEWAY_URL}`;
  private userUrl = `${environment.USER_URL}/api/v1/user`;

  constructor(private http: HttpClient) { }

  //Conectamos con backend
  public getUserInfo(): Observable<any>{
    //return this.http.get<any>(`${environment.USER_URL}/api/v1/user/`);
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/`);
  }
  //Funcion para actualizar un usuario
  public editProfile(email:string, description:string, urlHeader:string, urlPfp:string, birthdate:Date, libraryPrivate:boolean): Observable<any>{
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    const body = {
      "email": email,
      "attributes": {
        "birthdate": birthdate,
        "library_private": libraryPrivate,
        "url_header": urlHeader,
        "url_pfp": urlPfp,
        "description": description
      }
    }
    return this.http.put(`${this.gatewayUrl}/${this.userUrl}/`, body, {headers: header});
  }
  //Actualizar contraseña
  public editPassword(Password:string): Observable<any>{
    const header = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    const body = {
      "type": "Password",
      "value": Password,
      "temporary": false

    }
    return this.http.put(`${this.gatewayUrl}/${this.userUrl}/change-password`, body, {headers: header})
  }

  //Eliminar cuenta de usuario
  public deleteUser(): Observable<any>{
    return this.http.delete<any>(`${this.gatewayUrl}/${this.userUrl}/`);
  }



  // Publicar un anuncio
  public createAnnouncement(description:string): Observable<any>{
    return this.http.post(`${this.gatewayUrl}/${this.userUrl}/announce?announce=${description}`, null)
  }

  // Obtener todos los anuncios de un usuario
  public getAnnouncementList(username: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/${username}/announcements`);
  }

  // Eliminar un anuncio
  public deleteAnnouncement(announcementId: number): Observable<any>{
    return this.http.delete<any>(`${this.gatewayUrl}/${this.userUrl}/announce?announce=${announcementId}`);
  }
  // Obtener el userId mediante el writerId
  public getUserId(writerId: number): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.storyUrl}/writer/userId?writerId=${writerId}`);
  }
  //Obtener el usuario mediante el userId
  public getUser(userId: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/userId/${userId}`);
  }
  //Obtener el usuario mediante el username
  public getUserByUsername(username: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/${username}`);
  }
  //Funcion para seguir a un usuario
  public followUser(usename: string): Observable<any>{
    return this.http.post<any>(`${this.gatewayUrl}/${this.userUrl}/follow?target=${usename}`, null);
  }
  //Funcion para dejar de seguir a un usuario
  public unfollowUser(usename: string): Observable<any>{
    return this.http.post<any>(`${this.gatewayUrl}/${this.userUrl}/unfollow?target=${usename}`, null);
  }
  //Obtener todos los seguidos de un usuario
  public getFollowedUsers(username: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/${username}/following`);
  }
  //Obtener todos los seguidores de un usuario
  public getFollowers(username: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/${username}/followers`);
  }
  //Obtener un booleano si el usuario logueado sigue a otro usuario
  public follows(username: string): Observable<any>{
    return this.http.get<any>(`${this.gatewayUrl}/${this.userUrl}/follows?target=${username}`);
  }
  //Eliminar comentario de un capitulo
  public deleteComment(commentId: number): Observable<any>{
    return this.http.delete<any>(`${this.gatewayUrl}/${this.storyUrl}/chapter/comment?commentId=${commentId}`);
  }

}
