const express = require('express');
const cors = require('cors');
const mysql = require('mysql')

const app = express();

const SELECT_ALL_FLOWERS = 'SELECT * FROM flowers'

const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'password',
    database: 'flowers'
})

connection.connect(err => {
    if (err) {
        console.log(err)
        return err;
    }
    console.log('Connected!')
})

console.log(connection)

app.use(cors());

app.get('/', (req, res) => {

})

app.get('/flowers', (req, res) => {
    console.log('test')
    connection.query(SELECT_ALL_FLOWERS, (err, results) => {
        if (err) {
            console.log('test2')
            return res.send(err)
        } else {
            return res.json({
                data: results
            })
        }
    })
}) 

app.get('/flowers/add', (req, res) => {
    const { genus, species, comname} = req.query;
    const INSERT_FLOWER = `INSERT INTO flowers (genus, species, comname) values ('${genus}', '${species}', '${comname}')`;
    connection.query(INSERT_FLOWER, (err, results) => {
        if (err) {
            return err;
        } else {
            console.log("Successfully added product");
        }
    })
    res.send('Adding product');
})

app.listen(4000, () => {
    console.log(`Flowers server listening on port 4000`)
})

