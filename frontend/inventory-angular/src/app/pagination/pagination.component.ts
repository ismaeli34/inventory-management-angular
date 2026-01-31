import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-pagination',
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent implements OnInit{

  @Input() currentPage: number =1;

  @Input() totalPages: number = 1;

  @Output() pageChange = new EventEmitter<number>();


  // method to generate page numbers
  get pageNumbers(){
    return Array.from({length: this.totalPages}, (_,i)=> i+1)
  }

  // method to handle page Change

  onPageChange(page:number): void{
    if (page >= 1 && page <= this.totalPages){
      this.pageChange.emit(page)
    }
  }




  ngOnInit(): void {
    }

}
