import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoryService } from '../../service/story.service';
import { ChapterService } from '../../service/chapter.service';
import { UserService } from '../../service/user.service';
import { KeycloakService } from 'keycloak-angular';
import { CommentDto } from 'src/app/dto/comment.dto';
import { UserDto } from 'src/app/dto/user.dto';
import { ChapterDto } from 'src/app/dto/chapter.dto';
import { format } from 'date-fns';

@Component({
  selector: 'app-viewchapter',
  templateUrl: './viewchapter.component.html',
  styleUrls: ['./viewchapter.component.css']
})


  export class ViewchapterComponent {
  constructor(private route: ActivatedRoute, private StoryService: StoryService, private ChapterService: ChapterService, private UserService:UserService, private keycloakService: KeycloakService) {
    this.keycloakService.isLoggedIn().then(
      (result) => {
        this.logged = result;
      }
    );
  }
  logged: boolean;
  chapter: ChapterDto;
  ComentarioUser: string;
  comentarios: CommentDto[] = [];
  user: UserDto;
  users: UserDto[] = [];
  id: string | null;
  Id: number
  NcommentId: number;
  urlPorDefecto = '../../../assets/profile/pfp.svg';
  usernamePorDefecto = 'Usuario';
  
  ngOnInit(){
    this.id = this.route.snapshot.paramMap.get('chapterId');
    console.log(this.id);
    this.Id = Number(this.id);
    console.log(this.Id);
    this.ChapterService.getChapter(this.Id).subscribe({
      next:data=>{
        console.log(data.data);
        this.chapter = data.data;
        console.log(this.chapter);
      }
    })
    this.ChapterService.getCommentList(this.Id).subscribe({
      next:data=>{
        this.comentarios = data.data.slice().reverse();
        console.log(this.comentarios);
        this.obtenerUsuarios();
        console.log(this.users);
      }
    })
    if(this.logged == true){
      this.UserService.getUserInfo().subscribe({
        next:data=>{
          this.user = data.data;
          if(this.user.url_pfp.trim().length <= 0)
            this.user.url_pfp = '../../../assets/profile/pfp.svg';
          console.log(this.user);
        }
      })
    }

  }
  obtenerUsuarios(){
    if(this.logged == true){
      for (let i = 0; i < this.comentarios.length; i++) {
        this.UserService.getUser(this.comentarios[i].userId).subscribe({
          next: data => {
            if(data.data.sub != this.user.sub){
              var usuario = data.data;
              if(usuario.url_pfp.trim().length <= 0)
                usuario.url_pfp = '../../../assets/profile/pfp.svg';
              if(usuario.url_header.trim().length <= 0)
                usuario.url_header = '../../../assets/profile/header.svg';
              this.users.push(data.data);
            }
          }
        })
      }
    }
  }

  getUser(userId: String): UserDto{
    //Recorremos la lista de usuarios
    for (let i = 0; i < this.users.length; i++) {
      //Si el id del usuario es igual al id del usuario que se pasó como parámetro
      if(this.users[i].sub == userId){
        //Retornamos el usuario
        return this.users[i];
      }
    }
    //Retornamos un userDto vacio
    return {
      sub: '',
      email: '',
      preferred_username: '',
      url_pfp: '',
      url_header: '',
      description: '',
      library_private: false,
      birthdate: new Date()
    }
  }

  Publicar(){
    console.log(this.Id);
    console.log(this.ComentarioUser);
    if(this.ComentarioUser == null) {
      alert('Debe llenar todos los campos');
    }else {
      if(this.logged == false){
        alert('Debe iniciar sesión para comentar');
      }else{
        this.ChapterService.publishComment(this.Id, this.ComentarioUser, new Date(), true);
        console.log('Comentario guardado');
        this.Reload();
      }
    }
  }

  Eliminar(commentId: number){
    this.NcommentId = Number(commentId);
    console.log("Comentario eliminado");
    console.log(commentId);
    this.UserService.deleteComment(this.NcommentId).subscribe({
      next:data=>{
        console.log(data.data);
        this.Reload();
      }
    })
  }

  Reload(){
    window.location.reload();
  }

  obtenerFormatoFecha(fecha: Date): string {
    fecha = new Date(fecha);
    return format(fecha, 'dd-MM-yyyy HH:mm');
  }

}
