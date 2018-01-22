#-*- coding: utf-8 -*-

##웹 크롤러를 이용한 소스코드 스크래핑 Selenium 을 이용함
##Selenium 은 웹 테스팅 모듈

from selenium import webdriver
from bs4 import BeautifulSoup
import re

def subjectNumvering (inform_subject) :
    result = inform_subject
    if "CU" in result or "DU" in result :
        result += "00"
        return result

    if "인문자연" in result :
        result += "1"

    if "항우기" in result :
        result += "2"
    elif "소프트" in result :
        result += "3"
    elif "항공재료" in result :
        result += "4"
    elif "항교물" in result or "항공교통" in result or "항공우주법" in result or "물류" in result :
        result += "5"
    elif "항공운항" in result :
        result += "6"
    elif "영어" in result :
        result += "7"
    elif "경영" in result :
        result += "8"
    elif "항전정" in result :
        result += "9"

    return result

year = "2017"
hakgi = "20" ## 10 - 1학기 15 - 여름학기 20 - 2학기 25 - 겨울학기

driver = webdriver.Chrome () ##selenium 사용을 위한 crhome driver 연결

driver.implicitly_wait(3) ##암묵적으로 3초 기다림 (서버 안정화를 위함)

driver.get('https://www.kau.ac.kr/page/login.jsp?ppage=&target_page=act_Portal_Check.jsp@chk1-1') ##로그인 페이지에 접속
driver.find_element_by_name('p_id').send_keys('2015125061') ##소스코드중에 p_id 를 찾아서 입력
driver.find_element_by_name('p_pwd').send_keys('01040562889z!') ##소스코드중에 P_pwd를 찾아서 입력
driver.find_element_by_xpath('/html/body/div[2]/div[2]/table[2]/tbody/tr[3]/td/form/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr/td[2]/a/img').click() ##login 버튼 클릭


list_index = [1, 3, 4, 5, 6, 7, 9, 10 ,13]
p = re.compile(r">(.*)<")
fname = "list_timetable.txt"

for i in range (1, 48) :
    driver.get('https://portal.kau.ac.kr/sugang/LectDeptSchTop.jsp?year=2017&hakgi=20&hakgwa_name=%C7%D0%BA%CE&hakgwa_code=A0000&gwamok_name=&selhaknyun=%&selyoil=%&jojik_code=A0000&nowPage=' + str(i))

    html = driver.page_source
    soup = BeautifulSoup(html, "html.parser")
    tr_0 = soup.find_all('tr', "tr_0")
    tr_1 = soup.find_all('tr', "tr_1")

    tr_0 = tr_0[1:-1]

    for j in tr_1 :
        temp_time = ""
        list_str = str(j).split("\n")
        list_str = list_str[8:22]
        for k in list_index :
            if list_str[k] == "" :
                continue
            if list_str[k] == '<td align="left">' :
                list_str[k] = list_str[7]
            m = p.search(list_str[k])
            temp_time += m.group(1) + ","
        temp_time = temp_time.replace(" <br/> ", "/")
        temp_time = temp_time.replace("<br/>", "/")
        temp_time = subjectNumvering(temp_time)
        temp_time += "\n"
        fhand = open(fname, "a")
        fhand.write(temp_time)
        fhand.close()

    for j in tr_0 :
        temp_time = ""
        list_str = str(j).split("\n")
        list_str = list_str[8 : 22]
        for k in list_index :
            if list_str[k] == "" :
                continue
            if list_str[k] == '<td align="left">' :
                list_str[k] = list_str[7]
            m = p.search(list_str[k])
            temp_time += m.group(1) + ","
        temp_time = temp_time.replace(" <br/> ", "/")
        temp_time = temp_time.replace("<br/>", "/")
        temp_time = subjectNumvering(temp_time)
        temp_time += "\n"
        fhand = open(fname, "a")
        fhand.write(temp_time)
        fhand.close()
