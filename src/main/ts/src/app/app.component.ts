import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterOutlet } from '@angular/router';
import { EnergyGridService } from './energy-grid-service.';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterOutlet, ReactiveFormsModule, FormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatCardModule, FlexLayoutModule, MatCardModule, MatSlideToggleModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  data: any;
  readingsEnabled: any;

  constructor(private energyGridService: EnergyGridService) {
  }

  ngOnInit(): void {
    this.fetchEnergyGrid();
  }

  fetchEnergyGrid() {
    this.energyGridService.getGrid().subscribe((data: any) => {
      this.data = data;
    });
  }

  saveEnergyGrid() {
    this.energyGridService.updateGrid(this.data).subscribe(() => {
      console.log('Energy Grid updated successfully!');
      alert('Zapisano');
    });
  }

  toggleReadings() {
    this.data.reportingEnabled = !this.data.reportingEnabled;
  }
}
