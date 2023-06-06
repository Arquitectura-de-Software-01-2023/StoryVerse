import { Component } from '@angular/core';
import { UserService } from '../../service/user.service';
import { UserDto } from 'src/app/dto/user.dto';
import { format } from 'date-fns';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {

  //formularioEditarUsuario:FormGroup;
  date: Date = new Date();
  email: string;
  birthdate: string;
  user: UserDto;
  library_private: boolean;
  //Campos del formulario
  inputTitle: string;
  inputEmail: string;
  inputBirthdate: Date;

  constructor( private userService: UserService) {
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next: (data) => {
        console.log(data);
        this.user = data.data;
        this.inputEmail = data.data.email;
        this.birthdate = format(new Date(data.data.birthdate), 'yyyy-MM-dd');
        this.library_private = data.data.library_private;
      },
      error: (error) => console.log(error),
      },    
    )
    
    }

  //Contraseña
  showPassword = false;

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  // Guardar valor del checkbox
  saveCheckbox() {
    this.library_private = !this.library_private;
  }

  inputPassword: string = "";
  editarDatosUsuario():any{
    console.log(this.library_private)
    //Damos formato a la fecha
    console.log(this.inputBirthdate);
    if(this.inputBirthdate == undefined ){
      this.inputBirthdate = new Date(this.birthdate);
      //añadimos un dia
      this.inputBirthdate.setDate(this.inputBirthdate.getDate() + 1);
      console.log(this.inputBirthdate);
    }
    this.userService.editProfile(this.inputEmail,this.user.description, this.user.url_header, this.user.url_pfp, this.inputBirthdate, this.library_private)
    .subscribe({
      next: (data) => {
        console.log(data);
        console.log(this.inputPassword)
        if(this.inputPassword.length > 0){
          this.userService.editPassword(this.inputPassword)
          .subscribe({
            next: (data) => {
              console.log("Se cambio la contraseña");
            },
            error: (error) => console.log(error),
          });

        }
        alert("Datos actualizados");
        //Redireccionamos a la página de perfil
         window.location.href = "/profile";
      },
    });
  }

  cancelar() {
    window.location.href = "/profile";
  }

  eliminarCuenta(){
    this.userService.deleteUser().subscribe({
      next: (data) => {
        console.log(data);
        alert("Cuenta eliminada");
        window.location.href = "/";
      }
    });
  }
}
