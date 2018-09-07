# springCloudExperiment
Spring cloud experiment for beginner

## 01. 개요

스프링 클라우드 기능을 테스트 해보기 위해 만드는 더미 프로젝트
- IDE : IntelliJ IDEA
- 의존 관리 : Gradle 4.10

## 02. 시작하기

#### 기본 의존 라이브러리

- [Spring Initializr](https://start.spring.io) : 의존성에 `Web`, `Cloud Bootstrap`, `Cloud Stream`, `Rest Repositories HAL Browser` 추가하였음
- [Spring Cloud Quick Start](https://projects.spring.io/spring-cloud/) : 생성되는 `build.gradle` 을 사용

#### 라이브러리 임포트 시 문제점

퀵 스타트에서 사용하는 방식은 내부적으로 필요한 라이브러리만 추릴 수 있어 좋으나 현재는 그레들 빌드가 수행되지 않는 문제가 있음.
생성되는 gradle 파일을 사용할 경우 `apply plugin: 'spring-boot'` 를 찾지 못하여 수행되지 않음. 
[스택오버플로우 질의](https://stackoverflow.com/questions/26577805/spring-boot-gradle-plugin-cant-be-found) 처럼 `dependency-management` 를 사용하는 방법이 있다는데 안됨. 
일단은 클라우드의 기능을 확인하는 것이 목표라 Initializr 로 진행 

## 99. 내역
- 20180907 초안 작성중
