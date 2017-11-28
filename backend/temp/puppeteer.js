var express = require ('express');
const puppeteer = require('puppeteer');

var app = express();

app.get('/test', function (req, res) {
  count = count + 1;
  console.log("@" + req.method + " " + req.url);

  (async () => {
    const browser = await puppeteer.launch({
      headless: false //창을 띄워서 확인하려면 false headless 는 false 옵션
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

    let subject_content = await page.evaluate(() => {
      const anchors = Array.from(document.querySelectorAll('h3, div.overview.forum .name'));
      return anchors.map(anchor => anchor.textContent);
    });
    browser.close();
    console.log(subject_content.join('\n'));
    res.send(subject_content.join('\n'));
  })();

})

app.listen(8000, function (){
  console.log ('Connected 8000 port!!!');
});
