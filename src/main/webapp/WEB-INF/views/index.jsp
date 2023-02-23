<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> --%>

<%
    String _webapp = request.getContextPath();
    String _weburl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_webapp;
%>

<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="imagetoolbar" content="no">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="description" content="ETRI Hotel & Resort">
    <meta name="keyword" content="ETRI Hotel & Resort">
    <title>ETRI Hotel & Resort - 예약관리</title>
    <link rel="stylesheet" href="asset/css/default.css">
    <link rel="stylesheet" href="asset/css/ds_style.css">
    <script type="text/javascript" src="asset/js/jquery-1.12.4.min.js"></script>
    <script>
        $(document).ready(function () {

// 약관전문
            $('.cls_detail').click(function () {
                $('.cls_dialog').css("display", "block");
            });
            $('.cls_dialog_close').click(function () {
                $('.cls_dialog').css("display", "none");
            });
        });
    </script>
</head>
<body>
<div class="ds_container">
    <header class="sub_header">
        <div class="ds_hd_sub_wrap">
            <div class="hd_logo">
                <a href="<%=_webapp%>"><img src="asset/img/logo_top.png" alt="ETRI Hotel & Resort"></a>
            </div>
            <div class="ds_hd_sub">
                <ul>
                    <li><a href="#">관리자 로그아웃</a></li>
                    <li>ㅣ</li>
                    <li><a href="list">예약관리</a></li>
                </ul>
            </div>
        </div>
    </header>
    <section>
        <article class="ds_sub_intro">
            <div class="sub_wrap">
                <div class="sub_lnb_wrap">
                    <div class="sub_lnb_con">
                        <div class="lnb_top_wrap">
                            <div class="mu_dt_1"><img src="asset/img/menu_icon_01.png" alt=""> 관리자</div>
                            <div class="adm_box_wrap btm_bd">
                                <div class="adm_info_wrap">
                                    <div class="adm_info_tit">예약 내역</div>
                                    <div class="adm_info_num num_gry">127</div>
                                </div>
                                <div class="adm_info_wrap">
                                    <div class="adm_info_tit">취소 내역</div>
                                    <div class="adm_info_num num_red" id="cancelCnt">
                                        ${fn:length(cancelList)}
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="mu_dt_1"><img src="asset/img/menu_icon_02.png" alt=""> 예약확인</div>
                        <div class="mu_dt_2">객실 예약 내역</div>
                        <div class="mu_dt_2">다이닝 예약 내역</div>
                        <div class="mu_dt_2 btm_bd">멤버십 예약 내역</div>
                        <div class="mu_dt_1"><img src="asset/img/menu_icon_03.png" alt=""> 예약 취소</div>
                        <div class="mu_dt_2 btm_bd">예약 취소 내역</div>
                    </div>
                </div>
                <div class="sub_con_wrap">
                    <div class="sub_con_area">
                        <div class="page_navi_wrap">
                            <div class="page_tit">예약 취소 내역</div>
                            <div class="page_nav">예약 취소 > <span>예약 취소 내역</span></div>
                        </div>
                        <div class="btn_row_l">
                            <p class="con_btn"><button type="button" name="viewButton" onclick="deleteAll()"><span>초기화</span></button></p>
                            <p class="con_btn"><button type="button" name="viewButton" onClick="location.reload()"><span>새로고침</span></button></p>
                        </div>
                        <div class="tbl_wrap" style="height: 66vh; overflow-y: auto">
                            <table>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>접수일</th>
                                    <th>접수번호</th>
                                    <th>이름</th>
                                    <th>연락처</th>
                                    <th>상태</th>
                                    <th>내용</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty cancelList}">
                                        <c:forEach var="cancel" items="${cancelList}" varStatus="status">
                                            <tr>
                                                <td>${fn:length(cancelList) - status.index}</td>
                                                <td>${cancel.RECDATE}</td>
                                                <td>${cancel.REGSEQ}</td>
                                                <td>${cancel.USERNAME}</td>
                                                <td>${cancel.PHONE}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${cancel.STATUS eq 'W'}">
                                                            취소대기
                                                        </c:when>
                                                        <c:when test="${cancel.STATUS eq 'C'}">
                                                            취소완료
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td><a href="javascript:detail('${cancel.CANCELID}');" class="cls_detail">상세보기 ></a></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7">예약 취소 내역이 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </section>
</div>

