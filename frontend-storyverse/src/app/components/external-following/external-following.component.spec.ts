import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalFollowingComponent } from './external-following.component';

describe('ExternalFollowingComponent', () => {
  let component: ExternalFollowingComponent;
  let fixture: ComponentFixture<ExternalFollowingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalFollowingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExternalFollowingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
