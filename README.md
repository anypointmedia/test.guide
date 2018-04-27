# test-guide
Spring Boot과 Spock Test Framework를 사용하는 테스트 샘플코드를 소개<br>
Data Driven Testing을 적용<br>
docker-compose를 활용하여 외부 환경 구동 처리<br>


## Prerequisites
* install docker [https://docs.docker.com/install/]
* install docker-compose [https://docs.docker.com/compose/install/]
* install jdk 1.8 [http://openjdk.java.net/install/]

## 주요 테스트 항목
* [Spock Genesis](https://github.com/Bijnagte/spock-genesis)와 [Random Beans](https://github.com/benas/random-beans)를 사용한 테스트
* @WebMvcTest 테스트
* @SpringBootTest 테스트
* @DataJpaTest 테스트
* [Testcontainers](https://www.testcontainers.org/)와 docker-compose를 사용한 테스트