import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BaptismHistoryDetailComponent } from './baptism-history-detail.component';

describe('BaptismHistory Management Detail Component', () => {
  let comp: BaptismHistoryDetailComponent;
  let fixture: ComponentFixture<BaptismHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BaptismHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ baptismHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BaptismHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BaptismHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load baptismHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.baptismHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
