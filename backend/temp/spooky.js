try {
    var Spooky = require('spooky');
} catch (e) {
    var Spooky = require('../lib/spooky');
}


var url = "http://lms.kau.ac.kr"
// var url = "http://127.0.0.1:3000/before.html" //테스트 용
var temp

var spooky = new Spooky({
  child: {
    transport: 'http'
    , command : 'casperjs.cmd'
/*
events.js:160
throw er; // Unhandled 'error' event

Error: spawn casperjs ENOENT

            해결 하는 부분 command 추가  in windows

            우분투에선 command :부분 제거
            */
  },
  casper: {
    logLevel: 'debug',
    verbose: true
  }
  }, function (err) {
    if (err) {
      e = new Error('Failed to initialize SpookyJS');
      e.details = err;
      throw e;
  }

  spooky.start(url);

  spooky.then (function () {
    this.click('#btn_sso_login')
  })


  spooky.then (function () {
    this.capture('login.png')
  })

  spooky.then (function () {
    var id = "id" // id 입력
    var password = "password" //password 입력
    this.fill ('form[name=LoginForm]', {
      'p_id' : id,
      'p_pwd' : password
    }, true);
  })

  spooky.then (function () {
    this.wait(5000, function () {
      if (this.exists('span.postsincelogin')) {
        temp = this.getElementsInfo('h3, div.overview.forum .name').map(function(info) {return info.text.trim('\n')})
        var date = new Date();
        this.capture(String (date.getYear()) + String (date.getMonth()+1) + String (date.getDate()) + ".png")
      }
      })
  });

  spooky.run();
});

spooky.on('error', function (e, stack) {
    console.error(e);

    if (stack) {
        console.log(stack);
    }
});
