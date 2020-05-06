const express = require('express')
const app = express();

const { Chess } = require('chess.js')
const chess = new Chess()

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

app.listen(8000, () => {
    console.log('Example app listening on port 8000!')
});