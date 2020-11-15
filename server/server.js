// JavaScript source code
const http = require('http');
const express = require('express');
const bodyParser = require('body-parser');
const MongoClient = require('mongodb').MongoClient;
const bcrypt = require('bcrypt');
const url = "mongodb+srv://User2:testpass1234@a-mad-cluster.1u5jl.mongodb.net/API?retryWrites=true&w=majority";
const jwt = require('jsonwebtoken');

const salt = bcrypt.genSaltSync(10);
const app = express();
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }));

//Status encoded
const OK = 200;
const BAD_REQUEST = 400;
const UNAUTHORIZED = 401;
const CONFLICT = 403;
const NOT_FOUND = 404;
const INTERNAL_SERVER_ERROR = 500;

var authMiddleware = function (req, res, next) {
    try {
        if (!req.headers.authorizationkey) {
            res.status(UNAUTHORIZED).send("UNAUTHORIZED");
        } else {
            var decode = jwt.decode(req.headers.authorizationkey);
            jwt.verify(req.headers.authorizationkey, 'secret', function (err, decoded) {
                if (err) {
                    console.log(err);
                    res.status(BAD_REQUEST).send(err.message);
                } else {
                    if (decoded.u_id == decode.u_id) {
                        req.encode = decoded.u_id;
                        next();
                    } else
                        console.log("fail");
                }
            });
        }
    } catch (err) {
        res.send(err);
    }
}

// user signup encyption check
app.post('/v1/user/signup', function (req, res) {

    if (typeof req.body.email === "undefined" || typeof req.body.password === "undefined") {
        res.status(BAD_REQUEST).send("Bad request Check request Body");
    } else {
        //console.log("Before Client")
        client = new MongoClient(url, {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });
        const hash = bcrypt.hashSync(req.body.password, salt);
        //console.log("Before Connect "+hash)
        client.connect().then(() => {
            var myObj = {
                emailId: req.body.email,
                password: hash
            };
            //console.log("Before Insert")
            client.db('DataManagement').collection('Users').insertOne(myObj, function (err, result) {
                if (err)
                    res.status(INTERNAL_SERVER_ERROR).send(err);
                else if (result.insertedCount == 1) {
                    res.status(OK).send("Signed up Successfully");
                    console.log(result.insertedId);
                }
                return client.close();
            })
        });
    }
});

// user login with password encyption check
app.post('/v1/user/login', function (req, res) {
    if (typeof req.body.email === "undefined" || typeof req.body.password === "undefined") {
        res.status(BAD_REQUEST).send("Bad request Check request Body");
    } else {
        //console.log("Before Client")
        client = new MongoClient(url, {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });
        //console.log("Before Connect")
        client.connect().then(() => {
            var myObj = {
                emailId: req.body.email,
            };
            //console.log("Before Retrieval")
            client.db('DataManagement').collection('Users').findOne(myObj, function (err, result) {
                if (err)
                    res.status(INTERNAL_SERVER_ERROR).send(err);
                else if (result == null)
                    res.status(OK).send("No such user found");
                else if (result != null && bcrypt.compareSync(req.body.password, result.password)) {
                    var token = jwt.sign({
                        u_id: result._id
                    }, 'secret', {
                        expiresIn: 60 * 60
                    });
                    res.header("AuthorizationKey", token).status(OK).send("Login Successful");
                }
                else {
                    res.status(OK).send("Invalid Credentials");
                }
                return client.close();
            })
        });
    }
});

//get all enrolled users
app.get('/v1/actors', authMiddleware, function (req, res) {
    if (typeof req.body.skips === "undefined" || typeof req.body.limit === "undefined" ||
        typeof req.body.filters === "undefined" || typeof req.body.sortMethod === "undefined") {
        res.status(BAD_REQUEST).send("Bad request Check request Body");
    } else {

        // console.log("skips: ", req.body.skips,
        //             "\nlimit: ", req.body.limit,
        //             "\nfilters: ", req.body.filters,
        //             "\nsortAttribute: ", req.body.sortMethod);

        client = new MongoClient(url, {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });
        client.connect().then(() => {
            var myObj = req.body.filters;
            console.log("myObj: ", myObj);

            var mySort = req.body.sortMethod;

            client.db('DataManagement').collection('Movies').find(myObj).sort(mySort).skip(parseInt(req.body.skips)).limit(parseInt(req.body.limit)).toArray(function (err, result) {
                if (err)
                    res.status(INTERNAL_SERVER_ERROR).send(err);
                else if (result == null)
                    res.status(OK).send("No Actors found");
                else if (result != null) {
                    res.status(OK).send(result);
                }
                return client.close();
            })
        });
    }
});

const port = 3000;
http.createServer(app).listen(port, () => {
    console.log('Express server listening on port ' + port);
});