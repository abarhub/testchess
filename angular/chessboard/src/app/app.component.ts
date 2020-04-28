import {Component, OnInit} from '@angular/core';
import * as $ from 'jquery';

declare var ChessBoard: any;

declare var Chess: any;


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

  chessEngine: any;

  board3: any;

  board3message='';

  whiteSquareGrey = '#a9a9a9';
  blackSquareGrey = '#696969';

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
    this.chessEngine=new Chess();
    this.board3=ChessBoard('board3', {
      position: 'start',
      onDragStart: (source, piece)=> {this.onDragStart(source, piece);},
      onDrop: (source, target)=>{this.onDrop(source, target);},
      onMouseoutSquare: (square, piece)=>{this.onMouseoutSquare(square, piece);},
      onMouseoverSquare: (square, piece) => {this.onMouseoverSquare(square,piece)},
      onSnapEnd: ()=>{this.onSnapEnd();}
    });
    console.info("chessEngine init",this.chessEngine);
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

  //

  removeGreySquares ():void {
    $('#myBoard .square-55d63').css('background', '');
  }

  greySquare (square):void {
    let square2 = $('#myBoard .square-' + square);

    let background = this.whiteSquareGrey;
    if (square2.hasClass('black-3c85d')) {
      background = this.blackSquareGrey;
    }

    square2.css('background', background)
  }

  onDragStart (source, piece):boolean {
    console.log("chessEngine:",this.chessEngine);
    // do not pick up pieces if the game is over
    if (this.chessEngine.game_over()) return false;

    // or if it's not that side's turn
    if ((this.chessEngine.turn() === 'w' && piece.search(/^b/) !== -1) ||
      (this.chessEngine.turn() === 'b' && piece.search(/^w/) !== -1)) {
      return false;
    }
  }

  onDrop (source, target):string {
    this.removeGreySquares();

    // see if the move is legal
    let move = this.chessEngine.move({
      from: source,
      to: target,
      promotion: 'q' // NOTE: always promote to a queen for example simplicity
    });

    // illegal move
    if (move === null) return 'snapback';
  }

  onMouseoverSquare (square, piece):void {
    console.log("chessEngine:",this.chessEngine);
    // get list of possible moves for this square
    let moves = this.chessEngine.moves({
      square: square,
      verbose: true
    })

    console.log("move:",moves);

    // exit if there are no moves available for this square
    if (moves.length === 0) return

    // highlight the square they moused over
    this.greySquare(square);

    // highlight the possible squares for this piece
    for (let i = 0; i < moves.length; i++) {
      this.greySquare(moves[i].to);
    }
  }

  onMouseoutSquare (square, piece):void {
    this.removeGreySquares();
  }

  onSnapEnd ():void {
    this.board3.position(this.chessEngine.fen());
  }

}
