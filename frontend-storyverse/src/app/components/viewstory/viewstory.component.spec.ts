import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewstoryComponent } from './viewstory.component';

describe('ViewstoryComponent', () => {
  let component: ViewstoryComponent;
  let fixture: ComponentFixture<ViewstoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewstoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewstoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
