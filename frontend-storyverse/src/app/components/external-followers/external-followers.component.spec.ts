import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalFollowersComponent } from './external-followers.component';

describe('ExternalFollowersComponent', () => {
  let component: ExternalFollowersComponent;
  let fixture: ComponentFixture<ExternalFollowersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalFollowersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExternalFollowersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
