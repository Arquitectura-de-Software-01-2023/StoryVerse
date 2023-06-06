import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryResultComponent } from './category-result.component';

describe('CategoryResultComponent', () => {
  let component: CategoryResultComponent;
  let fixture: ComponentFixture<CategoryResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CategoryResultComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoryResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
