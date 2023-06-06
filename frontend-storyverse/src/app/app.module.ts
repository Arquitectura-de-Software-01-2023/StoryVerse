import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { initializeKeycloak } from './init/keycloak-init.factory';
import { APP_INITIALIZER } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SettingsComponent } from './components/settings/settings.component';
import { MystoriesComponent } from './components/mystories/mystories.component';
import { NewstoryComponent } from './components/newstory/newstory.component';
import { NewchapterComponent } from './components/newchapter/newchapter.component';
import { EditChapterComponent } from './components/edit-chapter/edit-chapter.component';
import { EditStoryComponent } from './components/edit-story/edit-story.component';
import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { HttpClientModule } from '@angular/common/http';
import { CategoriesComponent } from './components/categories/categories.component';
import { ExternalProfileComponent } from './components/external-profile/external-profile.component';
import { ViewstoryComponent } from './components/viewstory/viewstory.component';
import { ViewchapterComponent } from './components/viewchapter/viewchapter.component';
import { LibraryComponent } from './components/library/library.component';
import { AnnouncementComponent } from './components/announcement/announcement.component';
import { FollowingComponent } from './components/following/following.component';
import { FollowersComponent } from './components/followers/followers.component';
import { InvitedComponent } from './components/invited/invited.component';
import { NavbarinvitedComponent } from './components/navbarinvited/navbarinvited.component';
import { CategoryResultComponent } from './components/category-result/category-result.component';
import { ExternalAnnouncementComponent } from './components/external-announcement/external-announcement.component';
import { ExternalFollowingComponent } from './components/external-following/external-following.component';
import { ExternalFollowersComponent } from './components/external-followers/external-followers.component';
import { SearchResultComponent } from './components/search-result/search-result.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatPaginatorModule} from '@angular/material/paginator';
import { NotificationComponent } from './components/notification/notification.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    ProfileComponent,
    EditProfileComponent,
    SettingsComponent,
    MystoriesComponent,
    NewstoryComponent,
    NewchapterComponent,
    EditChapterComponent,
    EditStoryComponent,
    CategoriesComponent,
    ExternalProfileComponent,
    ViewstoryComponent,
    ViewchapterComponent,
    LibraryComponent,
    AnnouncementComponent,
    FollowingComponent,
    FollowersComponent,
    InvitedComponent,
    NavbarinvitedComponent,
    CategoryResultComponent,
    ExternalAnnouncementComponent,
    ExternalFollowingComponent,
    ExternalFollowersComponent,
    SearchResultComponent,
    NotificationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, // <-- Importa el módulo FormsModule
    ReactiveFormsModule, // <-- Importa el módulo ReactiveFormsModule
    KeycloakAngularModule, // <-- Importa el módulo KeycloakAngularModule
    HttpClientModule, BrowserAnimationsModule, // <-- Importa el módulo HttpClientModule
    MatPaginatorModule
  ],
  providers: [
   {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
