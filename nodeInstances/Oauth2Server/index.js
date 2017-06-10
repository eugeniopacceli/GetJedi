var express = require('express'),
    bodyParser = require('body-parser'),
    oauthserver = require('oauth2-server');
 
var app = express();

app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
 
let fAuth = {
  handle: function(request, response) {
    return false;
  }
};

app.oauth = oauthserver({
  model: require('./model.js'),
  grants: ['password'],
  debug: true,
  authenticateHandler : fAuth
});
 
app.all('/oauth/token', app.oauth.grant());
 
app.get('/secret', app.oauth.authorise(), function (req, res) {
  res.send('Secret area');
});

app.get('/home', function (req, res) {
  res.sendFile('authorize.html', { root : __dirname});
});
 
app.use(app.oauth.errorHandler());
 
app.listen(3000);