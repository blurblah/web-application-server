# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* Header에서 request 분리
* BufferedReader => Reader <= InputStreamReader - InputStream
* index.html 요청시 css 등의 resource 요청도 들어올 수 있는데 응답 header의 Content-Type을 맞춰주어야 함

### 요구사항 2 - get 방식으로 회원가입
* GET 방식으로 form에서의 요청이 전달되면 url parameter로 입력값이 전달됨

### 요구사항 3 - post 방식으로 회원가입
* POST 방식인 경우 form의 입력값이 body에 실려옴
* body에 실릴 경우 header에 body의 길이가 Content-Length로 전달됨
* header 읽은 후에 Content-Length 만큼만 BufferedReader로 읽으면 완료 

### 요구사항 4 - redirect 방식으로 이동
* URL을 redirect로 보낼 때 header 응답은 302 Found
* URL까지 변경하려면 Location을 header에 추가
* Client(browser)가 받은 응답이 302인 경우 Location을 확인해 서버로 재요청함 

### 요구사항 5 - cookie
* 응답 header에 Set-Cookie를 실어서 보내면 요청시 해당 값을 가진 Cookie가 header에 포함되어 요청됨

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 