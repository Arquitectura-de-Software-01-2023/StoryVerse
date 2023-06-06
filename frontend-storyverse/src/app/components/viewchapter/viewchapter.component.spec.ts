import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewchapterComponent } from './viewchapter.component';

describe('ViewchapterComponent', () => {
  let component: ViewchapterComponent;
  let fixture: ComponentFixture<ViewchapterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewchapterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewchapterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
