import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.docker';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AwsS3Service {

  constructor(private http: HttpClient) { }

  // Función para subir un archivo a AWS S3
  public uploadFile(file: String): Observable<any> {
    const body = {'file': file};
    return this.http.post(environment.AWS_S3_URL, body);
  }

  // Funcion para eliminar un archivo de AWS S3
  public deleteFile(file: string) {
    return this.http.delete(`${environment.AWS_S3_URL}/${file}`);
  }
}
