import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as $ from 'jquery';
import {HttpClient} from "@angular/common/http";
import {ChessbordService} from "./chessbord-service";
import {ListemvtService} from "./listemvt.service";

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

  perftDepth: number=4;

  // @ViewChild('monElementHTML') monElement:ElementRef;

  constructor(private http: HttpClient) { }

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


  removeGreySquares ():void {
    $('#board3 .square-55d63').css('background', '');
  }

  greySquare (square):void {
    let square2 = $('#board3 .square-' + square);

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
    let choix=true;
    choix=false;
    if(choix){
      this.onMouseoverSquare1(square,piece);
    } else {
      this.onMouseoverSquare2(square,piece);
    }
  }

  onMouseoverSquare1 (square, piece):void {
    console.log("onMouseoverSquare1:",square);
    console.log("chessEngine:",this.chessEngine);
    console.log("square:",square);
    // get list of possible moves for this square
    let moves = this.chessEngine.moves({
      square: square,
      verbose: true
    })

    this.getListeMouve(square);

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

  onMouseoverSquare2 (square, piece):void {
    console.log("onMouseoverSquare2:",square);
    console.log("chessEngine:",this.chessEngine);
    console.log("square:",square);

    let chess=new ChessbordService();
    chess.mouvement=square;
    chess.fen=this.chessEngine.fen();
    console.info("getListeMouve square=", square);
    this.http.post<ListemvtService>("api/mvt",chess)
      .subscribe(listeMvt => {
        console.log("listeMvt",listeMvt);

        let liste=[];
        if(listeMvt && listeMvt.deplacements){
          liste=listeMvt.deplacements;
        }

        let moves = this.chessEngine.moves({
          square: square,
          verbose: true
        });

        let liste2=[];
        if(moves &&moves.length>0){
          for (let i = 0; i < moves.length; i++) {
            liste2.push(moves[i].to);
          }
        }

        console.log("liste=",liste, "liste2=",liste2);

        let listePlus=this.diff(liste,liste2);
        let listeMoins=this.diff(liste2,liste);

        if(listePlus.size==0&&listeMoins.size==0){
          console.log("pas de difference");
        } else {
          console.error("listePlus=",listePlus, "listeMoins=",listeMoins);
          this.board3message+="listePlus="+this.affiche(listePlus)+
            "listeMoins="+this.affiche(listeMoins)+".\n";
        }

        if(liste.length>0){
          this.greySquare(square);

          // highlight the possible squares for this piece
          for (let i = 0; i < liste.length; i++) {
            this.greySquare(liste[i]);
          }
        }
      });

    // // get list of possible moves for this square
    // let moves = this.chessEngine.moves({
    //   square: square,
    //   verbose: true
    // })
    //
    // this.getListeMouve(square);
    //
    // console.log("move:",moves);
    //
    // // exit if there are no moves available for this square
    // if (moves.length === 0) return
    //
    // // highlight the square they moused over
    // this.greySquare(square);
    //
    // // highlight the possible squares for this piece
    // for (let i = 0; i < moves.length; i++) {
    //   this.greySquare(moves[i].to);
    // }
  }

  onMouseoutSquare (square, piece):void {
    this.removeGreySquares();
  }

  onSnapEnd ():void {
    this.board3.position(this.chessEngine.fen());
  }

  copieBord():void {
    let fen=this.board2.fen();
    console.log("fen=",fen);
    this.board3.position(fen);
    this.chessEngine.load(fen+' w - - 0 1');
  }

  test1():void {
    this.http.get("api/greeting", {responseType: 'text'})
      .subscribe((data: string) => console.log("res",data));
    // this.http.get("api/greeting")
    //   .subscribe((data: string) => console.log("res",data));
  }

  test2():void {
    this.http.get("api/mvt")
      .subscribe((data: string) => console.log("res",data));
  }

  private getListeMouve(square: string) {
    let chess=new ChessbordService();
    chess.mouvement=square;
    chess.fen=this.chessEngine.fen();
    console.info("getListeMouve square=", square);
    this.http.post<ListemvtService>("api/mvt",chess)
      .subscribe(listeMvt => console.log("listeMvt",listeMvt));
  }

  private diff(liste:string[],liste2:string[]):Set<string> {
    let listePlus=new Set<string>();
    for(let i=0;i<liste.length;i++){
      listePlus.add(liste[i]);
    }
    for(let i=0;i<liste2.length;i++){
      listePlus.delete(liste2[i]);
    }
    return listePlus;
  }

  private affiche(set:Set<string>):string {
    let res='';
    for (let s of set) {
      if(res.length>0){
        res+=',';
      }
      res+=s;
    }
    return '['+res+']';
  }

  calculPerft() {
    const depth=this.perftDepth;
    console.log("perft("+depth+") ...");
    let localchessEngine=new Chess();
    let fen=this.board2.fen();
    console.log("fen=",fen);
    localchessEngine.load(fen+' w - - 0 1');
    const debut=new Date().getTime();
    let res=this.calculPerftValue(localchessEngine,depth);
    const fin=new Date().getTime();
    console.log("perft("+depth+")",res, " (duree:",fin-debut,")");
    this.board3message="perft("+depth+") : "+res+" (duree:"+(fin-debut)+")";
  }

  private calculPerftValue(localChessEngine: any, depth: number):number {
    let count=0;

    if (depth <= 0) return 1;

    let moves = localChessEngine.moves()
    for (let i = 0; i < moves.length; i++) {
      //console.log("calcul depth:", depth, "i=", i);
      const move = moves[i];
      localChessEngine.move(move);
      count += this.calculPerftValue(localChessEngine, depth - 1);
      localChessEngine.undo();
    }
    return count;
  }
}