<!-- Dialog : S -->
<div class="cls_dialog cls_dialog_wrap" title="예약 취소 내역 상세">
    <div class="cls_dialog_area">
        <div class="cls_tit_wrap">
            <span class="cls_tit">예약 취소 내역 상세</span>
            <a href="javascript:void(0);" class="cls_dialog_close" title="창 닫기"><span class="cls_close_btn"><img src="asset/img/btn_close.png" alt="닫기"></span></a>
        </div>
        <div class="cls_wrap">
            <div class="tbl_wrap">
                <table>
                    <thead>
                    <tr>
                        <th>이름</th>
                        <th>연락처</th>
                        <th>진행상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><span id="popUserName"></span></td>
                        <td><span id="popPhone"></span></td>
                        <td><span id="popStatus"></span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="cls_con_wrap" style="width: 45%; ">
                <div class="cls_con_row">
                    <div class="cls_con_tit"><span>내용</span></div>
                    <div class="cls_con_txt"><span id="popCONTENT"></span></div>
                </div>
                <div class="cls_con_row">
                    <div class="cls_con_tit"><span>첨부파일</span></div>
                    <div class="cls_con_txt"><span id="popORGFILEPATH"></span></div>
                </div>
            </div>
            <div class="btn_row pd_t20">
                <p class="con_btn"><button type="button" name="viewButton" onclick="cancelComplete();"><span>취소 확인</span></button></p>
            </div>
        </div>
    </div>
</div>


<form name="form" method="POST">
    <input type="hidden" id="formSTATUS" name="formSTATUS" value="C"/>
    <input type="hidden" id="cancelId" name="cancelId" value=""/>
</form>
<!-- Dialog : E -->


<script type="text/javascript">
    const _webapp = "<%=_webapp%>";
    const _weburl = "<%=_weburl%>";

    function deleteAll() {
        const f = document.form;

        if(confirm("초기화 하시겠습니까?")) {

            f.action = "<c:url value='/deleteAll'/>";
            f.submit();

        } else {
            return null;
        }

    }

    function detail(cancelId) {
        const jsonObj = new Object();
        jsonObj.cancelId = cancelId;
        const jsonData = JSON.stringify(jsonObj);

        console.log("jsonData: " + jsonData);

        $.ajax({
            dataType: 'json',
            url: "detail",
            type: "POST",
            async: false,
            contentType: "application/json;charset=utf-8",
            data : jsonData,
            success : function(data) {
                console.log(data);

                jQuery("#popUserName").html(data.USERNAME);
                jQuery("#popPhone").html(data.PHONE);
                if(data.STATUS == "W") {
                    jQuery("#popStatus").text("취소대기");
                }
                if(data.STATUS == "C") {
                    jQuery("#popStatus").text("취소완료");
                }
                jQuery("#popCONTENT").html(data.CONTENT);

                let fileHtml = "";
                for(var i=0; i<data.fileInfoList.length; i++) {
                    let fileName = "첨부파일" + (i+1) + "." + data.fileInfoList[i].FILEEXT;
                    let fileUrl = _webapp + "/fileDownload?fileId=" + data.fileInfoList[i].FILEID + "&fileName=" + encodeURI(fileName);

                    let orgFilePath =  _weburl + "/file/img" + data.fileInfoList[i].ORGFILEPATH;

                    let fileExt = data.fileInfoList[i].FILEEXT.toUpperCase();
                    console.log(fileExt);
                    fileHtml += fileName + "<br/>";
                    if (fileExt == "PDF") {
                        fileHtml += "";
                    } else {
                        fileHtml += "<img src='"+orgFilePath+"' alt='증빙 이미지"+[i+1]+"' style='width: 95vh;'> <br/>";
                    }
                    fileHtml += "<br/>";
                }
                jQuery("#popORGFILEPATH").html(fileHtml);

                console.log("cancelId:" + cancelId)
                jQuery("#cancelId").val(cancelId)

            },error : function(xhr,status) {
                console.log("chkBleStatus ERROR - " + status);
            }

        })

    }


    function cancelComplete() {
        const f = document.form;

        f.action = "<c:url value='/update'/>";
        f.submit();
    }

</script>

<iframe id="frmDownload" name="frmDownload" style="display: none;">
</iframe>

</body>
</html>
