<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>マナトーク　クレジット</title>
  <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet" type="text/css">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="credits-wrapper">
  <div class="stars"></div>
  <div class="shooting-stars">
    <span class="shooting-star"></span> <span class="shooting-star"></span>
    <span class="shooting-star"></span> <span class="shooting-star"></span>
    <span class="shooting-star"></span> <span class="shooting-star"></span>
  </div>

  <div class="credits-card">
    <div class="credits-title">クレジット</div>
    <div class="credits-main">開発者</div>
    <div class="credits-main-list">
      <p>チームディレクター : LEE JU-EUN</p>
      <p>プログラマー : KIM SE-JUNG</p>
      <p>プログラマー : KIM YEONG-SEON</p>
      <p>メインプログラマー : PARK GA-HWI</p>
      <p>プログラマー : SONG JI-HWAN</p>
    </div>
    <div class="credits-thanks">外部リソース</div>
    <div class="credits-thanks-list">
      <p>• jQuery - OpenJS Foundation</p>
      <p>• FullCalendar - FullCalendar LLC</p>
      <p>• Hanzi Writer - Chanind</p>

      <p>• <a href="https://www.shiritori-tango.net/" target="_blank">しりとり単語検索</a> (Shiritori Tango)</p>
      <p>• <a href="https://ja.wiktionary.org/" target="_blank">Wiktionary (日本語版)</a></p>
    </div>
    <input class="prev-btn" type="button" value="ログインへ戻る" onclick="location.href='/user/login'">
  </div>
</div>




</body>
</html>