import { Routes } from '@angular/router';
import { SudComponent } from './components/main/sud-component/sud-component';
import { PredmetComponent } from './components/main/predmet-component/predmet-component';
import { HomeComponent } from './components/utility/home-component/home-component';
import { AboutComponent } from './components/utility/about-component/about-component';
import { AuthorComponent } from './components/utility/author-component/author-component';

export const routes: Routes = [
    { path: 'sudovi', component: SudComponent },
    { path: 'predmeti', component: PredmetComponent },
    { path: 'home', component:HomeComponent},
    { path: 'about', component:AboutComponent},
    { path: 'author', component:AuthorComponent},
    { path: '', component: HomeComponent, pathMatch: 'full' },
];