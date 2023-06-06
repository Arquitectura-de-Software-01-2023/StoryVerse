import { Component } from '@angular/core';
import { StoryService } from '../../service/story.service';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';
import { AwsS3Service } from 'src/app/service/aws-s3.service';
import { UserDto } from 'src/app/dto/user.dto';
import { AwsSesService } from 'src/app/service/aws-ses.service';

@Component({
  selector: 'app-newstory',
  templateUrl: './newstory.component.html',
  styleUrls: ['./newstory.component.css']
})
export class NewstoryComponent {
  
  constructor(private StoryService: StoryService, private userService: UserService, private router: Router, private awsS3Service: AwsS3Service, private awsSesService: AwsSesService) { }
  
  //Información
  categories = [
    {id: 1, name: "Aventura"},
    {id: 2, name: "Acción"},
    {id: 3, name: "Ciencia Ficción"},
    {id: 4, name: "Espiritual"},
    {id: 5, name: "Fantasía"},
    {id: 6, name: "Humor"},
    {id: 7, name: "Misterio"},
    {id: 8, name: "Novela histórica"},
    {id: 9, name: "Novela juvenil"},
    {id: 10, name: "Paranormal"},
    {id: 11, name: "Romance"},
    {id: 12, name: "Suspenso"},
    {id: 13, name: "Terror"},
    {id: 14, name: "Vampiros"},
    {id: 15, name: "Otros"}
  ]
  audiencies = [
    {id: 1, name: "Todo público"},
    {id: 2, name: "Mayores de 13"},
    {id: 3, name: "Mayores de 18"},
    {id: 4, name: "Mayores de 21"}
  ];

  //Campos del formulario
  selectedBookCover: File;
  urlBookCover: string = '../../../assets/profile/book-cover.png';
  inputTitle: string;
  txtAreaDescription: string;
  selectCategory: string = '0';
  selectAudiency: string = '0';
  inputTags: string;
  followers: any = [];
  user: UserDto;
  storyId: String;

  ngOnInit(){
    this.userService.getUserInfo().subscribe({
      next: data => {
        console.log(data.data);
        this.user = data.data;
      }
    });
  }

  onFileSelectedBookCover(event: any) {
    this.selectedBookCover = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.result != null) {
        this.urlBookCover = reader.result.toString();
      }
    };
    reader.readAsDataURL(this.selectedBookCover);
  }
  
  saveStory() {
    if (this.inputTitle == null || this.txtAreaDescription == null || this.inputTags == null){
      alert('Debe llenar todos los campos');
    } else {
      if (this.inputTitle.trim().length <= 0 || this.txtAreaDescription.trim().length <= 0 || this.selectAudiency == '0' || this.selectCategory == '0' || this.inputTags.trim().length <= 0) {
        alert('Debe llenar todos los campos');
      } else {
        if (this.urlBookCover != '../../../assets/profile/book-cover.png') {
          this.awsS3Service.uploadFile(this.urlBookCover).subscribe({
            next: data => {
              console.log(data);
              console.log('Imagen guardada');
              this.urlBookCover = data.uploadResult.Location;
              let tags = this.inputTags.split(',').map((tag: string) => tag.trim());
              this.StoryService.createStory(this.selectCategory, this.inputTitle,  this.urlBookCover, this.txtAreaDescription, this.selectAudiency, new Date(), 0, true, tags).subscribe({
                next: data => {
                  console.log(data);
                  this.storyId = data.data;
                  console.log('Historia guardada');
                  this.userService.getFollowers(this.user.preferred_username).subscribe({
                    next:data=>{
                      console.log(data.data);
                      this.followers = data.data;
                      //Mandar un mensaje a todos los followers
                      if (this.followers.length > 0) {
                        this.followers.forEach((follower: any) => {
                          this.awsSesService.sendMessage(
                            follower.email, 
                            "storyverse27@gmail.com", 
                            "Nuevo anuncio de " + this.user.preferred_username, 
                            "¡Enhorabuena! "+this.user.preferred_username+" ha escrito una nueva historia: '"+this.inputTitle +"'. ¡No te la pierdas y adéntrate en esta aventura única!"
                            ).subscribe({
                            next: data => {
                              console.log(data);
                              this.awsSesService.createNotification(follower.preferred_username, "Nuevo anuncio de " + this.user.preferred_username, "¡Enhorabuena! "+this.user.preferred_username+" ha escrito una nueva historia: '"+this.inputTitle +"'. ¡No te la pierdas y adéntrate en esta aventura única!").subscribe({
                                next: data => {
                                  console.log(data);
                                }
                              });
                              if (this.followers.indexOf(follower) == this.followers.length - 1) {
                                this.router.navigate(['/newchapter', this.storyId, this.inputTitle]);
                              }
                            }
                          })
                        });
                      } else {
                        this.router.navigate(['/newchapter', this.storyId, this.inputTitle]);
                      }
                    }
                  });
                },
              });
            },
          });
        } else {
          let tags = this.inputTags.split(',').map((tag: string) => tag.trim());
          console.log(this.urlBookCover);
          this.StoryService.createStory(this.selectCategory, this.inputTitle,  this.urlBookCover, this.txtAreaDescription, this.selectAudiency, new Date(), 0, true, tags).subscribe({
            next: data => {
              console.log(data);
              this.storyId = data.data;
              console.log('Historia guardada');
              this.userService.getFollowers(this.user.preferred_username).subscribe({
                next:data=>{
                  console.log(data.data);
                  this.followers = data.data;
                  //Mandar un mensaje a todos los followers
                  if (this.followers.length > 0) {
                    this.followers.forEach((follower: any) => {
                      this.awsSesService.sendMessage(
                        follower.email, 
                        "storyverse27@gmail.com", 
                        "Nuevo anuncio de " + this.user.preferred_username, 
                        "¡Enhorabuena! "+this.user.preferred_username+" ha escrito una nueva historia: '"+this.inputTitle +"'. ¡No te la pierdas y adéntrate en esta aventura única!"
                        ).subscribe({
                        next: data => {
                          console.log(data);
                          this.awsSesService.createNotification(follower.preferred_username, "Nuevo anuncio de " + this.user.preferred_username, "¡Enhorabuena! "+this.user.preferred_username+" ha escrito una nueva historia: '"+this.inputTitle +"'. ¡No te la pierdas y adéntrate en esta aventura única!").subscribe({
                            next: data => {
                              console.log(data);
                            }
                          });
                          if (this.followers.indexOf(follower) == this.followers.length - 1) {
                            this.router.navigate(['/newchapter', this.storyId, this.inputTitle]);
                          }
                        }
                      })
                    });
                  } else {
                    this.router.navigate(['/newchapter', this.storyId, this.inputTitle]);
                  }
                }
              });
            },
          });
        }
      }
    }
  }
}