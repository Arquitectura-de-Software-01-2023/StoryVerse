import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarinvitedComponent } from './navbarinvited.component';

describe('NavbarinvitedComponent', () => {
  let component: NavbarinvitedComponent;
  let fixture: ComponentFixture<NavbarinvitedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NavbarinvitedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarinvitedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
