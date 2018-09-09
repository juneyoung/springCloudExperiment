# springCloudExperiment
Spring cloud experiment for beginner

## 01. 개요

스프링 클라우드 기능을 테스트 해보기 위해 만드는 더미 프로젝트
- IDE : IntelliJ IDEA
- 의존 관리 : Gradle 4.10

## 02. 시작하기

#### 기본 의존 라이브러리
아래 2 가지 방법 중 하나로 클라우드 프로젝트를 생성할 수 있음. 

- [Spring Initializr](https://start.spring.io) : 의존성에 `Web`, `Cloud Bootstrap`, `Cloud Stream`, `Rest Repositories HAL Browser` 추가하였음
- [Spring Cloud Quick Start](https://projects.spring.io/spring-cloud/) : 생성되는 `build.gradle` 을 사용

#### 라이브러리 임포트 시 문제점

퀵 스타트에서 사용하는 방식은 내부적으로 필요한 라이브러리만 추릴 수 있어 좋으나 현재는 그레들 빌드가 수행되지 않는 문제가 있음.
생성되는 gradle 파일을 사용할 경우 `apply plugin: 'spring-boot'` 를 찾지 못하여 수행되지 않음. 
[스택오버플로우 질의](https://stackoverflow.com/questions/26577805/spring-boot-gradle-plugin-cant-be-found) 처럼 `dependency-management` 를 사용하는 방법이 있다는데 안됨. 
일단은 클라우드의 기능을 확인하는 것이 목표라 Initializr 로 진행 

#### IDEA IDE 사용시 gradle 오류
Initializr 에서 제공된 파일 실행 시에는 그레들 빌드 시에 프록시 문제로 gradle sync 오류가 발생함.
[공식문서](https://docs.gradle.org/current/userguide/userguide_single.html#sec:accessing_the_web_via_a_proxy) 처럼 gradle.properties 를 수정해주면 오류가 나지 않는다.

## 80. 트러블슈팅

#### A. Intellij - gradle : `Unable to resolve org.springframework.aqmp:spring-amqp:2.0..RELEASE`
 gradle 에서 해당 라이브러리를 가져오지 못하는 게 문제라고 가정했기 때문에 처음 시도한 방법은 터미널에서 `gradle clean build -x test --refresh-dependencies --stacktrace` 를 실행했다. 하지만 빌드가 성공했고 별다른 오류 메세지도 출력되지 않았다. 때문에 IDE의 문제라고 판단하여 IDE 로그를 보니 아래와 같은 구문을 찾을 수 있었다.
 ```
 2018-09-07 14:04:24,556 [ 606132]   INFO - xecution.GradleExecutionHelper - Passing command-line args to Gradle Tooling API: -Didea.version=2018.1.3 -Didea.resolveSourceSetDependencies=true -Djava.awt.headless=true -Pandroid.injected.build.model.only=true -Pandroid.injected.build.model.only.advanced=true -Pandroid.injected.invoked.from.ide=true -Pandroid.injected.build.model.only.versioned=3 --init-script /private/var/folders/w7/c3mkf3h514dc86cc4l72q8mm0000gn/T/ijinit.gradle --offline 
2018-09-07 14:04:38,421 [ 619997]   INFO - .project.GradleProjectResolver - Gradle project resolve error 
org.gradle.tooling.BuildException: Could not run build action using Gradle distribution 'https://services.gradle.org/distributions/gradle-4.8.1-all.zip'.
	at org.gradle.tooling.internal.consumer.ExceptionTransformer.transform(ExceptionTransformer.java:51)
	at org.gradle.tooling.internal.consumer.ExceptionTransformer.transform(ExceptionTransformer.java:29)
 ```
`4.8.11` 이라는 항목을 본 순간, 해당 부분이 `gradle-wrapper.properties` 항목에 있는 게 기억이 났고, 프로젝트 내 빌드  `use local gradle distribution` 항목에 체크를 하고 머신에 있는 4.10 버전의 홈으로 연결해서 정상적으로 연동이 가능해졌다.

#### B. Rabbit mq `WARNING: module crypto not found, so not scanned for boot steps.`
기본적으로 라이브러리는 `standalone` 버전으로 설치해서 사용하는 편이다. 하지만 rabbitmq 의 경우 OSX 10.x 에서 사용할 경우, openssl 이슈가 발생한다. 해당crypto 라는 건 erlang 의 라이브러리고 래빗엠큐를 설치할 때 얼랭의 디펜던시까지는 확인을 해주지 않는다. rabbitmq user groups 에 해당 문제에 대해 질문을 했고, brew 나 kerl 을 사용하면 문제없을 거라 답변받았음.

#### C. Rabbit mq - RabbitMessageTemplate 권한 문제
```
com.rabbitmq.client.AuthenticationFailureException: ACCESS_REFUSED - Login was refused using authentication mechanism PLAIN. For details see the broker logfile.
```
그래서 broker log 파일을 보니 아래와 같은 구문을 찾을 수 있었음 
```
2018-09-09 15:55:32.307 [error] <0.406.0> Error on AMQP connection <0.406.0> ([::1]:50156 -> [::1]:5672, state: starting):
PLAIN login refused: user 'guest' - invalid credentials
```
수정방법
```
# 사용자 목록 출력. guest 사용자가 존재하지 않았음
$ > rabbitmqctl list_users

# 사용자 추가 
$ > rabbitmqctl add_user guest guest

# 사용자에게 관리자 권한 부여
$ > rabbitmqcrl set_user_tags guest administator

# 사용자 비밀번호 변경
$ > rabbitmqctl change_password guest guest

# 퍼미션 추가 - 이 부분은 무슨 프로세스인지 확인 필요 
$ > rabbitmqctl set_permissions -p / guest ".*" ".*" ".*"
```
레드헷 쪽 포럼에서 찾았음 : 
- [레빗 공식](https://www.rabbitmq.com/rabbitmqctl.8.html)
- [링크 1](https://access.redhat.com/solutions/2172871)
- [링크 2](https://gist.github.com/sdieunidou/1813409ddfd0185c82c7)

## 99. 내역
- 20180907 초안 작성중
