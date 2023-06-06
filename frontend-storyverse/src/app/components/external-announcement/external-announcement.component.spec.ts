import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalAnnouncementComponent } from './external-announcement.component';

describe('ExternalAnnouncementComponent', () => {
  let component: ExternalAnnouncementComponent;
  let fixture: ComponentFixture<ExternalAnnouncementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalAnnouncementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExternalAnnouncementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
