var express = require('express');
var bodyParser = require('body-parser');

var app = express();

var temp;
var temp_r;
var temp_s;

var result_1;
var result_2;

app.set('view engine', 'pug');
app.set('views','./views');

app.use(express.static('public'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.post('/test_post', function(req, res){

})






app.listen(3000, function (){
  console.log ('Connected 3000 port!!!');
});


function call_spooky(){
  try{
    var Spooky = require('spooky');
  } catch (e) {
    var Spooky = require('../lib/spooky');
  }

  var url = "http://lms.kau.ac.kr";
  var url_test = "http://127.0.0.1:3000/lms_before.html"


  var spooky = new Spooky( {
    child: {
      transport : 'http' ,
      command : 'casperjs.cmd'
    },
    casper : {
      logLevel : 'debug',
      verbose : true
    }
  }, function (err) {

    if (err) {
      e = new Error('Failed to initialize SpookyJS');
      e.details = err;
      throw e;
    }

    spooky.start(url_test);
/*
    spooky.then(function(){
      this.click('#btn_sso_login')
    })
*/
    spooky.then(function(){
      this.capture('login.png')
    })
/*
    spooky.then(function(){
      var id = in_id;
      var password = in_pwd;
      this.fill ('form[name=LoginForm]', {'p_id' : id, 'p_pwd' : password}, true);
    })
*/
    spooky.then(function(){
      this.wait(5000, function(){
        this.capture('test.png');
        if (this.exists('span.postsincelogin')) {
          temp = this.getElementsInfo('h3, div.overview.forum .name').map(function(info) {return info.text.trim('\n')})
          var date = new Date();
          this.capture(String (date.getYear()) + String (date.getMonth()+1) + String (date.getDate()) + ".png")

          temp_s = String(temp)
          var temp_c = temp_s.replace(/:/g , ',')

          var temp_a = temp_c.split(',')

          for(var i = 0; i < temp_a.length; i++){
            if(temp_a[i] == '포럼'){
              temp_r =  temp_a[i+1] + ',' + temp_a[i+2]
            }
          }
        }
      })
    })

    spooky.run()

  })

  spooky.on('error', function (e, stack) {
      console.error(e);
      if (stack) {
          console.log(stack);
      }
  })

  return temp_r
}
