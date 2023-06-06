import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.docker';

@Injectable({
  providedIn: 'root'
})
export class AwsSesService {

  constructor(private http: HttpClient) { }

  // Función para mandar un mensaje de verificación a AWS SES
  public sendVerificationMessage(email: String): Observable<any> {
    const body = {'email': email};
    return this.http.post(`https://9erb3ipb8h.execute-api.us-east-1.amazonaws.com/verify-email`, body);
  }

  // Función para mandar un mensaje mediante AWS SES
  public sendMessage(to: String, from: String, subject: String, text: String): Observable<any> {
    const body = {'to': to, 'from': from, 'subject': subject, 'text': text};
    return this.http.post(`https://9erb3ipb8h.execute-api.us-east-1.amazonaws.com/send-email`, body);
  }

  // Funcion para verificacion de creacion de usuario
  public verifyUser(username: String): Observable<any> {
    return this.http.get(`${environment.KEYCLOAK_URL}/admin/realms/software/users?username=${username}`);
  }

  // Funcion para guardar una notificacion
  public createNotification(owner: String, title: String, description: String){
    const body = {'owner': owner, 'title': title, 'description': description};
    return this.http.post(`https://cxo5pw3l1g.execute-api.us-east-1.amazonaws.com/notification`, body);
  } 

  // Funcion para obtener mis notificaciones
  public getMyNotifications(owner: String): Observable<any> {
    return this.http.get(`https://cxo5pw3l1g.execute-api.us-east-1.amazonaws.com/notification/${owner}`);
  }
}
