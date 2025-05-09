# 🎮 Mafia 게임 프로젝트

자바 네트워크 프로그래밍을 활용한 마피아 게임 프로젝트입니다. 클라이언트-서버 아키텍처를 기반으로 멀티플레이어 마피아 게임을 구현했습니다.

## 프로젝트 개요

이 프로젝트는 자바의 소켓 프로그래밍을 활용하여 실시간 멀티플레이어 마피아 게임을 구현했습니다. 사용자는 클라이언트 애플리케이션을 통해 게임 서버에 접속하고, 다른 플레이어들과 함께 마피아 게임을 즐길 수 있습니다.

## 주요 기능

- **역할 배정**: 각 플레이어는 게임 시작 시 시민, 마피아, 의사, 경찰 중 하나의 역할을 무작위로 부여받습니다.
- **주/야간 사이클**: 게임은 낮(토론), 투표, 밤(직업 활동) 사이클로 진행됩니다.
- **역할별 특수 능력**:
  - **마피아**: 밤에 한 명의 시민을 지목하여 살해할 수 있습니다.
  - **의사**: 밤에 한 명의 플레이어를 지목하여 마피아의 공격으로부터 보호할 수 있습니다.
  - **경찰**: 밤에 한 명의 플레이어를 조사하여 그 역할을 알아낼 수 있습니다.
- **투표 시스템**: 낮 시간에 플레이어들은 마피아로 의심되는 사람을 투표로 추방할 수 있습니다.
- **채팅 기능**: 플레이어들은 게임 내에서 실시간으로 대화할 수 있습니다.
- **승리 조건**: 
  - **시민팀 승리**: 모든 마피아를 제거하면 시민팀이 승리합니다.
  - **마피아팀 승리**: 마피아의 수가 시민팀의 수 이상이 되면 마피아팀이 승리합니다.

## 기술 스택

- **언어**: Java
- **UI**: Swing(그래픽 사용자 인터페이스)
- **네트워킹**: Java Socket API(TCP/IP 통신)
- **동시성**: Java Thread API(멀티스레딩)

## 구조

- **Server.java**: 게임 서버 및 클라이언트 연결 관리
- **ServerThread.java**: 각 클라이언트 연결을 처리하는 스레드
- **Client.java**: 클라이언트 애플리케이션 메인 클래스
- **Game.java**: 게임 로직을 구현한 핵심 클래스
- **User.java**: 게임 플레이어 정보 저장 클래스
- **DayNight.java**: 게임의 시간대(낮/밤) 및 단계를 나타내는 열거형
- **기타 UI 관련 클래스**: 게임 화면, 채팅 인터페이스 등을 구현

## 게임 진행 흐름

1. 서버 시작 및 클라이언트 연결
2. 플레이어들이 게임에 참여
3. 게임 시작 시 각 플레이어에게 역할 무작위 배정
4. 주/야간 사이클 반복:
   - **낮(DAY)**: 플레이어 간 토론
   - **투표(VOTE)**: 의심되는 플레이어 투표
   - **밤(NIGHT)**: 마피아 활동
   - **조사(POLICE)**: 경찰 조사 활동
   - **치료(HEAL)**: 의사 치료 활동
5. 승리 조건 충족 시 게임 종료

## 실행 방법

1. 서버 실행:
   ```bash
   java -cp . mafia.Server
   ```

2. 클라이언트 실행:
   ```bash
   java -cp . mafia.Client
   ```

3. 클라이언트에서 서버 IP와 사용자 이름을 입력하여 접속
