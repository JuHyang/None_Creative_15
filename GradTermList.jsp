<HTML>
<HEAD>
	<link rel=StyleSheet HREF='/css/haksa.css' type='text/css' title='css'>
	<TITLE>::누적성적조회::</TITLE>

	<script language=javascript>
<!--
	function GradTotList(y,h){

	document.myform.action="GradTotList.jsp?guYear="+y+"&guHakgi="+h;
	document.myform.submit();

	}

	function allResult(hakbun){

	document.myform.action="GradTotList.jsp?guYear=all";
	document.myform.submit();

	}


-->
	</script>
</HEAD>

<body>





<table width="635">
	<tr height="23" valign="middle">
		<td width="23" rowspan="2">
		<img src="/images/title_icon.gif" border="0" align="middle"></td>
		<td width="576" valign="middle">
		<div align="left" valign="middle">&nbsp;&nbsp;<B>누적성적조회</B></div></td>
	</tr>
	<tr height="1">
		<td width="576" valign=bottom><div align="left">
		<img src="/images/title_bar.gif" border="0"  height="1"></div></td>
	</tr>
	<tr height="1"><td colspan="2"></tr>
</table>



<FORM name="myform" method="post">
<input type="hidden" name="hakbun" value="2015125015">
<input type="hidden" name="guYear" value="2018">
<input type="hidden" name="guHakgi" value="10">


<table border=0 width=790 cellspacing=0 cellpadding=0 >
	<tr>
		<td  colspan="8" align="right">
    	<a href="javascript:allResult('2015125015','all');"> 전체 보기 </a>
		</td>
	</tr>
			<table border=0 cellpadding="0" cellspacing="2" width=790 class="table1">
  			<tr height="27" class="tr">
			    <td width="140" align="middle">년도 / 학기</td>
    			<td width="130" align="middle">신청 학점</td>
    			<td width="130" align="middle">취득 학점</td>
    			<td width="130" align="middle">평점합</td>
    			<td width="130" align="middle">평점평균</td>
    			<td width="130" align="middle">비고</td>
   			</tr>


  			<tr height="25" class="tr_0">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2015','10');" >
			    		2015년 1학기</a></td>
    			<td align="center" valign="middle">19			</td>
    			<td align="center" valign="middle">16			</td>
    			<td align="center" valign="middle">49.0		</td>
    			<td align="center" valign="middle">3.27		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

  			<tr height="25" class="tr_1">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2015','20');" >
			    		2015년 2학기</a></td>
    			<td align="center" valign="middle">16			</td>
    			<td align="center" valign="middle">16			</td>
    			<td align="center" valign="middle">60.0		</td>
    			<td align="center" valign="middle">4.0		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

  			<tr height="25" class="tr_0">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2016','10');" >
			    		2016년 1학기</a></td>
    			<td align="center" valign="middle">18			</td>
    			<td align="center" valign="middle">18			</td>
    			<td align="center" valign="middle">72.0		</td>
    			<td align="center" valign="middle">4.0		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

  			<tr height="25" class="tr_1">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2016','20');" >
			    		2016년 2학기</a></td>
    			<td align="center" valign="middle">20			</td>
    			<td align="center" valign="middle">20			</td>
    			<td align="center" valign="middle">75.0		</td>
    			<td align="center" valign="middle">4.17		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

  			<tr height="25" class="tr_0">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2017','10');" >
			    		2017년 1학기</a></td>
    			<td align="center" valign="middle">20			</td>
    			<td align="center" valign="middle">17			</td>
    			<td align="center" valign="middle">60.0		</td>
    			<td align="center" valign="middle">3.33		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

  			<tr height="25" class="tr_1">
			    <td align="center" valign="middle">
			    	<a href="javascript:GradTotList('2017','20');" >
			    		2017년 2학기</a></td>
    			<td align="center" valign="middle">18			</td>
    			<td align="center" valign="middle">18			</td>
    			<td align="center" valign="middle">81.0		</td>
    			<td align="center" valign="middle">4.5		</td>
    			<td align="center" valign="middle">		</td>
   			</tr>

    	</table>
<br>
    	<table border=0 cellpadding="0" cellspacing="2" width=790 class="table1">
  			<tr height="27" class="tr">
  				<td width="190" align="middle"><B> 총 계 </td>
    			<td width="120" align="middle"><B>총신청학점</td>
    			<td width="80" 	align="middle" class="tr_1" >111</td>
    			<td width="120" align="middle"><B>총취득학점</td>
    			<td width="80" 	align="middle" class="tr_1" >105</td>
    			<td width="120" align="middle"><B>총평점</td>
    			<td width="80" 	align="middle" class="tr_1" >397.0</td>
    			<td width="120" align="middle"><B>총평점평균</td>
       		<td width="80" 	align="middle" class="tr_1" >3.89</td>
        </tr>
    	</table>

    </td>
	</tr>


</table>
<input type="hidden" name="sinchungTotSum"	 value="111">
<input type="hidden" name="chwideukTotSum" 	 value="105">
<input type="hidden" name="hipyungjumTotSum" value="397.0">
<input type="hidden" name="totPyung" 				value="3.89">


<table width="790" align="left">
	<tr>
		<td>
		<font color="red">#.</font>F포함 성적임.(2014학년도 1학기 이전 F성적 있는경우 성적증명서의 평점평균과 다를 수 있으므로 확인요망)<br>
		</td>
	</tr>
	<tr>
		<td>
		<font color="red">#.</font>학석사연계과정에서 취득한 대학원 성적은 학부 평점평균 및 총취득학점에서 제외됨<br>
		</td>
	</tr>
</table>

</FORM>
</CENTER>

</BODY>
</HTML>
