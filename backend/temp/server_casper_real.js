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

var url = "http://127.0.0.1:3000/lms_before.html"

var result;
var i;
var temp;
var temp_re = new Array();
var temp_result = new Array();
var temp_result_1 = new Array();

app.get("/test_post", function(req, res){
  res.render('test_post')
})


app.post("/form_reciver", function(req,res){
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

    spooky.start(url);

    spooky.then(function(){
      this.wait(1000, function(){
        if(this.exists('span.postsincelogin')){
          temp = this.getElementsInfo('h3, div.overview.forum .name').map(function(info) {return info.text.trim('\n')})
          var date = new Date();
          this.capture(String (date.getYear()) + String (date.getMonth()+1) + String (date.getDate()) + ".png")
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

    if(result != ""){
      temp_re = result.split(',')
      for(i = 0; i < temp_re.length; i++) {
        if(temp_re[i] == '포럼') {
          temp_result[k] = String(temp_re[i-1] + ',' + temp_re[i+1])
          result = temp_result[k]
        }
        // else{
        //   temp_result[i] = ''
        // }
      }

      // var j = 0;
      // for(i = 0; i < temp_re.length; i++) {
      //   if(temp_result[i] != ''){
      //     temp_result_1[j]= temp_result[i]
      //     j++
      //   }
      // }

      result = result.split(',')

      var Subject = result[0]
      var Content = result[1]
      var date = new Date();
      var Time = String(date.getMonth()+1) + '월 ' + String(date.getDate()) + '일 ' + String(date.getHours()) + '시 ' + String(date.getMinutes()) + '분'

      var objJSON = new Object();
      var objJSONAArr = new Array();

      objJSON.time = Time;
      objJSON.subject = Subject;
      objJSON.content = Content;

      objJSONAArr.push(objJSON);

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
