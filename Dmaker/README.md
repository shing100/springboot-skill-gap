# 스프링 프레임워크 핵심기술

## Core (DI, IoC)
스프링의 근간, 내가 만든 클래스를 스프링이 직접 관리하여 어플리케이션을 동작하게 한다

## AOP(Aspect Oriented Programming)
공통적인 코드를 프레임워크 레벨에서 지원해주는 방법

## Validation, Data binding
검증 그리고 외부에서 받은 데이터를 담아내는 방법

## Resource
스프링 내부에서 설정이 들어있는 파일들에 접근하는 동작 원리

## SpEL
짧은 표현식을 통해 필요한 데이터나 설정 값을 얻어올 수 있게 하는 특별한 형태의 표현식에 가까운 간편한 언어

## Null-Safety
Null을 조금 더 잘 다루고 싶다면?


# 스프링의 디자인 철학

- 모든 기능에서 다양한 가능성(다양한 모듈)을 사용 가능, 심지어 외부 모듈을 활용 가능
    - 너무 높은 자유도 어떤 점에서는 스프링을 어렵게 하는 요소
- 유연하게 계속 추가 개발을 하고 있는 프레임워크
- 이전 버전과의 강력한 호환성
    - 너무 많은 레거시 때문에 코드의 복잡성이 높아지긴 함
- API 디자인을 섬세하게 노력한다
    - 스프링 코드 자체가 하나의 좋은 참고 소스
- 높은 코드 품질을 유지하려 함
    - 스프링 프로젝트 github은 아주 좋은 참고 소스이자 PR과 이슈 관리도 좋은 프로세스 참고용

→ 한마디로 높은 자유도를 주고 계속 발전하는 고품질의 다양성이 있는 프로젝트, 그런데 너무 자유로워서 때론 어렵다.

### References

[Overview of Spring Framework](https://docs.spring.io/spring-framework/docs/5.0.0.RC2/spring-framework-reference/overview.html#overview-modules)

[Spring Framework Overview](https://docs.spring.io/spring-framework/docs/current/reference/html/overview.html#overview-philosophy)

[Core Technologies](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#spring-core)


## Bean이란?

### 자바에서의 javaBean

- 데이터를 저장하기 위한 구조체로 자바 빈 규약이라는 것을 따르는 구조체
- private 프로퍼티와 getter/setter로만 데이터를 접근한다.
- 인수(argument)가 없는 기본 생성자가 있다.

```java
public class JavaBean {
	private String id;
	private Integer count;

	public JavaBean(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
```

### 스프링에서의 Bean

- 스프링 IoC 컨테이너에 의해 생성되고 관리되는 객체
- 자바에서처럼 new Object(); 로 생성하지 않는다
- 각각의 Bean들 끼리는 서로를 편리하게 의존(사용)할 수 있음

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ce2adaf9-5924-49bd-bca1-300499544985/Untitled.png

## 스프링 컨테이너 개요

ApplicationContext 인터페이스를 통해 제공되는 스프링 컨테이너는 Bean 객체의 생성 및 Bean들의 조립(상호 의존성 관리)을 담당합니다.

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ce2adaf9-5924-49bd-bca1-300499544985/Untitled.png

### Bean의 등록

- 과거에는 xml로 설정을 따로 관리하여 등록(불편)
- 현재는 annotation 기반으로 Bean 등록
    - @Bean, @Controller, @Service

### Bean 등록 시 정보

- Class 경로
- Bean의 이름
    - 기본적으로는 원 Class 이름에서 첫 문자만 소문자로 변경  → accountService, userDao
    - 원하는 경우 변경 가능
- Scope : Bean을 생성하는 규칙
    - singleton : 컨테이너에 단일로 생성
    - prototype: 작업 시마다 Bean을 새로 생성하고 싶을 경우
    - request: http 요청마다 새롭게 Bean을 생성하고 싶은 경우

### Bean LifeCycle callback(빈 생명주기 콜백함수)

- callback : 어떤 이벤트가 발생하는 경우 호출되는 메서드
- lifecycle callback
    - Bean을 생성하고 초기화하고 파괴하는 등 특정 시점에 호출되도록 정의된 함수
- 주로 많이 사용되는 콜백
    - @PostConstruct : 빈 생성 시점에 필요한 작업을 수행
    - @PreDestroy : 빈 파괴(주로 어플리케이션 종료) 시점에 필요한 작업을 수행

### References

[Core Technologies](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans)

[JavaBeans - Wikipedia](https://en.wikipedia.org/wiki/JavaBeans)


# Null Safety

널 안정성을 높이는 방법

- 아래와 같은 코드를 만들지 않는 방법
- 혹은 아래와 같은 널 체크를 하지 않아서 발생하는 NPE(Null Pointer Exception)을 방지하는 방법
- 완벽한 방법은 아니지만 IDE(Intellij, Eclipse)에서 경고를 표시함으로써 1차적인 문제를 방지하고, 정확한 에러 위치를 확인할 수 있도록 도움

```java
public void method(String request) {
	if(request == null) return;

	// normal process
	System.out.println(request.toUpperCase());
}
```

---

## @NonNull Annotation

- 해당 값이나 함수 등이 Null이 아님을 나타내는 어노테이션
- org.springframework.lang.NonNull 사용
- 메서드 파라미터에 붙이는 경우 : null이라는 데이터가 들어오는 것을 사전에 방지함

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bf51ba76-a104-410e-ae99-a05bd6ef5fe7/Untitled.png

- 프로퍼티에 붙이는 경우 : null을 저장하는 경우 경고

  !https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4ee6aee5-cee9-4f60-8d6f-76e5768daa94/Untitled.png

- 메서드에 붙이는 경우 : null을 리턴하는 경우 경고, 응답값을 저장하거나 활용하는 쪽도 NonNull이라고 신뢰하고 사용

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c2e5a4a5-530a-483b-b0f0-839cbbd60502/Untitled.png

---

## @Nullable Annotation

- @NonNull과 반대로 해당 데이터가 null일 수 있음을 명시함
- 해당 어노테이션이 붙은 값을 사용하는 경우 null check를 항상 수행하도록 경고

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/be64dc25-c292-4ee0-8265-1dc010873b13/Untitled.png

---

## Null 관련 어노테이션 참고

- jetbrain(intellij 개발회사) : https://www.jetbrains.com/help/idea/nullable-and-notnull-annotations.html
- lombok : https://projectlombok.org/features/NonNull
