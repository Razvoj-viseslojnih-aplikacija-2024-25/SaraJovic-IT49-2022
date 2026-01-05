import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-about-component',
  imports: [MatIconModule, MatCardModule],
  templateUrl: './about-component.html',
  styleUrl: './about-component.css'
})
export class AboutComponent {

}