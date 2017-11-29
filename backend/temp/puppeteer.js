var express = require ('express');
const puppeteer = require('puppeteer');

var app = express();
var result;
app.get('/test', function (req, res) {
  console.log("@" + req.method + " " + req.url);

  (async () => {
    const browser = await puppeteer.launch({
      headless: true //창을 띄워서 확인하려면 false headless 는 true 옵션
    });
    browser.newPage({ context: 'another-context' })
    const page = await browser.newPage();
    // await page.goto('https://www.kau.ac.kr/page/login.jsp?target_page=act_Lms_Check.jsp@chk1-1'); // lms 로그인창으로 이동
    await page.goto('http://127.0.0.1:3000/lms_before_arr.html') // 테스트용
    // await page.type("[name=p_id]", "2015125061") // id찾아서 넣기
    // await page.type("[name=p_pwd", "01040562889z@") // 비밀번호 찾아서 넣기
    // await page.click("body > div.aside > div.articel > table:nth-child(2) > tbody > tr:nth-child(3) > td > form > table > tbody > tr:nth-child(3) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > a > img") // 로그인 버튼 클릭
    //
    await page.waitForSelector("#wrapper > header.navbar > nav > div > a.brand");
    if (await page.$('span.postsincelogin') != null) {
      result = await page.evaluate(() => {
        const anchors = Array.from(document.querySelectorAll('h3, div.overview.forum .name'));
        return anchors.map(anchor => anchor.textContent);
      });
    } else {
      result = "";
    }
    browser.close();

    result = String (result);
    result = result.replace(/:/g,',');

    var k = 0;
    var objJSONAArr = new Array();
    var temp_re = new Array();
    var temp_result = new Array();
    var temp_result_1 = new Array();

    if (result != "") {
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
        var hour = date.getHours();
        var hour_str;
        if (hour > 12) {
          hour = hour - 12;
          hour_str = "오후 " + String (hour)
        } else {
          hour_str = "오전 " + String (hour)
        }
        var Time = String(date.getMonth()+1) + '월 ' + String(date.getDate()) + '일 ' + hour_str + ' : ' + String(date.getMinutes()) + '분'

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
  })();
})

app.listen(8000, function (){
  console.log ('Connected 8000 port!!!');
});
