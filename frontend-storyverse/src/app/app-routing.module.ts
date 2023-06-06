import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { SettingsComponent } from './components/settings/settings.component';
import { MystoriesComponent } from './components/mystories/mystories.component';
import { NewstoryComponent } from './components/newstory/newstory.component';
import { EditStoryComponent } from './components/edit-story/edit-story.component';
import { NewchapterComponent } from './components/newchapter/newchapter.component';
import { EditChapterComponent } from './components/edit-chapter/edit-chapter.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { ExternalProfileComponent } from './components/external-profile/external-profile.component';
import { ViewstoryComponent } from './components/viewstory/viewstory.component';
import { ViewchapterComponent } from './components/viewchapter/viewchapter.component';
import { LibraryComponent } from './components/library/library.component';
import { AnnouncementComponent } from './components/announcement/announcement.component';
import { FollowingComponent } from './components/following/following.component';
import { FollowersComponent } from './components/followers/followers.component';
import { InvitedComponent } from './components/invited/invited.component';
import { CategoryResultComponent } from './components/category-result/category-result.component';
import { ExternalAnnouncementComponent } from './components/external-announcement/external-announcement.component';
import { ExternalFollowingComponent } from './components/external-following/external-following.component';
import { ExternalFollowersComponent } from './components/external-followers/external-followers.component';
import { SearchResultComponent } from './components/search-result/search-result.component';
import { AuthGuard } from './guard/auth.guard';
import { NotificationComponent } from './components/notification/notification.component';

const routes: Routes = [
  {path: 'home', component: HomeComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'edit-profile', component: EditProfileComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'settings', component: SettingsComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'mystories', component: MystoriesComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'newstory', component: NewstoryComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'edit-story/:storyId', component: EditStoryComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'newchapter/:storyId/:storyTitle', component: NewchapterComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'edit-chapter/:storyId/:chapterId', component: EditChapterComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'categories', component: CategoriesComponent},
  {path: ':user/profile', component: ExternalProfileComponent},
  {path: 'viewstory/:storyId', component: ViewstoryComponent},
  {path: 'viewchapter/:storyId/:chapterId', component: ViewchapterComponent},
  {path: 'library', component: LibraryComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'announcement', component: AnnouncementComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'following', component: FollowingComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: 'followers', component: FollowersComponent, data: {roles: ['USER']}, canActivate: [AuthGuard]},
  {path: '', component: InvitedComponent},
  {path: 'category-result/:categoryId', component: CategoryResultComponent},
  {path: ':user/announcement', component: ExternalAnnouncementComponent},
  {path: ':user/following', component: ExternalFollowingComponent},
  {path: ':user/followers', component: ExternalFollowersComponent},
  {path: 'search/:searchTitle', component: SearchResultComponent},
  {path: 'notificaciones', component: NotificationComponent},
]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
