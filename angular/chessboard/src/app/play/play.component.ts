import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ListemvtService} from "../listemvt.service";
import {InitChessboard} from "./init-chessboard";
import {Move} from "./move";

declare var ChessBoard: any;

declare var Chess: any;

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.scss']
})
export class PlayComponent implements OnInit {

  JOUEUR_NOIR = 'Noir';
  JOUEUR_BLANC = 'Blanc';

  board: any;

  chessEngine: any;

  id: number;

  joueur: string;

  listeAlgorithme: string[];

  algorithmeBlanc: string;
  algorithmeNoir: string;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.board = ChessBoard('board01', {
      position: 'start',
      draggable: true
    });

    this.chessEngine = new Chess();

    this.http.get<string[]>("api/algorithme")
      .subscribe(listeAlgo => {
        console.log("listeAlgo", listeAlgo);
        this.listeAlgorithme = listeAlgo;
        this.algorithmeBlanc=this.listeAlgorithme[0];
        this.algorithmeNoir=this.listeAlgorithme[0];
      });
  }

  init() {
    this.http.post<InitChessboard>("api/init", null)
      .subscribe(initChessboard => {
        console.log("initChessboard", initChessboard);
        this.id = initChessboard.id;
        this.board.position(initChessboard.fen);
        if (initChessboard.joueurCourant == 'B') {
          this.joueur = this.JOUEUR_BLANC;
        } else {
          this.joueur = this.JOUEUR_NOIR;
        }
      });
  }

  next() {
    if (this.id > 0) {

      let algo;
      if (this.joueur == this.JOUEUR_BLANC) {
        algo = this.algorithmeBlanc;
      } else {
        algo = this.algorithmeNoir;
      }

      if (algo && algo.length > 0) {
        this.http.post<Move>("api/nextMove/" + this.id + "/" + algo, null)
          .subscribe(nextMove => {
            console.log("nextMove", nextMove);
            if(nextMove.fenResultat){
              this.board.position(nextMove.fenResultat);
              if (nextMove.joueurCourant == 'B') {
                this.joueur = this.JOUEUR_BLANC;
              } else {
                this.joueur = this.JOUEUR_NOIR;
              }
            }
          });
      }
    }
  }
}
