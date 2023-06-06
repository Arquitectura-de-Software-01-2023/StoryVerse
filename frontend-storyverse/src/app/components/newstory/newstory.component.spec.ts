import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewstoryComponent } from './newstory.component';

describe('NewstoryComponent', () => {
  let component: NewstoryComponent;
  let fixture: ComponentFixture<NewstoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewstoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewstoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
