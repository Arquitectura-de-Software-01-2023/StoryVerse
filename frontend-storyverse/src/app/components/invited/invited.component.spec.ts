import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvitedComponent } from './invited.component';

describe('InvitedComponent', () => {
  let component: InvitedComponent;
  let fixture: ComponentFixture<InvitedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvitedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvitedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
