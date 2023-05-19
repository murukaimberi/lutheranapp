import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DependantDetailComponent } from './dependant-detail.component';

describe('Dependant Management Detail Component', () => {
  let comp: DependantDetailComponent;
  let fixture: ComponentFixture<DependantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DependantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dependant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DependantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DependantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dependant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dependant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
