const express = require('express')
const app = express();

const { Chess } = require('chess.js')
const chess = new Chess();

// Parse URL-encoded bodies (as sent by HTML forms)
app.use(express.urlencoded());

// Parse JSON bodies (as sent by API clients)
app.use(express.json());

//app.use(express.json());

app.get('/', (req, res) => {
    res.send('Hello World!')
});

app.get('/test1', function(req, res) {
    res.setHeader('Content-Type', 'text/plain');
    res.send('Vous êtes à l\'accueil, que puis-je pour vous ?');
});

app.get('/sous-sol', function(req, res) {
    res.setHeader('Content-Type', 'text/plain');
    res.send('Vous êtes dans la cave à vins, ces bouteilles sont à moi !');
});

app.get('/etage/1/chambre', function(req, res) {
    res.setHeader('Content-Type', 'text/plain');
    res.send('Hé ho, c\'est privé ici !');
});

app.get('/chess1', function(req, res) {
    console.log('body', req.body);

    const moves = chess.moves({ verbose: true });
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(moves, null, 4));
});

app.post('/chess2', function(req, res) {
    console.log('body', req.body);

    if(req.body &&req.body.fen){
        chess.load(req.body.fen);
    }

    const moves = chess.moves({ verbose: true });
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(moves, null, 4));
});

function calculPerft(myChess, depth){

    let nodes=0;

    if(depth<=0){
        return 1;
    }

    const moves = myChess.moves();
    for(let i=0;i<moves.length;i++){
        myChess.move(moves[i]);
        nodes+=calculPerft(myChess, depth-1);
        myChess.undo();
    }

    return nodes;
}

app.post('/chess3', function(req, res) {
    console.log('body', req.body);

    let mychess= new Chess();

    let depth=1;

    if(req.body &&req.body.fen){
        if(req.body.fen){
            mychess.load(req.body.fen);
        }
        if(req.body.nb && req.body.nb>0){
            depth=req.body.nb;
        }
    }

    console.log("calcul Perft:", depth);

    const nodes = calculPerft(mychess,depth);

    console.log("depth:", depth, "Perft", nodes);

    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(nodes, null, 4));
});

app.listen(8000, () => {
    console.log('Example app listening on port 8000!')
});