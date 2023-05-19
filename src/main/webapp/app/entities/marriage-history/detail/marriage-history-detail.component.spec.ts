import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarriageHistoryDetailComponent } from './marriage-history-detail.component';

describe('MarriageHistory Management Detail Component', () => {
  let comp: MarriageHistoryDetailComponent;
  let fixture: ComponentFixture<MarriageHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MarriageHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ marriageHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MarriageHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MarriageHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load marriageHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.marriageHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
