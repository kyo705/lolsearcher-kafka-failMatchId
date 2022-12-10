# FailMatchIds App

> [LolSearcher App](https://github.com/kyo705/LolSearcher#lolsearcher)
> 으로부터 발생한 fail match id들을 RIOT 게임 서버에게 API 요청으로 데이터를 가져오는 애플리케이션


## 프로젝트 깃 브런치

> - **main** — 실제 메인 브런치(완성본)
> - **develop** — 다음 버전을 위한 개발 브런치(테스트용)

## 프로젝트 커밋 메시지 카테고리

> - [INITIAL] — repository를 생성하고 최초에 파일을 업로드 할 때
> - [ADD] — 신규 파일 추가
> - [UPDATE] — 코드 변경이 일어날때
> - [REFACTOR] — 코드를 리팩토링 했을때
> - [FIX] — 잘못된 링크 정보 변경, 필요한 모듈 추가 및 삭제
> - [REMOVE] — 파일 제거
> - [STYLE] — 디자인 관련 변경사항

## 프로젝트 내 적용 기술
> - Back-End
>   - 언어 : Java
>   - 프레임 워크 : SpringBoot
>   - 빌드 관리 툴 : Gradle
>   - REST API 수집 : WebClient
> - DevOps
>   - MessageQueue : Kafka

## 기능 요구 사항
> 1. MessageQueue로부터 실패한 매치 Id들을 가져옴
> 2. 가져온 Fail Match Ids를 통해 RIOT 게임 서버로부터 Match 데이터를 요청함
> 3. 응답이 성공한 Match 데이터와 실패한 MatchIds를 MessageQueue에 저장함
> 4. 1~3번까지 과정을 계속 반복