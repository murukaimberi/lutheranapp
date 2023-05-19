import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'congregant',
        data: { pageTitle: 'lutheranappApp.congregant.home.title' },
        loadChildren: () => import('./congregant/congregant.module').then(m => m.CongregantModule),
      },
      {
        path: 'post',
        data: { pageTitle: 'lutheranappApp.post.home.title' },
        loadChildren: () => import('./post/post.module').then(m => m.PostModule),
      },
      {
        path: 'baptism-history',
        data: { pageTitle: 'lutheranappApp.baptismHistory.home.title' },
        loadChildren: () => import('./baptism-history/baptism-history.module').then(m => m.BaptismHistoryModule),
      },
      {
        path: 'marriage-history',
        data: { pageTitle: 'lutheranappApp.marriageHistory.home.title' },
        loadChildren: () => import('./marriage-history/marriage-history.module').then(m => m.MarriageHistoryModule),
      },
      {
        path: 'dependant',
        data: { pageTitle: 'lutheranappApp.dependant.home.title' },
        loadChildren: () => import('./dependant/dependant.module').then(m => m.DependantModule),
      },
      {
        path: 'league',
        data: { pageTitle: 'lutheranappApp.league.home.title' },
        loadChildren: () => import('./league/league.module').then(m => m.LeagueModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'lutheranappApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'lutheranappApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'lutheranappApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'contribution',
        data: { pageTitle: 'lutheranappApp.contribution.home.title' },
        loadChildren: () => import('./contribution/contribution.module').then(m => m.ContributionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
