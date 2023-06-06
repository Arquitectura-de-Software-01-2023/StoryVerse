import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalProfileComponent } from './external-profile.component';

describe('ExternalProfileComponent', () => {
  let component: ExternalProfileComponent;
  let fixture: ComponentFixture<ExternalProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExternalProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
