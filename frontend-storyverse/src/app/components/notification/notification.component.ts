import { Component } from '@angular/core';
import { UserDto } from 'src/app/dto/user.dto';
import { AwsSesService } from 'src/app/service/aws-ses.service';
import { UserService } from 'src/app/service/user.service';
import { format } from 'date-fns';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent {

  notifications: any = [];
  user: UserDto;

  constructor(private awsSesService: AwsSesService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getUserInfo()
      .subscribe({
        next: (data) => {
          this.user = data.data;
          this.awsSesService.getMyNotifications(this.user.preferred_username).subscribe({
            next: data => {
              this.notifications = data;
              this.notifications = this.notifications.body.notifications;
              console.log(this.notifications);
            }
          })
        },
        error: (error) => console.log(error),
      });
  }
  obtenerFormatoFecha(fecha: Date): string {
    fecha = new Date(fecha);
    return format(fecha, 'dd-MM-yyyy HH:mm');
  }

}
