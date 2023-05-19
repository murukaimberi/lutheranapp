import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContributionDetailComponent } from './contribution-detail.component';

describe('Contribution Management Detail Component', () => {
  let comp: ContributionDetailComponent;
  let fixture: ComponentFixture<ContributionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContributionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contribution: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContributionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContributionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contribution on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contribution).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
