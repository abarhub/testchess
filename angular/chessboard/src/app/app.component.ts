import {Component, OnInit} from '@angular/core';

declare var ChessBoard: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'chessboard';
  board: any;

  board2: any;

  board2Fen='';

  ngOnInit(): void {
    this.board=ChessBoard('board1', {
      position: 'start',
      draggable: true
    });
    this.board2=ChessBoard('board2', {
      position: 'start',
      draggable: true,
      dropOffBoard: 'trash',
      sparePieces: true
    });
  }

  start():void {
    this.board2.start();
  }

  clear():void {
    this.board2.clear();
  }

  showFen():void {
    this.board2Fen=this.board2.fen();
  }
}
