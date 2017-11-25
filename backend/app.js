try {
    var Spooky = require('spooky');
} catch (e) {
    var Spooky = require('../lib/spooky');
}

var express = require ('express');
var bodyParser = require('body-parser');

var app = express();

app.set('view engine', 'pug');
app.set('views','./views');

app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: true }));

var url_lms = "http://lms.kau.ac.kr"
// var url = "http://127.0.0.1:3000/lms_before_arr.html"

var result;
var result_login;
var temp;
var temp_re = new Array();
var temp_result = new Array();
var temp_result_1 = new Array();

app.get("/test_post", function(req, res){
  console.log("@" + req.method + " " + req.url);
  res.render('test_post')
})

app.post("/login", function(req,res){
  console.log("@" + req.method + " " + req.url);

  var spooky = new Spooky({
    child: {
      transport: 'http',
      command : 'casperjs.cmd'
    },
    casper: {
      logLevel: 'debug',
      verbose: true}
    },
    function(err){
      if(err) {
        e = new Error('Failed to initialize SpookyJS');
        e.details = err;
        throw e;
      }

      spooky.start(url_lms);

      spooky.then(function(){
        this.click('#btn_sso_login')
      })
      // spooky.then(function(){
      //   this.capture('login.png')
      // })

      var id_req = req.body.studentNum;
      var pwd_req = req.body.password;

      spooky.then([{id_then : id_req , pwd_then : pwd_req}, function(){
        this.fill ('form[name=LoginForm]', {'p_id' : id_then, 'p_pwd' : pwd_then}, true);
      }])

      spooky.then(function(){
        this.wait(500,function(){
          if(this.exists('header, div.container-fluid .row-fluid .login-header .loggedin-user, ul.nav.pull-left, li.navbar-text, div.logininfo')){
            result_login = "1"
          }
          else{
            result_login = "0"
          }
          this.emit('result_login' ,result_login)
        })
      })

      spooky.run()
    })

    spooky.on('result_login', function(result_login){

      result_login = {"result" : result_login}

      console.log(result_login);
      res.send(result_login);
    })
})


app.post("/lms/data", function(req,res){
  console.log("@" + req.method + " " + req.url);

  var spooky = new Spooky({
    child: {
      transport: 'http',
      command : 'casperjs.cmd'
    },
    casper: {
      logLevel: 'debug',
      verbose: true}
    },
    function(err){
      if(err){
        e = new Error('Failed to initialize SpookyJS');
        e.details = err;
        throw e;
      }

    spooky.start(url_lms);

    spooky.then(function(){
      this.click('#btn_sso_login')
    })

    // spooky.then(function(){
    //   this.capture('login.png')
    // })

    var id_req = req.body.studentNum;
    var pwd_req = req.body.password;

    spooky.then([{id_then : id_req , pwd_then : pwd_req}, function(){
      this.fill ('form[name=LoginForm]', {'p_id' : id_then, 'p_pwd' : pwd_then}, true);
    }])

    spooky.then(function(){
      this.wait(500, function(){
        if(this.exists('span.postsincelogin')){
          temp = this.getElementsInfo('h3, div.overview.forum .name').map(function(info) {return info.text.trim('\n')})
          var date = new Date();
          // this.capture(String (date.getYear()) + String (date.getMonth()+1) + String (date.getDate()) + ".png")
          temp = String(temp).replace(/:/g,',')
          temp = temp.split(',')
          temp = String(temp)
          result = temp
          this.emit('result', result);
        }
        else{
          result = ""
          this.emit('result', result);
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


  spooky.on('result', function(result){

    var k = 0;
  //  var objJSON = new Object();
    var objJSONAArr = new Array();

    if(result != ""){
      temp_re = result.split(',')
      for(var i = 0; i < temp_re.length; i++) {
        if(temp_re[i] == '포럼') {
          temp_result[k] = temp_re[i-1] + ',' + temp_re[i+1]
          k++
          result = temp_result[k]
        }
      }

      for (var j = 0; j < temp_result.length; j++) {
        var re = temp_result[j]
        result = re.split(',')

        var Subject = result[0]
        var Content = result[1]
        var date = new Date();
        var Time = String(date.getMonth()+1) + '월 ' + String(date.getDate()) + '일 ' + String(date.getHours()) + '시 ' + String(date.getMinutes()) + '분'

        temp_result_1[j] = {"time" : Time , "subject" : Subject , "content" : Content}

      }
      for(var i = 0; i < temp_result_1.length; i++) {
        objJSONAArr.push(temp_result_1[i]);
      }

      result = objJSONAArr
  }
  else{
    var jsonArr = new Array();
    result = jsonArr;
  }

    console.log(result);
    res.send(result);
  })

})

app.listen(8000, function (){
  console.log ('Connected 8000 port!!!');
});
