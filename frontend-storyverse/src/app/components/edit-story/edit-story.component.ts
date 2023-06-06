import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StoryService } from '../../service/story.service';
import { UserService } from '../../service/user.service';
import { AwsS3Service } from 'src/app/service/aws-s3.service';

@Component({
  selector: 'app-edit-story',
  templateUrl: './edit-story.component.html',
  styleUrls: ['./edit-story.component.css']
})
export class EditStoryComponent {

  constructor(private route: ActivatedRoute, private StoryService: StoryService, private UserService: UserService, private awsS3Service: AwsS3Service) { }

  //Información
  categories = [{id: 1, name: "Aventura"},{id: 2, name: "Acción"},{id: 3, name: "Ciencia Ficción"},{id: 4, name: "Espiritual"},
  {id: 5, name: "Fantasía"},{id: 6, name: "Humor"},{id: 7, name: "Misterio"},{id: 8, name: "Novela histórica"},
  {id: 9, name: "Novela juvenil"},{id: 10, name: "Paranormal"},{id: 11, name: "Romance"},{id: 12, name: "Suspenso"},
  {id: 13, name: "Terror"},{id: 14, name: "Vampiros"},{id: 15, name: "Otros"}];
  audiencies = [{id: 1, name: "Todo público"},{id: 2, name: "Mayores de 13"},{id: 3, name: "Mayores de 18"},{id: 4, name: "Mayores de 21"}];

  // Campos del formulario
  inputTitle: string;
  txtAreaDescription: string;
  selectCategory: number;
  selectAudiency: number;
  inputTags: string;
  urlBookCover: string;
  votes: number;

  // Cargar la imagen
  selectedBookCover: File;
  aux: string;
  onFileSelectedBookCover(event: any) {
    this.selectedBookCover = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.result != null){
        this.urlBookCover = reader.result?.toString();
      }
    };
    reader.readAsDataURL(this.selectedBookCover);
  }

  // Variables
  id: number;
  subId: string;
  userId: number;
  
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('storyId'));
    this.StoryService.getStory(this.id).subscribe({
      next:data=>{
        console.log(data.data);
        this.inputTitle = data.data.title;
        this.txtAreaDescription = data.data.description;
        this.selectCategory = data.data.categoryId;
        this.selectAudiency = Number(data.data.audience);
        this.urlBookCover = data.data.urlCover;
        this.votes = data.data.votes;
        this.aux = this.urlBookCover;
        this.StoryService.getTags(this.id).subscribe({
          next:data=>{
            console.log(data.data);
            //Funcion para convertir el arreglo de tags en un string
            const tagString = data.data.reduce((accumulator: string, tag: { name: string }) => {
              if (accumulator === '') {
                return tag.name;
              } else {
                return accumulator + ', ' + tag.name;
              }
            }, '');
            console.log(tagString);
            this.inputTags = tagString;
          }
        });
      }
    });
    this.UserService.getUserInfo().subscribe({
      next:data=>{
        console.log(data.data);
        this.subId = data.data.sub;
        this.StoryService.getWriterId(this.subId).subscribe({
          next:data=>{
            this.userId = data;
          }
        })
      }
    })
  }

  saveChangesStory() {
    console.log('Historia actualizada');
    console.log(this.inputTitle);
    console.log(this.txtAreaDescription);
    console.log(this.selectCategory);
    console.log(this.selectAudiency);
    console.log(this.inputTags);
    console.log(this.urlBookCover);
    console.log(this.id);
    if (this.inputTitle.trim().length <= 0 || this.txtAreaDescription.trim().length <= 0 || this.inputTags.trim().length <= 0) {
      alert('Debe llenar todos los campos');
    } else {
      if (this.aux != this.urlBookCover){
        let fileName = this.aux.split('/').pop();
        console.log(fileName);
        if (fileName != null){
          this.awsS3Service.deleteFile(fileName).subscribe({
            next: data => {
              console.log(data);
              this.awsS3Service.uploadFile(this.urlBookCover).subscribe({
                next: data => {
                  console.log(data);
                  console.log('Imagen guardada');
                  this.urlBookCover = data.uploadResult.Location;
                  this.StoryService.editStory(this.id, this.userId, this.selectCategory, this.inputTitle,  this.urlBookCover, this.txtAreaDescription, this.selectAudiency.toString(), new Date(), this.votes, true);
                  const tagsArray = this.inputTags.split(',').map((tag: string) => tag.trim());
                  console.log(tagsArray);
                  this.StoryService.updateTag(this.id, tagsArray);
                  window.location.href = "/mystories";
                },
              });
            }
          });
        }
      } else {
        this.StoryService.editStory(this.id, this.userId, this.selectCategory, this.inputTitle,  this.urlBookCover, this.txtAreaDescription, this.selectAudiency.toString(), new Date(), this.votes, true);
        const tagsArray = this.inputTags.split(',').map((tag: string) => tag.trim());
        console.log(tagsArray);
        this.StoryService.updateTag(this.id, tagsArray);
        window.location.href = "/mystories";
      }
    }
  }
}