import { Component } from '@angular/core';
import { UserService } from '../../service/user.service';
import { AwsS3Service } from 'src/app/service/aws-s3.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})

export class EditProfileComponent {

  constructor(private userService: UserService, private awsS3Service: AwsS3Service) { }
  
  // Información del usuario
  preferred_username: string;
  description: string = "";
  email: string;
  birthdate: Date;
  sub: string;
  url_header: string = "";
  url_pfp: string = "";
  aux_url_header: string = "";
  aux_url_pfp: string = "";
  library_private: boolean;
  
  ngOnInit() {
    console.log("Obteniendo información del usuario");
    this.userService.getUserInfo()
    .subscribe({
      next: (data) => {
        console.log(data);
        this.preferred_username = data.data.preferred_username;
        this.description = data.data.description;
        this.email = data.data.email;
        this.birthdate = data.data.birthdate;
        this.library_private = data.data.library_private;
        if (data.data.url_header.trim().length <= 0){
          this.url_header = "../../../assets/profile/header.svg";
        } else {
          this.url_header = data.data.url_header;
        }
        this.aux_url_header = this.url_header;
        if (data.data.url_pfp.trim().length <= 0){
          this.url_pfp = "../../../assets/profile/pfp.svg";
        } else {
          this.url_pfp = data.data.url_pfp;
        }
        this.aux_url_pfp = this.url_pfp;
        console.log(this.preferred_username);
        console.log(this.description);
        console.log(this.url_pfp);
        console.log(this.aux_url_pfp);
        console.log(this.url_header);
        console.log(this.aux_url_header);
        console.log(this.birthdate);
      },
      error: (error) => console.log(error),
      },    
    )
  }

