import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-author-component',
  standalone: true,
  imports: [MatIconModule, MatCardModule],
  templateUrl: './author-component.html',
  styleUrl: './author-component.css'
})
export class AuthorComponent {

}