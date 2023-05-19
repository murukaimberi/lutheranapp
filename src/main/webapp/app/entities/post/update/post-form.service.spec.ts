import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../post.test-samples';

import { PostFormService } from './post-form.service';

describe('Post Form Service', () => {
  let service: PostFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostFormService);
  });

  describe('Service methods', () => {
    describe('createPostFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPostFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            description: expect.any(Object),
            postedDate: expect.any(Object),
            image: expect.any(Object),
            congregant: expect.any(Object),
          })
        );
      });

      it('passing IPost should create a new form with FormGroup', () => {
        const formGroup = service.createPostFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            description: expect.any(Object),
            postedDate: expect.any(Object),
            image: expect.any(Object),
            congregant: expect.any(Object),
          })
        );
      });
    });

    describe('getPost', () => {
      it('should return NewPost for default Post initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPostFormGroup(sampleWithNewData);

        const post = service.getPost(formGroup) as any;

        expect(post).toMatchObject(sampleWithNewData);
      });

      it('should return NewPost for empty Post initial value', () => {
        const formGroup = service.createPostFormGroup();

        const post = service.getPost(formGroup) as any;

        expect(post).toMatchObject({});
      });

      it('should return IPost', () => {
        const formGroup = service.createPostFormGroup(sampleWithRequiredData);

        const post = service.getPost(formGroup) as any;

        expect(post).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPost should not enable id FormControl', () => {
        const formGroup = service.createPostFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPost should disable id FormControl', () => {
        const formGroup = service.createPostFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