  // Campos para las fotos
  selectedPfp: File;
  selectedHeader: File;
  onFileSelectedPfp(event: any) {
    this.selectedPfp = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.result != null){
        this.url_pfp = reader.result?.toString();
      }
    };
    reader.readAsDataURL(this.selectedPfp);
  }
  onFileSelectedHeader(event: any) {
    this.selectedHeader = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.result != null){
        this.url_header = reader.result?.toString();
      }
    };
    reader.readAsDataURL(this.selectedHeader);
  }

  //Guardar cambios
  save() {   
    if (this.url_pfp == this.aux_url_pfp) {
      if (this.url_header == this.aux_url_header) {
        console.log(this.preferred_username);
        console.log(this.description);
        console.log(this.url_pfp);
        console.log(this.url_header);
        console.log(this.birthdate);
        this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
        .subscribe({
          next: (data) => {
            console.log(data);
            window.location.href = "/profile";
          }
        });
      } else {
        if (this.aux_url_header == "../../../assets/profile/header.svg"){
          this.awsS3Service.uploadFile(this.url_header).subscribe({
            next: (data) => {
              console.log(data);
              this.url_header = data.uploadResult.Location;
              console.log(this.preferred_username);
              console.log(this.description);
              console.log(this.url_pfp);
              console.log(this.url_header);
              console.log(this.birthdate);
              this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
              .subscribe({
                next: (data) => {
                  console.log(data);
                  window.location.href = "/profile";
                }
              });
            },
          });
        } else {
          let fileName = this.aux_url_header.split("/").pop();
          console.log(fileName);
          if (fileName != null) {
            this.awsS3Service.deleteFile(fileName).subscribe({
              next: (data) => {
                console.log(data);
                this.awsS3Service.uploadFile(this.url_header).subscribe({
                  next: (data) => {
                    console.log(data);
                    this.url_header = data.uploadResult.Location;
                    console.log(this.preferred_username);
                    console.log(this.description);
                    console.log(this.url_pfp);
                    console.log(this.url_header);
                    console.log(this.birthdate);
                    this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                    .subscribe({
                      next: (data) => {
                        console.log(data);
                        window.location.href = "/profile";
                      }
                    });
                  },
                });
              },
            });
          }
        }
      }
    } else {
      if (this.aux_url_pfp == "../../../assets/profile/pfp.svg") {
        this.awsS3Service.uploadFile(this.url_pfp).subscribe({
          next: (data) => {
            console.log(data);
            this.url_pfp = data.uploadResult.Location;
            if (this.url_header == this.aux_url_header) {
              console.log(this.preferred_username);
              console.log(this.description);
              console.log(this.url_pfp);
              console.log(this.url_header);
              console.log(this.birthdate);
              this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
              .subscribe({
                next: (data) => {
                  console.log(data);
                  window.location.href = "/profile";
                }
              });
            } else {
              if (this.aux_url_header == "../../../assets/profile/header.svg"){
                this.awsS3Service.uploadFile(this.url_header).subscribe({
                  next: (data) => {
                    console.log(data);
                    this.url_header = data.uploadResult.Location;
                    console.log(this.preferred_username);
                    console.log(this.description);
                    console.log(this.url_pfp);
                    console.log(this.url_header);
                    console.log(this.birthdate);
                    this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                    .subscribe({
                      next: (data) => {
                        console.log(data);
                        window.location.href = "/profile";
                      }
                    });

                  },
                });
              } else {
                let fileName = this.aux_url_header.split("/").pop();
                console.log(fileName);
                if (fileName != null) {
                  this.awsS3Service.deleteFile(fileName).subscribe({
                    next: (data) => {
                      console.log(data);
                      this.awsS3Service.uploadFile(this.url_header).subscribe({
                        next: (data) => {
                          console.log(data);
                          this.url_header = data.uploadResult.Location;
                          console.log(this.preferred_username);
                          console.log(this.description);
                          console.log(this.url_pfp);
                          console.log(this.url_header);
                          console.log(this.birthdate);
                          this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                          .subscribe({
                            next: (data) => {
                              console.log(data);
                              window.location.href = "/profile";
                            }
                          });
                        },
                      });
                    },
                  });
                }
              }
            }
          },
        });
      } else {
        let fileName = this.aux_url_pfp.split("/").pop();
        console.log(fileName);
        if (fileName != null) {
          this.awsS3Service.deleteFile(fileName).subscribe({
            next: (data) => {
              console.log(data);
              this.awsS3Service.uploadFile(this.url_pfp).subscribe({
                next: (data) => {
                  console.log(data);
                  this.url_pfp = data.uploadResult.Location;
                  if (this.url_header == this.aux_url_header) {
                    console.log(this.preferred_username);
                    console.log(this.description);
                    console.log(this.url_pfp);
                    console.log(this.url_header);
                    console.log(this.birthdate);
                    this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                    .subscribe({
                      next: (data) => {
                        console.log(data);
                        window.location.href = "/profile";
                      }
                    });
                  } else {
                    if (this.aux_url_header == "../../../assets/profile/header.svg"){
                      this.awsS3Service.uploadFile(this.url_header).subscribe({
                        next: (data) => {
                          console.log(data);
                          this.url_header = data.uploadResult.Location;
                          console.log(this.preferred_username);
                          console.log(this.description);
                          console.log(this.url_pfp);
                          console.log(this.url_header);
                          console.log(this.birthdate);
                          this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                          .subscribe({
                            next: (data) => {
                              console.log(data);
                              window.location.href = "/profile";
                            }
                          });
                        },
                      });
                    } else {
                      let fileName = this.aux_url_header.split("/").pop();
                      console.log(fileName);
                      if (fileName != null) {
                        this.awsS3Service.deleteFile(fileName).subscribe({
                          next: (data) => {
                            console.log(data);
                            this.awsS3Service.uploadFile(this.url_header).subscribe({
                              next: (data) => {
                                console.log(data);
                                this.url_header = data.uploadResult.Location;
                                console.log(this.preferred_username);
                                console.log(this.description);
                                console.log(this.url_pfp);
                                console.log(this.url_header);
                                console.log(this.birthdate);
                                this.userService.editProfile(this.email, this.description, this.url_header, this.url_pfp, this.birthdate, this.library_private)
                                .subscribe({
                                  next: (data) => {
                                    console.log(data);
                                    window.location.href = "/profile";
                                  }
                                });
                              },
                            });
                          },
                        });
                      }
                    }
                  }
                },
              });
            },
          });
        }
      }
    }
  }
}
